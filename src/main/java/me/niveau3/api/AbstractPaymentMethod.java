package me.niveau3.api;

import lombok.Getter;
import me.niveau3.services.MainService;
import service.api.IScanner;

/**
 *
 */
@Getter
public abstract class AbstractPaymentMethod {

    private MainService mainService;

    public AbstractPaymentMethod(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * the name of the payment method
     */
    public abstract String getDisplayName();


    public abstract void execute();

    public abstract void pay();
}
