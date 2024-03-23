package sanity.nil.order.infrastructure.saga;


import sanity.nil.order.infrastructure.saga.consts.SagaStatus;

import java.util.Optional;
import java.util.UUID;

public interface SagaRepository {

    void saveSaga(SagaState sagaState);

    Optional<SagaState> getSagaByID(UUID id);

    SagaStatus getStatusByID(UUID id);

    void updateSaga(SagaState sagaState);

    SagaState newSaga();
}
