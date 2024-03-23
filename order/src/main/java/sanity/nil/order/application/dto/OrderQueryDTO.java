package sanity.nil.order.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import sanity.nil.order.domain.consts.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderQueryDTO (
        @JsonProperty(value = "amount") BigDecimal amount,
        @JsonProperty(value = "status") OrderStatus status,
        @JsonProperty(value = "created_at") LocalDateTime createdAt
){}