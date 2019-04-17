package me.niveau3.payment_methods;

import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.services.MainService;
import me.niveau3.api.util.PaymentUtils;
import service.api.IScanner;

import java.util.ArrayList;

/**
 * the method that allows creditcart payment.
 */
public class CreditCard extends AbstractPaymentMethod {

    public CreditCard(MainService mainService) {
        super(mainService);
    }

    @Override
    public String getDisplayName() {
        return "Kreditkarte";
    }

    @Override
    public void execute() {
        pay();
    }

    @Override
    public void pay() {
        PaymentUtils.payItems(getMainService(), new ArrayList<>(getMainService().getShoppingCartService().getCart().getItems().values()));
    }
}
