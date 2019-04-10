package me.niveau3.services.bank;

import service.api.IProgram;
import service.api.IStopable;

public class BankService implements IProgram, IStopable {

    AccountManager accountManager;
    AccountService accountService;

    public BankService() {
        accountManager = new AccountManager();
        accountService = new AccountService(accountManager);
    }

    @Override
    public void execute() {
        accountService.execute();
    }
}
