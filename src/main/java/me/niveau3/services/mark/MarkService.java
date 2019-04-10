package me.niveau3.services.mark;

import lombok.Getter;
import service.api.IProgram;
import service.api.IStopable;
@Getter
public class MarkService implements IProgram, IStopable {
    private ShoppingCartService shoppingCartService;
    private ProductService productService;

    public MarkService() {
        this.shoppingCartService = new ShoppingCartService();
        this.productService = new ProductService(this);
    }

    @Override
    public void execute() {
        System.out.println("Bitte wähle eine Aktion aus:");
        System.out.println("[1] Produktliste anzeigen");
        System.out.println("[2] Warenkorb anzeigen");
        System.out.println("[stop] Zum hauptmenü zurück");
        String input = getScanner().next("Bitte gebe eine Valide aktion an!", "1", "2");

        if (input == null) {
            System.out.println("Kehre zum Hauptmenü zurück");
            return;
        }

        switch (input) {
            case "1":
                this.productService.execute();
                break;
            case "2":
                this.shoppingCartService.execute();
                break;
            default:
                System.out.println("invalid user input!");
                break;
        }
    }
}
