package sanity.nil.account.infrastructure.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(UUID id) {
        super(String.format("Account with id = %s not found.", id));
    }
}
