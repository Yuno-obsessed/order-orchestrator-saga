package sanity.nil.order.infrastructure.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sanity.nil.order.domain.consts.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders", schema = "public")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {

    public static final String FIELD_STATUS = "order_status";
    public static final String FIELD_ORDER_ID = "order_id";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_TRACE_ID = "traceID";

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userID;

    @Column(name = "trace_id")
    private UUID traceID;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
