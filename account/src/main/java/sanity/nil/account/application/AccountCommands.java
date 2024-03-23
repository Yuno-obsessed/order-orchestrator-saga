package sanity.nil.account.application;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import sanity.nil.account.application.command.VerifyBalanceCommand;

@RequiredArgsConstructor
@AllArgsConstructor
public class AccountCommands {

    public VerifyBalanceCommand verifyBalanceCommand;
}
