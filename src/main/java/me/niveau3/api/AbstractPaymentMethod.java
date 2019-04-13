package me.niveau3.api;

import lombok.Getter;
import me.niveau3.objects.ShoppingCart;

public abstract class AbstractPaymentMethod {

    @Getter
    private ShoppingCart cart;

    public AbstractPaymentMethod(ShoppingCart cart) {
        this.cart = cart;
    }

    public abstract String getDisplayName();
    public abstract void execute();
}
