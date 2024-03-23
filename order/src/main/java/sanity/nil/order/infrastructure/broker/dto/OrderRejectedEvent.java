package sanity.nil.order.infrastructure.broker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import sanity.nil.order.domain.consts.EventStatus;
import sanity.nil.order.domain.event.Event;
import sanity.nil.order.infrastructure.saga.SagaEvent;

import java.util.UUID;

@ToString
public class OrderRejectedEvent implements Event, SagaEvent {

    @JsonProperty(value = "payload")
    private Payload payload;

    @JsonProperty(value = "event_status")
    private EventStatus eventStatus;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "saga_id")
    private UUID sagaID;

    public record Payload (
            @JsonProperty(value = "user_id") UUID userID,
            @JsonProperty(value = "order_id") UUID orderID
    ) {}

    @Override
    public UUID getEntityID() {
        return payload.orderID;
    }

    @Override
    public String getEntityType() {
        return "order";
    }

    @Override
    public EventStatus getStatus() {
        return eventStatus;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public UUID getSagaID() {
        return sagaID;
    }

    @Override
    public void setSagaID(UUID sagaID) {
        this.sagaID = sagaID;
    }

    public OrderRejectedEvent(UUID userID, UUID orderID,
                                      EventStatus eventStatus, UUID sagaID) {
        this.payload = new Payload(orderID, userID);
        this.eventStatus = eventStatus;
        this.type = "OrderRejected";
        this.sagaID = sagaID;
    }

    public OrderRejectedEvent(Payload payload, EventStatus eventStatus, String type, UUID sagaID) {
        this.payload = payload;
        this.eventStatus = eventStatus;
        this.type = type;
        this.sagaID = sagaID;
    }
}
