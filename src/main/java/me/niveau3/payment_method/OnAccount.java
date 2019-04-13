package me.niveau3.payment_method;

import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.objects.ShoppingCart;

public class OnAccount extends AbstractPaymentMethod {
    public OnAccount(ShoppingCart cart) {
        super(cart);
    }

    @Override
    public String getDisplayName() {
        return "Auf Rechnung";
    }

    @Override
    public void execute() {

    }
}
