package me.niveau3.payment_method;

import lombok.Getter;
import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.objects.Account;
import me.niveau3.objects.ShoppingCart;
import me.niveau3.services.MainService;
import me.niveau3.util.Hasher;
import service.api.InternalScanner;

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
    public void execute(InternalScanner scanner) {
        items.addAll(getCart().getItems().values());
        getCart().clear();
    }

    public void pay(InternalScanner scanner) {
        final double amount = items
                .stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getAmount())
                .sum();


        PaymentUtils.payItems(getMainService(), scanner, amount);
    }


}
