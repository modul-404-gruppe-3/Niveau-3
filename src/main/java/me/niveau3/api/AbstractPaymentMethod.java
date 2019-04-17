package me.niveau3.api;

import lombok.Getter;
import me.niveau3.objects.ShoppingCart;
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

    /**
     * this method will be executed when ever you need to pay something with the given payment method.
     * @param scanner
     */
    public abstract void execute(IScanner scanner);
}
