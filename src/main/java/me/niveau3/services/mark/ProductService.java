package me.niveau3.services.mark;

import lombok.Getter;
import me.niveau3.api.objects.Product;
import me.niveau3.services.MainService;
import service.api.AbstractRunContinously;

/**
 * this service makes it possible to add products to the shopping cart.
 */
public class ProductService extends AbstractRunContinously {

    @Getter
    private MainService mainService;


    public ProductService(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * This Method will list all Products and lets you chose between products that you would wannt to add to your
     * shopping cart.
     */
    @Override
    public void execute() {
        //<editor-fold desc="Produkte auflisten">
        for (Product product : mainService.getProductManager().getProducts().values()) {
            System.out.println("[" + product.getId()+ "] " + product.getName() + ", " + product.getPrice() + " CHF");
        }
        //</editor-fold>

        //<editor-fold desc="Produkt zum warenkorb hinzufügen">
        System.out.println("Geben sie die Id des Objektes an das sie zum Warenkorb hinzufügen wollen.");

        Integer productId = getScanner().nextInteger("Bitte geben sie eine Gütlige Produkt Id ein.",
                mainService.getProductManager().getProducts().keySet().stream()
                .map(integer -> "" + integer)
                .toArray(String[]::new)
        );

        if (productId == null) {
            System.out.println("Produktliste geschlossen.");
            return;
        }

        System.out.println("Geben Sie die gewünschte Anzahl an:");

        Integer amount = getScanner().nextInteger("Bitte geben sie eine Valide Zahl grösser als 0 an.", integer -> integer > 0);

        Product product = mainService.getProductManager().getProducts().get(productId);
        mainService.getShoppingCartService().getCart().addItem(amount, product);

        System.out.println(product.getName() + " wurde " + amount + " mal hinzugefügt.");
        //</editor-fold>
    }
}
