package sanity.nil.account.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class Balance {

    private BigDecimal amount;

    public boolean isGreater(Balance balance) {
        return this.getAmount().compareTo(balance.getAmount()) > 0;
    }

    public void decreaseBalance(BigDecimal decreasedAmount) {
        this.amount = this.amount.subtract(decreasedAmount);
    }
}
