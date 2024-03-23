package sanity.nil.account.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Account {

    private UUID id;
    private String username;
    private Balance balance;
    private boolean active;

    public boolean verifyHasBalance(Balance balance) {
        if (active && this.balance.isGreater(balance)) {
            this.balance.decreaseBalance(balance.getAmount());
            return true;
        } else {
            return false;
        }
    }
}
