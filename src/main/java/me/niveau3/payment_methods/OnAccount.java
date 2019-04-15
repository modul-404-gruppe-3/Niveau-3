package me.niveau3.payment_methods;

import lombok.Getter;
import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.objects.ShoppingCart;
import me.niveau3.services.MainService;
import service.api.IScanner;

import java.util.ArrayList;
import java.util.List;

public class OnAccount extends AbstractPaymentMethod {
    @Getter
    private static OnAccount account;

    public OnAccount(MainService mainService) {
        super(mainService);
        items = new ArrayList<>();
        account = this;
    }

    public List<ShoppingCart.ShoppingCartItem> items;

    @Override
    public String getDisplayName() {
        return "Auf Rechnung";
    }

    @Override
    public void execute(IScanner scanner) {
        items.addAll(getCart().getItems().values());
        getCart().clear();
    }

    public void pay(IScanner scanner) {
        final double amount = items
                .stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getAmount())
                .sum();


        PaymentUtils.payItems(getMainService(), scanner, amount);
    }


}
