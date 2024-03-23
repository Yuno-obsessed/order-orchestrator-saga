package sanity.nil.account.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;
import sanity.nil.account.domain.consts.EventStatus;

import java.util.UUID;

@ToString
public class CustomerBalanceFailedEvent implements Event {

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
        return "account";
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

    public CustomerBalanceFailedEvent(UUID userID, UUID orderID,
                                        EventStatus eventStatus, UUID sagaID) {
        this.payload = new Payload(orderID, userID);
        this.eventStatus = eventStatus;
        this.type = "CustomerBalanceFailed";
        this.sagaID = sagaID;
    }

}
