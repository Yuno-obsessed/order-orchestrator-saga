package sanity.nil.order.application.dto;

import java.util.UUID;

public record CreateOrderCommandDTO(
        UUID orderID,
        UUID userID
) {
}
