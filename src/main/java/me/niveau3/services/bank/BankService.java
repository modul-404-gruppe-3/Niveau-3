package me.niveau3.services.bank;

import lombok.Getter;
import me.niveau3.services.MainService;
import service.api.AbstractProgram;
import service.api.IStopable;

@Getter
public class BankService extends AbstractProgram implements IStopable {


    private MainService mainService;

    public BankService(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    public void execute() {
        mainService.getAccountService().execute();
    }
}
