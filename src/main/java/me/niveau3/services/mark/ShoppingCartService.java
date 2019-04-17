package me.niveau3.services.mark;

import me.niveau3.api.AbstractPaymentMethod;
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
     */
    @Override
    public void execute() {
        System.out.println("wollen sie sich anmelden?");

        if (mainService.getBankService().getLoggedInAccount() == null) {

        }

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
        System.out.println("Bitte geben sie Aktion an:");
        System.out.println("[1] Objekte entfernen.");
        System.out.println("[2] Check out (Bezahlen)");
        System.out.println("[stop] Warenkorb verlassen");

        String input = getScanner().next("Bitte gebe eine Valide Aktion ein.", "1", "2");

        if (input == null) {
            System.out.println("Warenkorb verlassen.");
            return;
        }

        if (input.equalsIgnoreCase("1")) {

            System.out.println("Bitte geben Sie die Id des Objektes an, dass sie entfernen wollen.");

            Integer id = getScanner().nextInteger("Bitte geben sie eine Id eines Items aus dem Warenkorb an.",
                    getCart().getItems().keySet().stream().map(integer -> "" + integer).toArray(String[]::new));

            var item = getCart().removeItem(id);
            System.out.println(item.getProduct().getName() + " wurde aus der Listen entfernt.");
        }else if (input.equalsIgnoreCase("2")) {
            System.out.println("Gib eine der Folgenden Zahlungsmethoden an:");
            for (String indexesAsString : mainService.getPaymentMethodManager().getIndexesAsStrings()) {
                System.out.println("[" + indexesAsString + "] " + mainService.getPaymentMethodManager().get(indexesAsString).getDisplayName());
            }

            String m = getScanner().next("Bitte geben sie eine Valide Zahlungsart an.", mainService.getPaymentMethodManager().getIndexesAsStrings().toArray(new String[0]));

            AbstractPaymentMethod method = mainService.getPaymentMethodManager().get(m);

            double ammount = mainService.getShoppingCartService().getCart().getTotalAmount();

            method.execute();


            if (!(method instanceof CollectiveBill)) {
                System.out.println("Zahlung abgeschlossen! (" + method.getDisplayName()+ ", "+ ammount +" CHF)");
            }else {
                System.out.println("Bestellung auf rechnung hinzugefügt. (" + ammount + " CHF)");
            }
        } else {
            System.out.println("invalid user input!");
        }
    }

    @Override
    public void stop() {
        mainService.getFileManager().save();
    }
}
