package sanity.nil.order.application.interfaces;

import sanity.nil.order.application.dto.OrderQueryDTO;
import sanity.nil.order.domain.consts.OrderStatus;
import sanity.nil.order.domain.entity.Order;

import java.util.UUID;

public interface OrderRepository {

    void save(Order order);

    void changeStatus(UUID orderID, UUID userID, OrderStatus orderStatus);

    OrderQueryDTO getByTraceID(UUID traceID);
}
