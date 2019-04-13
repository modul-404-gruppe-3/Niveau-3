package me.niveau3.payment_method;

import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.objects.ShoppingCart;

public class Cash extends AbstractPaymentMethod {
    public Cash(ShoppingCart cart) {
        super(cart);
    }

    @Override
    public String getDisplayName() {
        return "Bar";
    }

    @Override
    public void execute() {
        this.getCart().clear();
    }
}
