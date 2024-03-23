package sanity.nil.order.infrastructure.saga;

import com.fasterxml.jackson.databind.JsonNode;
import sanity.nil.order.infrastructure.saga.consts.SagaStatus;

import java.util.UUID;

public interface SagaState {
    UUID getID();
    JsonNode getPayload();
    JsonNode getCurrentStep();
    SagaStatus getStatus();
    String getType();
    void setID(UUID id);
    void setPayload(JsonNode payload);
    void setCurrentStep(JsonNode currentStep);
    void setStatus(SagaStatus status);
    void setType(String type);
}
