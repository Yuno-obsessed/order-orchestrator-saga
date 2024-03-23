package sanity.nil.order.infrastructure.saga.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import sanity.nil.order.application.OrderCommands;
import sanity.nil.order.application.dto.CreateOrderCommandDTO;
import sanity.nil.order.application.dto.EventMessage;
import sanity.nil.order.application.dto.RejectOrderCommandDTO;
import sanity.nil.order.application.interfaces.OutboxRepository;
import sanity.nil.order.domain.consts.EventStatus;
import sanity.nil.order.domain.event.OrderCreateStartedEvent;
import sanity.nil.order.infrastructure.broker.dto.*;
import sanity.nil.order.infrastructure.saga.SagaNotFoundException;
import sanity.nil.order.infrastructure.saga.SagaRepository;
import sanity.nil.order.infrastructure.saga.SagaState;
import sanity.nil.order.infrastructure.saga.consts.SagaStatus;
import sanity.nil.order.infrastructure.saga.consts.SagaStepStatus;

import java.util.UUID;

@RequiredArgsConstructor
public class OrderCreationSaga {

    private final OrderCommands orderCommands;
    private final SagaRepository sagaRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void begin(OrderCreateStartedEvent event) {
        SagaState sagaState = sagaRepository.newSaga();

        sagaState.setID(UUID.randomUUID());
        sagaState.setType("order-creation");
        sagaState.setStatus(SagaStatus.STARTED);

        EventMessage message = new EventMessage(objectMapper, event, sagaState.getID());
        ArrayNode payloadArray = JsonNodeFactory.instance.arrayNode();
        ObjectNode payload = objectMapper.valueToTree(message);
        payload.remove("saga_id");
        payloadArray.add(payload);

        ObjectNode step = JsonNodeFactory.instance.objectNode();
        step.put("status", SagaStepStatus.STARTED.name());
        step.put("type", event.getType());

        sagaState.setCurrentStep(step);
        sagaState.setPayload(payloadArray);

        sagaRepository.saveSaga(sagaState);

        OrderCreateStartedEvent.Payload orderPayload = (OrderCreateStartedEvent.Payload) event.getPayload();
        VerifyCustomerEvent verifyCustomerEvent = new VerifyCustomerEvent(
                orderPayload.userID(), orderPayload.orderID(), orderPayload.amount(), EventStatus.AWAITING, sagaState.getID()
        );

        outboxRepository.saveMessage(new EventMessage(objectMapper, verifyCustomerEvent, sagaState.getID()));
    }

    public void balanceVerified(CustomerBalanceVerifiedEvent event) {
        SagaState sagaState = sagaRepository.getSagaByID(event.getSagaID()).orElseThrow(
                () -> new SagaNotFoundException(event.getSagaID())
        );

        EventMessage message = new EventMessage(objectMapper, event, event.getSagaID());

        ArrayNode payloadArray = (ArrayNode) sagaState.getPayload();
        ObjectNode payload = objectMapper.valueToTree(message);
        payload.remove("saga_id");
        payloadArray.add(payload);

        ObjectNode step = JsonNodeFactory.instance.objectNode();
        step.put("status", SagaStepStatus.COMPENSATING.name());
        step.put("type", event.getType());

        sagaState.setCurrentStep(step);
        sagaState.setPayload(payloadArray);

        sagaRepository.updateSaga(sagaState);

        CustomerBalanceVerifiedEvent.Payload customerPayload = (CustomerBalanceVerifiedEvent.Payload) event.getPayload();
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(customerPayload.userID(), customerPayload.orderID(),
                EventStatus.AWAITING, sagaState.getID()
        );

        outboxRepository.saveMessage(new EventMessage(objectMapper, orderCreatedEvent, event.getSagaID()));
    }

    public void balanceFailed(CustomerBalanceFailedEvent event) {
        SagaState sagaState = sagaRepository.getSagaByID(event.getSagaID()).orElseThrow(
                () -> new SagaNotFoundException(event.getSagaID())
        );

        EventMessage message = new EventMessage(objectMapper, event, event.getSagaID());

        ArrayNode payloadArray = (ArrayNode) sagaState.getPayload();
        ObjectNode payload = objectMapper.valueToTree(message);
        payload.remove("saga_id");
        payloadArray.add(payload);

        ObjectNode step = JsonNodeFactory.instance.objectNode();
        step.put("status", SagaStepStatus.FAILED.name());
        step.put("type", event.getType());

        sagaState.setCurrentStep(step);
        sagaState.setPayload(payloadArray);
        sagaState.setStatus(SagaStatus.ABORTING);

        sagaRepository.updateSaga(sagaState);

        CustomerBalanceFailedEvent.Payload customerPayload = (CustomerBalanceFailedEvent.Payload) event.getPayload();
        OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(customerPayload.userID(), customerPayload.orderID(),
                EventStatus.AWAITING, sagaState.getID()
        );
        outboxRepository.saveMessage(new EventMessage(objectMapper, orderRejectedEvent, event.getSagaID()));
    }

    public void orderRejected(OrderRejectedEvent event) {
        SagaState sagaState = sagaRepository.getSagaByID(event.getSagaID()).orElseThrow(
                () -> new SagaNotFoundException(event.getSagaID())
        );

        OrderRejectedEvent.Payload orderRejectedPayload = (OrderRejectedEvent.Payload) event.getPayload();
        orderCommands.rejectOrderCommand.handle(
                new RejectOrderCommandDTO(orderRejectedPayload.orderID(), orderRejectedPayload.userID())
        );

        EventMessage message = new EventMessage(objectMapper, event, event.getSagaID());

        ArrayNode payloadArray = (ArrayNode) sagaState.getPayload();
        ObjectNode payload = objectMapper.valueToTree(message);
        payload.remove("saga_id");
        payloadArray.add(payload);

        ObjectNode step = JsonNodeFactory.instance.objectNode();
        step.put("status", SagaStepStatus.FAILED.name());
        step.put("type", event.getType());

        sagaState.setStatus(SagaStatus.ABORTED);
        sagaState.setCurrentStep(step);
        sagaState.setPayload(payloadArray);

        sagaRepository.updateSaga(sagaState);
    }

    public void orderCreated(OrderCreatedEvent event) {
        SagaState sagaState = sagaRepository.getSagaByID(event.getSagaID()).orElseThrow(
                () -> new SagaNotFoundException(event.getSagaID())
        );

        OrderCreatedEvent.Payload orderCreatedEventPayload = (OrderCreatedEvent.Payload) event.getPayload();
        orderCommands.createOrderCommand.handle(
                new CreateOrderCommandDTO(orderCreatedEventPayload.orderID(), orderCreatedEventPayload.userID())
        );

        EventMessage message = new EventMessage(objectMapper, event, event.getSagaID());

        ArrayNode payloadArray = (ArrayNode) sagaState.getPayload();
        ObjectNode payload = objectMapper.valueToTree(message);
        payload.remove("saga_id");
        payloadArray.add(payload);

        ObjectNode step = JsonNodeFactory.instance.objectNode();
        step.put("status", SagaStepStatus.SUCCEEDED.name());
        step.put("type", event.getType());

        sagaState.setCurrentStep(step);
        sagaState.setPayload(payloadArray);
        sagaState.setStatus(SagaStatus.COMPLETED);

        sagaRepository.updateSaga(sagaState);
    }

}
