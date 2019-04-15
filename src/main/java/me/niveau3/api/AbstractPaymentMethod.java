package me.niveau3.api;

import lombok.Getter;
import me.niveau3.objects.ShoppingCart;
import me.niveau3.services.MainService;
import service.api.IScanner;

@Getter
public abstract class AbstractPaymentMethod {

    private ShoppingCart cart;
    private MainService mainService;

    public AbstractPaymentMethod(MainService mainService) {
        this.mainService = mainService;
        this.cart = mainService.getShoppingCartService().getCart();
    }

    public abstract String getDisplayName();
    public abstract void execute(IScanner scanner);
}
