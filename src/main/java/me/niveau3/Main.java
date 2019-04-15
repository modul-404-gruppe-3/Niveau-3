package me.niveau3;

import me.niveau3.payment_methods.Cash;
import me.niveau3.payment_methods.CreditCard;
import me.niveau3.payment_methods.OnAccount;
import me.niveau3.services.MainService;
import me.niveau3.objects.PaymentMethodManager;

public class Main {
    public static void main(String[] args) {

        MainService mainService = new MainService();

        registerPaymentMethods(mainService);

        mainService.run();
    }

    private static void registerPaymentMethods(MainService mainService) {
        PaymentMethodManager paymentMethodManager = mainService.getPaymentMethodManager();

        paymentMethodManager.register(new Cash(mainService));
        paymentMethodManager.register(new CreditCard(mainService));
        paymentMethodManager.register(new OnAccount(mainService));
    }
}
