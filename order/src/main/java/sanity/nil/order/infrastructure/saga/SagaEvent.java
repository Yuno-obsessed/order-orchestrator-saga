package sanity.nil.order.infrastructure.saga;

import java.util.UUID;

public interface SagaEvent {

    UUID getSagaID();
    void setSagaID(UUID sagaID);
}
