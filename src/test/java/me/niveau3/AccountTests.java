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

public class AccountTests {

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
    public void test_account_creation() {
        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0", "stop"));

        sut.run();

        Assertions.assertNotNull(sut.getAccountManager().getAccount("g"));
    }

    @Test
    public void test_account_valid_login() {
        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0", "stop"));

        sut.run();

        Assertions.assertTrue(sut.getAccountManager().canLogin("g", "123"));
    }

    @Test
    public void test_account_invalid_login() {
        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0", "stop"));

        sut.run();

        Assertions.assertFalse(sut.getAccountManager().canLogin("g", "321"));
    }

    @Test
    public void test_account_add_money() {
        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0",
                "1", "2","g", "123", "2", "100",
                "stop"));

        sut.run();

        Assertions.assertEquals(1100.0, sut.getAccountManager().getAccount("g").getBalance());
    }

    @Test
    public void test_account_remove_money() {
        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0",
                "1", "2","g", "123", "1", "100",
                "stop"));

        sut.run();

        Assertions.assertEquals(900.0, sut.getAccountManager().getAccount("g").getBalance());
    }

    @Test
    public void test_account_transaction() {
        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0",
                "1", "1", "g2", "321", "1000.0",
                "1", "2","g", "123", "4", "g2","100",
                "stop"));

        sut.run();

        Assertions.assertEquals(900.0, sut.getAccountManager().getAccount("g").getBalance());
        Assertions.assertEquals(1100.0, sut.getAccountManager().getAccount("g2").getBalance());
    }
}
