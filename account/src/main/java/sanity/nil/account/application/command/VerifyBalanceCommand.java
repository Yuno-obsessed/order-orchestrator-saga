package sanity.nil.account.application.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import sanity.nil.account.application.dto.EventMessage;
import sanity.nil.account.application.dto.VerifyBalanceCommandDTO;
import sanity.nil.account.application.interfaces.AccountRepository;
import sanity.nil.account.application.interfaces.OutboxRepository;
import sanity.nil.account.domain.consts.EventStatus;
import sanity.nil.account.domain.entity.Account;
import sanity.nil.account.domain.entity.Balance;
import sanity.nil.account.domain.event.CustomerBalanceFailedEvent;
import sanity.nil.account.domain.event.CustomerBalanceVerifiedEvent;
import sanity.nil.account.domain.event.Event;
import sanity.nil.account.infrastructure.broker.dto.VerifyCustomerEvent;

@RequiredArgsConstructor
public class VerifyBalanceCommand {

    private final OutboxRepository outboxRepository;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;

    public void handle(VerifyBalanceCommandDTO dto) {
        VerifyCustomerEvent.Payload verifyCustomerEventPayload = dto.verifyCustomerEventPayload();
        Account account = accountRepository.getByID(verifyCustomerEventPayload.userID());
        boolean verified = account.verifyHasBalance(new Balance(verifyCustomerEventPayload.amount()));

        Event event;
        if (verified) {
            event = new CustomerBalanceVerifiedEvent(account.getId(), verifyCustomerEventPayload.orderID(), EventStatus.AWAITING, dto.sagaID());
            accountRepository.updateBalance(account);
        } else {
            event = new CustomerBalanceFailedEvent(account.getId(), verifyCustomerEventPayload.orderID(), EventStatus.AWAITING, dto.sagaID());
        }

        outboxRepository.saveMessage(new EventMessage(objectMapper, event, dto.sagaID()));
    }
}
