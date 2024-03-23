package sanity.nil.order.application.dto;

import java.util.UUID;

public record RejectOrderCommandDTO(
        UUID orderID,
        UUID userID
) { }
