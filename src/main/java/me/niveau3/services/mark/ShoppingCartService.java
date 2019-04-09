package me.niveau3.services.mark;

import lombok.Getter;
import me.niveau3.objects.Product;
import me.niveau3.objects.ShoppingCart;
import service.api.IProgram;
import service.api.IStopable;

public class ShoppingCartService implements IStopable, IProgram {

    @Getter
    private ShoppingCart cart;

    public ShoppingCartService() {
        cart = new ShoppingCart();
        cart.addItem(5, new Product(1, 10, "baum"));
    }

    @Override
    public void execute() {
        System.out.println("==================");
        System.out.println("     Warenkorb");
        System.out.println("==================");

        for (ShoppingCart.ShoppingCartItem item : cart.getItems().values()) {

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

        String input = getScanner().next("Bitte gebe eine Valide Aktion ein.", "1");

        if (input == null) {
            System.out.println("Warenkorb verlassen.");
            return;
        }

        if (input.equalsIgnoreCase("1")) {

            System.out.println("Bitte geben Sie die Id des Objektes an, dass sie entfernen wollen.");

            Integer id = getScanner().nextInteger("Bitte geben sie eine Id eines Items aus dem Warenkorb an.",
                    cart.getItems().keySet().stream().map(integer -> "" + integer).toArray(String[]::new));

            var item = cart.removeItem(id);
            System.out.println(item.getProduct().getName() + " wurde aus der Listen entfernt.");
        } else {
            System.out.println("invalid user input!");
        }
    }
}
