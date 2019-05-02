package me.niveau3.services.mark;

import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.api.objects.Account;
import me.niveau3.api.objects.ShoppingCart;
import me.niveau3.payment_methods.CollectiveBill;
import me.niveau3.services.MainService;
import service.api.AbstractProgram;
import service.api.IStopable;

/**
 * this service lets you manage the shopping cart.
 */
public class ShoppingCartService extends AbstractProgram implements IStopable {
    private MainService mainService;
    private static ShoppingCart noLoginCart;

    /**
     * @return checks if user is logged in and gets the right shopping cart.
     */
    public ShoppingCart getCart() {
        return  (mainService.getBankService().getLoggedInAccount() == null)
                ? noLoginCart
                : mainService.getBankService().getLoggedInAccount().getCart();
    }

    public ShoppingCartService(MainService mainService) {
        this.mainService = mainService;
        noLoginCart = new ShoppingCart();
    }

    /**
     * this method will let you chose between removing products from the shopping cart and checking out.
     * You Only can execute this service when there is a shopping cart with at least one item.
     */
    @Override
    public void execute() {
        //<editor-fold desc="verify for a valid account">
        if (getCart().getItems().values().size() <= 0) {
            System.out.println("es sind aktuell keine Items in ihrem Warenkorb.");
        }
        if (mainService.getBankService().getLoggedInAccount() == null) {
            System.out.println("Sie sind Aktuell nicht angemeldet und haben keine Items in ihrem Warenkorb.");
            System.out.println("Sie können sich nun Anmelden, um ihrem persöndlichen Warenkorb zu sehen, " +
                    "oder 'stop' eingeben um zum hauptmenü zurück zu kommen.");
        }

        while (getCart().getItems().values().size() <= 0) {
            Account account = mainService.getBankService().login();
            if (account == null) {
                System.out.println("Warenkorb öffnen abgebrochen.");
                return;
            }

            if (getCart().getItems().values().size() <= 0) {
                System.out.println("Dieser Account hat keine Items in seinem Warenkorb.");
            }
        }
        //</editor-fold>

        //<editor-fold desc="warenkorb Items auflisten.">
        System.out.println("==================");
        System.out.println("     Warenkorb");
        System.out.println("==================");

        for (ShoppingCart.ShoppingCartItem item : getCart().getItems().values()) {

            System.out.println(String.format("[%s] %s (%s CHF) * %s (total:%s)",
                    item.getId(),
                    item.getProduct().getName(),
                    item.getProduct().getPrice(),
                    item.getAmount(),
                    item.getProduct().getPrice()*item.getAmount()));
        }

        System.out.println("==================");
        //</editor-fold>

        //<editor-fold desc="nächste Aktion auswählen">
        System.out.println("Bitte geben sie Aktion an:");
        System.out.println("[1] Objekte entfernen.");
        System.out.println("[2] Check out (Bezahlen)");
        System.out.println("[stop] Warenkorb verlassen");

        String input = getScanner().next("Bitte gebe eine Valide Aktion ein.", "1", "2");

        if (input == null) {
            System.out.println("Warenkorb verlassen.");
            return;
        }
        //</editor-fold>

        if (input.equalsIgnoreCase("1")) {
            //<editor-fold desc="objekt aus warenkorb entfernen">
            System.out.println("Bitte geben Sie die Id des Objektes an, dass sie entfernen wollen.");

            Integer id = getScanner().nextInteger("Bitte geben sie eine Id eines Items aus dem Warenkorb an.",
                    getCart().getItems().keySet().stream().map(integer -> "" + integer).toArray(String[]::new));

            var item = getCart().removeItem(id);
            System.out.println(item.getProduct().getName() + " wurde aus der Listen entfernt.");
            //</editor-fold>
        }else if (input.equalsIgnoreCase("2")) {
            //<editor-fold desc="Checkout">
            System.out.println("Gib eine der Folgenden Zahlungsmethoden an:");
            for (String indexesAsString : mainService.getPaymentMethodManager().getIndexesAsStrings()) {
                System.out.println("[" + indexesAsString + "] " + mainService.getPaymentMethodManager().get(indexesAsString).getDisplayName());
            }

            String paymentMethod = getScanner().next("Bitte geben sie eine Valide Zahlungsart an.", mainService.getPaymentMethodManager().getIndexesAsStrings().toArray(new String[0]));

            AbstractPaymentMethod method = mainService.getPaymentMethodManager().get(paymentMethod);

            double amount = mainService.getShoppingCartService().getCart().getTotalAmount();

            if (!method.execute()) {
                return;
            }


            if (!(method instanceof CollectiveBill)) {
                System.out.println("Zahlung abgeschlossen! (" + method.getDisplayName()+ ", "+ amount +" CHF)");
            }else {
                System.out.println("Bestellung zur Rechnung hinzugefügt. (" + amount + " CHF)");
                System.out.println("Items wurden aus ihrem Warenkorb entfernt.");
                System.out.println("====================================");
            }
            //</editor-fold>
        } else {
            System.out.println("invalid user input!");
        }
        mainService.getFileManager().saveAccountManager();
    }
}
