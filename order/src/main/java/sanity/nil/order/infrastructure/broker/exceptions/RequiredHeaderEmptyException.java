package sanity.nil.order.infrastructure.broker.exceptions;

public class RequiredHeaderEmptyException extends RuntimeException {

    public RequiredHeaderEmptyException(String headerName) {
        super(String.format("No Required Header with name = %s found in a message.", headerName));
    }
}
