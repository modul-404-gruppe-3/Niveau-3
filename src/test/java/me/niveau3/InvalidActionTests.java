package me.niveau3;

import me.niveau3.manager.PaymentMethodManager;
import me.niveau3.payment_methods.Cash;
import me.niveau3.payment_methods.CollectiveBill;
import me.niveau3.payment_methods.CreditCard;
import me.niveau3.services.MainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.api.AbstractProgram;
import service.api.MockScanner;

public class InvalidActionTests {

    private MainService sut;

    @BeforeEach
    public void beforeEach() {
        sut = new MainService(false);

        PaymentMethodManager paymentMethodManager = sut.getPaymentMethodManager();

        paymentMethodManager.register(new Cash(sut));
        paymentMethodManager.register(new CreditCard(sut));
        paymentMethodManager.register(new CollectiveBill(sut));
    }

    @Test
    public void checkout_bar() {
ää

        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "xyz", //create account
                "stop"));
        sut.run();

        Assertions.assertEquals(0, sut.getShoppingCartService().getCart().getTotalAmount());
    }

}
