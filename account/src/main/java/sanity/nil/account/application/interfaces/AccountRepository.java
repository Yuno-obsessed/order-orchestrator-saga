package sanity.nil.account.application.interfaces;

import sanity.nil.account.domain.entity.Account;

import java.util.UUID;

public interface AccountRepository {

    Account getByID(UUID accountID);

    void updateBalance(Account account);
}
