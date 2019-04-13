package me.niveau3.payment_method;

import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.objects.ShoppingCart;

public class CreditCard extends AbstractPaymentMethod {

    public CreditCard(ShoppingCart cart) {
        super(cart);
    }

    @Override
    public String getDisplayName() {
        return "Kreditkarte";
    }

    @Override
    public void execute() {

    }
}
