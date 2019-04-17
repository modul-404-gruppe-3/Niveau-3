package me.niveau3.services.mark;

import lombok.Getter;
import me.niveau3.api.objects.Product;
import me.niveau3.services.MainService;
import service.api.AbstractProgram;
import service.api.IStopable;

/**
 * this service makes it possible to add products to the shopping cart.
 */
public class ProductService extends AbstractProgram implements IStopable {

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
        for (Product product : mainService.getProductManager().getProducts().values()) {
            System.out.println("[" + product.getId()+ "] " + product.getName() + ", " + product.getPrice() + " CHF");
        }

        System.out.println("Geben sie die Id des Objektes an das sie zum Warenkorb hinzufügen wollen.");

        System.out.println(String.join(" ", mainService.getProductManager().getProducts().keySet().stream()
                .map(integer -> "" + integer)
                .toArray(String[]::new)));

        Integer prodictId = getScanner().nextInteger("Bitte geben sie eine Gütlige Produkt Id ein.",
                mainService.getProductManager().getProducts().keySet().stream()
                .map(integer -> "" + integer)
                .toArray(String[]::new));

        System.out.println("Geben Sie die gewünschte Anzahl an:");

        Integer amount = getScanner().nextInteger("Bitte geben sie eine Valide Zahl grösser als 0 an.", integer -> integer > 0);


        Product product = mainService.getProductManager().getProducts().get(prodictId);
        mainService.getShoppingCartService().getCart().addItem(amount, product);

        System.out.println(product.getName() + " wurde " + amount + " mal hinzugefügt.");

    }
}
