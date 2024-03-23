package sanity.nil.order.infrastructure.db.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;
import sanity.nil.order.infrastructure.db.model.SagaStateModel;
import sanity.nil.order.infrastructure.saga.SagaRepository;
import sanity.nil.order.infrastructure.saga.SagaState;
import sanity.nil.order.infrastructure.saga.consts.SagaStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class SagaRepositoryImpl implements SagaRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void saveSaga(SagaState sagaState) {
        entityManager.persist(sagaState);
    }

    @Override
    public void updateSaga(SagaState sagaState) {
        String jpql = "UPDATE SagaStateModel s " +
                "SET s.currentStep = :currentStep, " +
                "    s.payload = :payload, " +
                "    s.sagaStatus = :sagaStatus, " +
                "    s.updatedAt = :updatedAt " +
                " WHERE s.id = :id";

        entityManager.createQuery(jpql)
                .setParameter("currentStep", sagaState.getCurrentStep())
                .setParameter("payload", sagaState.getPayload())
                .setParameter("sagaStatus", sagaState.getStatus())
                .setParameter("id", sagaState.getID())
                .setParameter("updatedAt", LocalDateTime.now())
                .executeUpdate();
    }

    @Override
    public Optional<SagaState> getSagaByID(UUID id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SagaStateModel> cq = builder.createQuery(SagaStateModel.class);
        Root<SagaStateModel> root = cq.from(SagaStateModel.class);
        Predicate idPredicate = builder.equal(root.get(SagaStateModel.FIELD_ID), id);
        cq.where(builder.and(idPredicate));
        return Optional.of(entityManager.createQuery(cq).getSingleResult());
    }

    @Override
    public SagaStatus getStatusByID(UUID id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = builder.createTupleQuery();
        Root<SagaStateModel> root = cq.from(SagaStateModel.class);
        Predicate idPredicate = builder.equal(root.get(SagaStateModel.FIELD_ID), id);
        cq.select(root.get(SagaStateModel.FIELD_STATUS)).where(builder.and(idPredicate));
        Tuple tupleResult = entityManager.createQuery(cq).getSingleResult();
        return (SagaStatus) tupleResult.get(0);
    }

    @Override
    public SagaState newSaga() {
        return new SagaStateModel();
    }
}
