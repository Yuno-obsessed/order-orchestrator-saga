package sanity.nil.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sanity.nil.order.domain.consts.EventStatus;
import sanity.nil.order.domain.consts.OrderStatus;
import sanity.nil.order.domain.event.OrderCreateStartedEvent;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Order extends EntityBase {

    private UUID orderID;
    private UUID userID;
    private UUID traceID;
    private OrderStatus orderStatus;
    private BigDecimal amount;

    public Order(UUID userID, BigDecimal amount) {
        this.orderID = UUID.randomUUID();
        this.userID = userID;
        this.traceID = UUID.randomUUID();
        this.orderStatus = OrderStatus.CREATE_PENDING;
        this.amount = amount;
        this.addEvent(new OrderCreateStartedEvent(orderID, userID, traceID, amount, EventStatus.AWAITING));
    }

}
