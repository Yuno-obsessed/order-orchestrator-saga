package sanity.nil.order.infrastructure.db.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonNodeBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox",schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OutboxModel {

    public static final String FIELD_PROCESSED = "processed";

    @Id
    @Column(name = "id")
    private UUID eventID;

    @Column(name = "entity_id")
    private UUID entityID;

    @Column(name = "saga_id")
    private String sagaID;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "type")
    private String type;

    @Type(value = JsonNodeBinaryType.class)
    @Column(name = "payload", columnDefinition = "jsonb")
    private JsonNode payload;

//    @Column(name = "processed")
    private transient boolean processed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
