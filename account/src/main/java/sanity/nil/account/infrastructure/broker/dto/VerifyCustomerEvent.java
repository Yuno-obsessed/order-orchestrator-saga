package sanity.nil.account.infrastructure.broker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import sanity.nil.account.domain.consts.EventStatus;
import sanity.nil.account.domain.event.Event;

import java.math.BigDecimal;
import java.util.UUID;

public class VerifyCustomerEvent implements Event {

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
            @JsonProperty(value = "order_id") UUID orderID,
            @JsonProperty(value = "amount") BigDecimal amount
    ) {}

    @Override
    public UUID getEntityID() {
        return payload.orderID;
    }

    @Override
    public String getEntityType() {
        return "order-account";
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

    public VerifyCustomerEvent(UUID userID, UUID orderID, BigDecimal amount,
                                   EventStatus eventStatus, UUID sagaID) {
        this.payload = new Payload(orderID, userID, amount);
        this.eventStatus = eventStatus;
        this.type = "VerifyCustomer";
        this.sagaID = sagaID;
    }

}
