package sanity.nil.account.infrastructure.broker.impl;

public class RequiredHeaderEmptyException extends RuntimeException {

    public RequiredHeaderEmptyException(String headerName) {
        super(String.format("No Required Header with name = %s found in a message.", headerName));
    }
}
