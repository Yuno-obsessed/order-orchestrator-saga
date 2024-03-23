package sanity.nil.account.infrastructure.db.impl.mappers;

import sanity.nil.account.domain.entity.Account;
import sanity.nil.account.domain.entity.Balance;
import sanity.nil.account.infrastructure.db.model.AccountModel;

public class AccountMapper {

    public static Account convertModelToEntity(AccountModel model) {
        return new Account(model.getId(), model.getUsername(), new Balance(model.getBalance()), model.isActive());
    }

    public static AccountModel convertEntityToModel(Account account) {
        return new AccountModel(account.getId(), account.getUsername(), account.getBalance().getAmount(), account.isActive());
    }
}
