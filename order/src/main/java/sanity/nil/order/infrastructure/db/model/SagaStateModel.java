package sanity.nil.order.infrastructure.db.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonNodeBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import sanity.nil.order.infrastructure.saga.SagaState;
import sanity.nil.order.infrastructure.saga.consts.SagaStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "saga_state", schema = "public")
@Getter
@Setter
public class SagaStateModel implements SagaState {

    public static final String FIELD_ID = "id";
    public static final String FIELD_STATUS = "saga_status";
    public static final String FIELD_PAYLOAD = "payload";
    public static final String FIELD_CURRENT_STEP = "current_step";

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "saga_type")
    private String type;

    @Type(JsonNodeBinaryType.class)
    @Column(name = "payload", columnDefinition = "jsonb")
    private JsonNode payload;

    @Type(JsonNodeBinaryType.class)
    @Column(name = "current_step", columnDefinition = "jsonb")
    private JsonNode currentStep;

    @Enumerated(EnumType.STRING)
    @Column(name = "saga_status")
    private SagaStatus sagaStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onInsert() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public SagaStateModel(UUID id, JsonNode payload,
                          JsonNode currentStep, SagaStatus sagaStatus) {
        this.id = id;
        this.payload = payload;
        this.currentStep = currentStep;
        this.sagaStatus = sagaStatus;
    }

    public SagaStateModel() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public void setID(UUID id) {
        this.id = id;
    }

    public SagaStatus getStatus() {
        return sagaStatus;
    }

    @Override
    public void setStatus(SagaStatus status) {
        this.sagaStatus = status;
    }
}
