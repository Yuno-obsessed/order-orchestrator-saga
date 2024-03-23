package sanity.nil.order.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record StartCreateOrderCommandDTO(
        UUID userID,
        BigDecimal amount
) {}
