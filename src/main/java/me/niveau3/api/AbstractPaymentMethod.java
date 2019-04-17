package me.niveau3.api;

import lombok.Getter;
import me.niveau3.services.MainService;
import service.api.IScanner;

/**
 * This class is used so you can dynamically add payment methods without hardcodign them.
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

    /**
     * this method will be executed when checking out.
     */
    public abstract void execute();

    /**
     * With this method you can initiate the payment.
     */
    public abstract void pay();
}
