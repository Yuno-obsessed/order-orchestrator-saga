package sanity.nil.account.infrastructure.db.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.transaction.annotation.Transactional;
import sanity.nil.account.application.dto.EventMessage;
import sanity.nil.account.application.interfaces.OutboxRepository;
import sanity.nil.account.domain.event.Event;
import sanity.nil.account.infrastructure.db.model.OutboxModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class OutboxRepositoryImpl implements OutboxRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    @Override
    public void saveMessage(EventMessage event) {
        JsonNode payload = objectMapper.valueToTree(event.getPayload());
        OutboxModel outboxModel = new OutboxModel(UUID.randomUUID(), event.getEntityID(), event.getSagaID().toString(),
                event.getEntityType(), event.getType(), payload, false, LocalDateTime.now());
        entityManager.persist(outboxModel);
    }

    @Override
    public void saveMessages(Collection<Event> events) {
        for (Event event : events) {
            JsonNode payload = objectMapper.valueToTree(event.getPayload());
            OutboxModel outboxModel = new OutboxModel(UUID.randomUUID(), event.getEntityID(), null,
                    event.getEntityType(), event.getType(), payload, false, LocalDateTime.now());
            entityManager.persist(outboxModel);
        }
    }

    @Override
    public Collection<Event> getNotProcessed() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OutboxModel> cq = builder.createQuery(OutboxModel.class);
        Root<OutboxModel> root = cq.from(OutboxModel.class);
        Predicate nonProcessedPredicate = builder.equal(root.get(OutboxModel.FIELD_PROCESSED), false);
        cq.where(builder.and(nonProcessedPredicate));
        List<OutboxModel> outboxModels = entityManager.createQuery(cq).getResultList();
        return null;
    }
}
