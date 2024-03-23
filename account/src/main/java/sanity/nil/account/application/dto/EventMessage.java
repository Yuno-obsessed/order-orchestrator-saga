package sanity.nil.account.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sanity.nil.account.domain.event.Event;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class EventMessage {

    @JsonProperty(value = "event_id")
    private UUID eventID;

    @JsonProperty(value = "entity_id")
    private UUID entityID;

    @JsonProperty(value = "saga_id")
    private UUID sagaID;

    @JsonProperty(value = "entity_type")
    private String entityType;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "payload")
    private JsonNode payload;

    public EventMessage(ObjectMapper objectMapper, Event event, UUID sagaID) {
        try {
            Object payload = event.getPayload();
            this.eventID = UUID.randomUUID();
            this.entityID = event.getEntityID();
            this.sagaID = sagaID;
            this.entityType = event.getEntityType();
            this.type = event.getType();
            this.payload = objectMapper.valueToTree(payload);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
