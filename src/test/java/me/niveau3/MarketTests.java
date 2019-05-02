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

public class MarketTests {

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
    public void warenkorb_produkte_hinzufügen_ohne_account() {

        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "2", "1", "4", "3", "stop", //produkt hinzufügen
                "stop", "stop"));

        sut.run();

        Assertions.assertEquals(36, sut.getShoppingCartService().getCart().getTotalAmount());
    }

    @Test
    public void warenkorb_produkte_hinzufügen_mit_account() {

        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "1", "g", "123", "1000", //create account
                "2", "g", "123", "stop", "stop", //anmelden
                "2", "1", "4", "3",  "stop",//produkt hinzufügen
                "stop", "stop"));

        sut.run();

        Assertions.assertEquals(36, sut.getAccountManager().getAccount("g").getCart().getTotalAmount());
    }

    @Test
    public void warenkorb_produkte_entfernen_ohne_account() {

        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "2", "1", "4", "3", //add to cart
                "6", "1", //add to cart
                "1", "5", "stop", //add to cart
                "2", "1", "0", //remove from cart
                "stop", "stop", "stop"));

        sut.run();

        Assertions.assertEquals(55, sut.getShoppingCartService().getCart().getTotalAmount());
    }

    @Test
    public void warenkorb_produkte_entfernen_mit_account() {

        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "1", "g", "123", "1000", //create account
                "2", "g", "123", "stop","stop", //anmelden
                "2", "1", "4", "3", //add to cart
                "6", "1", //add to cart
                "1", "5","stop", //add to cart
                "2", "1", "0", //remove from cart
                "stop", "stop", "stop"));

        sut.run();

        Assertions.assertEquals(55, sut.getAccountManager().getAccount("g").getCart().getTotalAmount());
    }
}
