package me.niveau3;

import me.niveau3.objects.ShoppingCart;
import me.niveau3.payment_method.Cash;
import me.niveau3.payment_method.CreditCard;
import me.niveau3.payment_method.OnAccount;
import me.niveau3.services.MainService;
import me.niveau3.objects.PaymentMethodManager;

public class Main {
    public static void main(String[] args) {

        MainService mainService = new MainService();

        registerPaymentMethods(mainService);

        while (!mainService.isStop()) {
            mainService.execute();
        }
    }

    private static void registerPaymentMethods(MainService mainService) {
        ShoppingCart cart = mainService.getShoppingCartService().getCart();
        PaymentMethodManager paymentMethodManager = mainService.getPaymentMethodManager();

        paymentMethodManager.register(new Cash(cart));
        paymentMethodManager.register(new CreditCard(cart));
        paymentMethodManager.register(new OnAccount(cart));
    }
}
