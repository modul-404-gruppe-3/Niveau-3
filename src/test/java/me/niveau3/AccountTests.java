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
import service.api.InvalidScannerOutputException;
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
        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0", "stop",
                "stop"));

        sut.run();

        Assertions.assertNotNull(sut.getAccountManager().getAccount("g"));
    }

    @Test
    public void test_invalid_start_capital() {
        String message = Assertions.assertThrows(InvalidScannerOutputException.class,
                () -> {
                    AbstractProgram.setCurrentScanner(new MockScanner(sut,
                            "1", "g", "123", "xyz")); //create account

                    sut.run();
                },
                "This should throw a exception because 'xyz' is not a valid double.")
                .getMessage();

        System.out.println("b: "+message);
        Assertions.assertTrue(message.startsWith("xyz"));
        Assertions.assertTrue(message.endsWith("Bitte geben sie eine Valide Zahl grösser oder gleich 0 ein!"));
    }

    @Test
    public void test_account_valid_login() {
        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0", "stop",
                "stop"));

        sut.run();

        Assertions.assertTrue(sut.getAccountManager().canLogin("g", "123"));
    }

    @Test
    public void test_account_invalid_login() {
        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0", "stop",
                "stop"));

        sut.run();

        Assertions.assertFalse(sut.getAccountManager().canLogin("g", "321"));
    }

    @Test
    public void test_account_add_money() {
        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0",
                "2","g", "123", "2", "100", "stop",
                "stop", "stop"));

        sut.run();

        System.out.println(String.join(" ", sut.getAccountManager().getAccountNames()));

        Assertions.assertEquals(1100.0, sut.getAccountManager().getAccount("g").getBalance());
    }

    @Test
    public void test_account_add_invalid_amount_of_money() {
        String message = Assertions.assertThrows(InvalidScannerOutputException.class,
                () -> {
                    AbstractProgram.setCurrentScanner(new MockScanner(sut,
                            "1", "g", "123", "1000.0",
                            "2","g", "123", "2", "-100",
                            "stop", "stop"));

                    sut.run();
                },
                "This should throw a exception because '-100' is not a valid amount.")
                .getMessage();

        Assertions.assertTrue(message.startsWith("-100.0"));
        Assertions.assertTrue(message.endsWith("Bitte geben sie eine Valide Zahl grösser als 0 ein!"));
    }

    @Test
    public void test_account_remove_money() {
        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0",
                "2","g", "123", "1", "100",
                "stop", "stop", "stop"));

        sut.run();

        Assertions.assertEquals(900.0, sut.getAccountManager().getAccount("g").getBalance());
    }

    @Test
    public void test_account_remove_invalid_amount_of_money() {
        String message = Assertions.assertThrows(InvalidScannerOutputException.class,
                () -> {
                    AbstractProgram.setCurrentScanner(new MockScanner(sut,
                            "1", "g", "123", "1000.0",
                            "2","g", "123", "1", "1100.0",
                            "stop", "stop"));

                    sut.run();
                },
                "This should throw a exception because '1100' is a to big number.")
                .getMessage();

        Assertions.assertTrue(message.startsWith("1100.0"));
        Assertions.assertTrue(message.endsWith("Bitte geben sie eine Valide Zahl grösser als 0 ein, deren Betrag sie besitzen!"));
    }

    @Test
    public void test_account_transaction() {
        AbstractProgram.setCurrentScanner(new MockScanner(sut,
                "1", "g", "123", "1000.0",
                "1", "g2", "321", "1000.0",
                "2","g", "123", "4", "g2","100", "stop",
                "stop", "stop"));

        sut.run();

        Assertions.assertEquals(900.0, sut.getAccountManager().getAccount("g").getBalance());
        Assertions.assertEquals(1100.0, sut.getAccountManager().getAccount("g2").getBalance());
    }
}
