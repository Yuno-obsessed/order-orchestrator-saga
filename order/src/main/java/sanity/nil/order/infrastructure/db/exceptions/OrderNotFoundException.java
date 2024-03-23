package sanity.nil.order.infrastructure.db.exceptions;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(UUID traceID) {
        super(String.format("Order with traceID = %s not found.", traceID));
    }
}
