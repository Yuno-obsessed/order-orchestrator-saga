package sanity.nil.order.infrastructure.saga;

import java.util.UUID;

public class SagaNotFoundException extends RuntimeException{

    public SagaNotFoundException(UUID id) {
        super(String.format("Saga with id = %s was not found.", id));
    }
}
