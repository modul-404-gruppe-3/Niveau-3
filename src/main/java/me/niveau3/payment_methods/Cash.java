package me.niveau3.payment_methods;

import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.services.MainService;
import service.api.IScanner;

/**
 * the simplest payment method. It will only clear the shopping cart object.
 */
public class Cash extends AbstractPaymentMethod {
    public Cash(MainService mainService) {
        super(mainService);
    }

    @Override
    public String getDisplayName() {
        return "Bar";
    }

    @Override
    public boolean execute() {
        return pay();
    }

    @Override
    public boolean pay() {
        getMainService().getShoppingCartService().getCart().clear();
        return true;
    }
}
