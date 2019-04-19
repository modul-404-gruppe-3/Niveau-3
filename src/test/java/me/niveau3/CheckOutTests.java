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

public class CheckOutTests {

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
    public void checkout_with_no_items_in_basket() {
        String message = Assertions.assertThrows(InvalidScannerOutputException.class,
                () -> {
                    AbstractProgram.setStaticScanner(new MockScanner(sut,
                            "2", "2", "2",
                            "stop"));
                    sut.run();
                },
                "This should throw a exception because '2' is not a valid account and " +
                        "there is a account needed to show the basket of a account with items.")
                .getMessage();

        Assertions.assertTrue(message.startsWith("2"));
        Assertions.assertTrue(message.endsWith("Bitte geben einen Account an der existiert und genug geld auf dem Konto hat."));
    }

    @Test
    public void checkout_bar() {
        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "2", "1", "4", "3",
                "2", "2", "2", "1",
                "stop"));
        sut.run();

        Assertions.assertEquals(0, sut.getShoppingCartService().getCart().getTotalAmount());
    }

    @Test
    public void checkout_kreditkarte() {

        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "1000", //create account
                "2", "1", "4", "3", //add product
                "2", "2", "2", "2", //Zahlung mit Karte auswählen
                "g", "123", //Benutzerdaten für verifizierung eingeben.
                "stop"));
        sut.run();

        Assertions.assertEquals(0, sut.getShoppingCartService().getCart().getTotalAmount());
        Assertions.assertEquals(964, sut.getAccountManager().getAccount("g").getBalance());
    }

    @Test
    public void checkout_auf_sammelrechnung_ohne_account() {

        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "2", "1", "4", "3", //add product
                "2", "2", "2", "3", //Zahlung auf rechnung veranlassen
                "stop"));
        sut.run();

        Assertions.assertEquals(0, sut.getShoppingCartService().getCart().getItems().values().size());

        Assertions.assertEquals(1, CollectiveBill.getNoLoginBill().getItems().size());

        Assertions.assertEquals(3, CollectiveBill.getNoLoginBill().getItems().get(0).getAmount());
        Assertions.assertEquals(4, CollectiveBill.getNoLoginBill().getItems().get(0).getProduct().getId());
    }

    @Test
    public void checkout_auf_sammelrechnung_mit_account() {

        AbstractProgram.setStaticScanner(new MockScanner(sut,
                "1", "g", "123", "1000", //create account
                "1", "2", "g", "123", "stop", //anmelden
                "2", "1", "4", "3", //add product
                "2", "2", "2", "3", //Zahlung auf rechnung veranlassen
                "stop"));
        sut.run();

        Assertions.assertEquals(0, sut.getAccountManager().getAccount("g").getCart().getItems().values().size());

        Assertions.assertEquals(1, sut.getAccountManager().getAccount("g").getBill().getItems().size());

        Assertions.assertEquals(3, sut.getAccountManager().getAccount("g").getBill().getItems().get(0).getAmount());
        Assertions.assertEquals(4, sut.getAccountManager().getAccount("g").getBill().getItems().get(0).getProduct().getId());
    }
}
