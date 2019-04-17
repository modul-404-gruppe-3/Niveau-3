package me.niveau3;

import me.niveau3.manager.PaymentMethodManager;
import me.niveau3.payment_methods.Cash;
import me.niveau3.payment_methods.CreditCard;
import me.niveau3.payment_methods.CollectiveBill;
import me.niveau3.services.MainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.api.AbstractProgram;
import service.api.MockScanner;

public class CheckOutTests {

    private MainService sut;

    @BeforeEach
    public void beforeEach() {
        sut = new MainService();

        PaymentMethodManager paymentMethodManager = sut.getPaymentMethodManager();

        paymentMethodManager.register(new Cash(sut));
        paymentMethodManager.register(new CreditCard(sut));
        paymentMethodManager.register(new CollectiveBill(sut));
    }

    @Test
    public void checkout_bar() {

        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "2", "1", "1", "4", "3",
                "2", "2", "2", "1",
                "stop"));

        sut.run();

        Assertions.assertEquals(91, sut.getShoppingCartService().getCart().getTotalAmount());
    }
}
