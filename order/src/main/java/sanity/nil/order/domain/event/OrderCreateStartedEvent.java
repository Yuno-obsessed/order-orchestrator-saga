package sanity.nil.order.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import sanity.nil.order.domain.consts.EventStatus;

import java.math.BigDecimal;
import java.util.UUID;

@ToString
public class OrderCreateStartedEvent implements Event {

    @JsonProperty(value = "payload")
    private Payload payload;

    @JsonProperty(value = "event_status")
    private EventStatus eventStatus;

    @JsonProperty(value = "type")
    private String type;

    public record Payload (
        @JsonProperty(value = "order_id") UUID orderID,
        @JsonProperty(value = "user_id") UUID userID,
        @JsonProperty(value = "trace_id") UUID traceID,
        @JsonProperty(value = "amount") BigDecimal amount
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

    public OrderCreateStartedEvent(UUID orderID, UUID userID, UUID traceID, BigDecimal amount,
                                   EventStatus eventStatus) {
         this.payload = new Payload(orderID, userID, traceID, amount);
         this.eventStatus = eventStatus;
         this.type = "OrderCreateStarted";
    }

    public OrderCreateStartedEvent(Payload payload, EventStatus eventStatus, String type) {
        this.payload = payload;
        this.eventStatus = eventStatus;
        this.type = type;
    }
}
