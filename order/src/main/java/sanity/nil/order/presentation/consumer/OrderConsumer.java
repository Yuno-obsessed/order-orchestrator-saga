package sanity.nil.order.presentation.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import sanity.nil.order.domain.consts.EventStatus;
import sanity.nil.order.domain.event.OrderCreateStartedEvent;
import sanity.nil.order.infrastructure.broker.dto.CustomerBalanceFailedEvent;
import sanity.nil.order.infrastructure.broker.dto.CustomerBalanceVerifiedEvent;
import sanity.nil.order.infrastructure.broker.dto.OrderCreatedEvent;
import sanity.nil.order.infrastructure.broker.dto.OrderRejectedEvent;
import sanity.nil.order.infrastructure.broker.exceptions.RequiredHeaderEmptyException;
import sanity.nil.order.infrastructure.saga.impl.OrderCreationSaga;

import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final OrderCreationSaga orderCreationSaga;

    @KafkaListener(topics = "${application.broker.order-topic}", groupId = "1")
    public void orderEntityEvents(@Payload ConsumerRecord<String, String> message) {

        String eventType = Arrays.stream(message.headers().toArray())
                .filter(h -> h.key().equals("event_type"))
                .findFirst()
                .map(Header::value)
                .map(String::new)
                .orElseThrow(() -> new RequiredHeaderEmptyException("event_type"));

        String eventPayload = message.value();

        String sagaHeaderValue;
        UUID sagaID;
        try {
            switch (eventType) {
                case "OrderCreateStarted":
                    OrderCreateStartedEvent.Payload orderCreateStartedEventPayload =
                            objectMapper.readValue(eventPayload, OrderCreateStartedEvent.Payload.class);
                    log.info(orderCreateStartedEventPayload.toString());

                    OrderCreateStartedEvent orderCreateStartedEvent =
                            new OrderCreateStartedEvent(orderCreateStartedEventPayload, EventStatus.PROCESSED, eventType);
                    orderCreationSaga.begin(orderCreateStartedEvent);
                    break;

                case "OrderRejected":
                    sagaHeaderValue = Arrays.stream(message.headers().toArray())
                            .filter(h -> h.key().equals("saga_id"))
                            .findFirst()
                            .map(Header::value)
                            .map(String::new)
                            .orElseThrow(() -> new RequiredHeaderEmptyException("saga_id"));
                    sagaID = UUID.fromString(sagaHeaderValue);

                    OrderRejectedEvent.Payload orderRejectedEventPayload =
                            objectMapper.readValue(eventPayload, OrderRejectedEvent.Payload.class);
                    log.info(orderRejectedEventPayload.toString());

                    OrderRejectedEvent orderRejectedEvent =
                            new OrderRejectedEvent(orderRejectedEventPayload, EventStatus.PROCESSED, eventType, sagaID);
                    orderCreationSaga.orderRejected(orderRejectedEvent);
                    break;

                case "OrderCreated":
                    sagaHeaderValue = Arrays.stream(message.headers().toArray())
                            .filter(h -> h.key().equals("saga_id"))
                            .findFirst()
                            .map(Header::value)
                            .map(String::new)
                            .orElseThrow(() -> new RequiredHeaderEmptyException("saga_id"));
                    sagaID = UUID.fromString(sagaHeaderValue);

                    OrderCreatedEvent.Payload orderCreatedEventPayload =
                            objectMapper.readValue(eventPayload, OrderCreatedEvent.Payload.class);
                    log.info(orderCreatedEventPayload.toString());

                    OrderCreatedEvent orderCreatedEvent =
                            new OrderCreatedEvent(orderCreatedEventPayload, EventStatus.PROCESSED, eventType, sagaID);
                    orderCreationSaga.orderCreated(orderCreatedEvent);
            }
        } catch (JsonProcessingException e1) {
            log.error(e1.getMessage());
            return;
        }
    }

    @KafkaListener(topics = "${application.broker.account-topic}", groupId = "1" )
    public void customerEntityEvents(@Payload ConsumerRecord<String, String> message) {

        String eventType = Arrays.stream(message.headers().toArray())
                .filter(h -> h.key().equals("event_type"))
                .findFirst()
                .map(Header::value)
                .map(String::new)
                .orElseThrow(() -> new RequiredHeaderEmptyException("event_type"));

        String sagaHeaderValue = Arrays.stream(message.headers().toArray())
                .filter(h -> h.key().equals("saga_id"))
                .findFirst()
                .map(Header::value)
                .map(String::new)
                .orElseThrow(() -> new RequiredHeaderEmptyException("saga_id"));

        UUID sagaID = UUID.fromString(sagaHeaderValue);

        String eventPayload = message.value();

        try {
            switch (eventType) {
                case "CustomerBalanceVerified":
                    CustomerBalanceVerifiedEvent.Payload customerBalanceVerifiedEventPayload =
                            objectMapper.readValue(eventPayload, CustomerBalanceVerifiedEvent.Payload.class);
                    log.info(customerBalanceVerifiedEventPayload.toString());

                    CustomerBalanceVerifiedEvent customerBalanceVerifiedEvent =
                            new CustomerBalanceVerifiedEvent(customerBalanceVerifiedEventPayload, EventStatus.PROCESSED, eventType, sagaID);
                    orderCreationSaga.balanceVerified(customerBalanceVerifiedEvent);
                    break;

                case "CustomerBalanceFailed":
                    CustomerBalanceFailedEvent.Payload customerBalanceFailedEventPayload =
                            objectMapper.readValue(eventPayload, CustomerBalanceFailedEvent.Payload.class);
                    log.info(customerBalanceFailedEventPayload.toString());

                    CustomerBalanceFailedEvent customerBalanceFailedEvent =
                            new CustomerBalanceFailedEvent(customerBalanceFailedEventPayload, EventStatus.PROCESSED, eventType, sagaID);
                    orderCreationSaga.balanceFailed(customerBalanceFailedEvent);
                    break;
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return;
        }
    }

}
