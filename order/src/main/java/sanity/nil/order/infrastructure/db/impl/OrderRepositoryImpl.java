package sanity.nil.order.infrastructure.db.impl;

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
import sanity.nil.order.application.dto.OrderQueryDTO;
import sanity.nil.order.application.interfaces.OrderRepository;
import sanity.nil.order.domain.consts.OrderStatus;
import sanity.nil.order.domain.entity.Order;
import sanity.nil.order.infrastructure.db.exceptions.OrderNotFoundException;
import sanity.nil.order.infrastructure.db.model.OrderModel;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(Order order) {
        OrderModel orderModel = new OrderModel(order.getOrderID(), order.getUserID(),
                order.getTraceID(), order.getAmount(), order.getOrderStatus(), LocalDateTime.now());
        entityManager.persist(orderModel);
    }

    @Override
    public void changeStatus(UUID orderID, UUID userID, OrderStatus orderStatus) {
        String jpql = "UPDATE OrderModel o " +
                "SET o.orderStatus = :orderStatus " +
                "WHERE o.id = :id " +
                "AND o.userID = :userId";

        entityManager.createQuery(jpql)
                .setParameter("orderStatus", orderStatus)
                .setParameter("id", orderID)
                .setParameter("userId", userID)
                .executeUpdate();
    }

    @Override
    public OrderQueryDTO getByTraceID(UUID traceID) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderModel> cq = builder.createQuery(OrderModel.class);
        Root<OrderModel> root = cq.from(OrderModel.class);
        Predicate idPredicate = builder.equal(root.get(OrderModel.FIELD_TRACE_ID), traceID);
        cq.where(builder.and(idPredicate));
        OrderModel orderModel = Optional.of(entityManager.createQuery(cq).getSingleResult()).orElseThrow(
                () -> new OrderNotFoundException(traceID)
        );
        return new OrderQueryDTO(orderModel.getAmount(), orderModel.getOrderStatus(), orderModel.getCreatedAt());
    }
}
