package me.niveau3.services.mark;

import lombok.Getter;
import me.niveau3.api.objects.Product;
import me.niveau3.services.MainService;
import service.api.AbstractProgram;
import service.api.IStopable;

import java.util.HashMap;

/**
 * this service makes it possible to add products to the shopping cart.
 */
public class ProductService extends AbstractProgram implements IStopable {

    @Getter
    private MainService mainService;
    private HashMap<Integer, Product> products;


    public ProductService(MainService mainService) {
        this.mainService = mainService;
        loadProducts();
    }

    @Override
    public void execute() {
        for (Product product : products.values()) {
            System.out.println("[" + product.getId()+ "] " + product.getName() + ", " + product.getPrice() + " CHF");
        }

        System.out.println("Geben sie eine Aktion an:");
        System.out.println("[1] Produkt zum Warenkorb hinzufügen");
        System.out.println("[stop] Produkt Liste verlassen");


        String input = getScanner().next("Bitte gebe eine Valide Aktion ein.", "1");

        if (input == null) {
            System.out.println("Produktliste verlassen.");
            return;
        }

        if (input.equalsIgnoreCase("1")) {
            System.out.println("Geben sie die Id des Objektes an das sie zum Warenkorb hinzufügen wollen.");

            System.out.println(String.join(" ", products.keySet().stream()
                    .map(integer -> "" + integer)
                    .toArray(String[]::new)));

            Integer prodictId = getScanner().nextInteger("Bitte geben sie eine Gütlige Produkt Id ein.",
                    products.keySet().stream()
                    .map(integer -> "" + integer)
                    .toArray(String[]::new));

            System.out.println("Geben Sie die gewünschte Anzahl an:");

            Integer amount = getScanner().nextInteger("Bitte geben sie eine Valide Zahl grösser als 0 an.", integer -> integer > 0);


            Product product = products.get(prodictId);
            mainService.getShoppingCartService().getCart().addItem(amount, product);

            System.out.println(product.getName() + " wurde " + amount + " mal hinzugefügt.");
        }else {
            System.out.println("invalid user input!");
        }
    }

    public void loadProducts() {
        products = new HashMap<>();
        products.put(1, new Product(1, 10,"p1"));
        products.put(2, new Product(2, 20,"p2"));
        products.put(3, new Product(3, 15,"p3"));
        products.put(4, new Product(4, 12,"p4"));
        products.put(5, new Product(5, 1,"p5"));
        products.put(6, new Product(6, 5,"p6"));
        products.put(7, new Product(7, 5, "p7"));
    }


}
