package me.niveau3.payment_method;

import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.services.MainService;
import service.api.InternalScanner;

public class Cash extends AbstractPaymentMethod {
    public Cash(MainService mainService) {
        super(mainService);
    }

    @Override
    public String getDisplayName() {
        return "Bar";
    }

    @Override
    public void execute(InternalScanner scanner) {
        getMainService().getShoppingCartService().getCart().clear();
    }
}
