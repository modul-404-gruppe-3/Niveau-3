package me.niveau3;

import me.niveau3.objects.PaymentMethodManager;
import me.niveau3.objects.ShoppingCart;
import me.niveau3.payment_methods.Cash;
import me.niveau3.payment_methods.CreditCard;
import me.niveau3.payment_methods.OnAccount;
import me.niveau3.services.MainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.api.AbstractProgram;
import service.api.MockScanner;

public class MarkTests {

    private MainService sut;

    @BeforeEach
    public void beforeEach() {
        sut = new MainService();

        PaymentMethodManager paymentMethodManager = sut.getPaymentMethodManager();

        paymentMethodManager.register(new Cash(sut));
        paymentMethodManager.register(new CreditCard(sut));
        paymentMethodManager.register(new OnAccount(sut));
    }
    @Test
    public void warenkorb_produkte_hinzufügen() {

        AbstractProgram.scanner = new MockScanner(sut,
                "2", "1", "1", "4", "3",
                "2", "1", "1", "6", "1",
                "2", "1", "1", "1", "5",
                "stop");

        sut.run();

        Assertions.assertEquals(91, sut.getShoppingCartService().getCart().getTotalAmount());
    }
}
