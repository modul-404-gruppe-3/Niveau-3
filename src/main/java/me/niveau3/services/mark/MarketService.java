package me.niveau3.services.mark;

import lombok.Getter;
import me.niveau3.services.MainService;
import service.api.AbstractProgram;
import service.api.IStopable;

/**
 * The main service for all market activity
 */
@Getter
public class MarketService extends AbstractProgram implements IStopable {
    private MainService mainService;

    public MarketService(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * this method lets you chose between a showing the product list and showing the shopping cart.
     */
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
                this.mainService.getProductService().execute();
                break;
            case "2":
                this.mainService.getShoppingCartService().execute();
                break;
            default:
                System.out.println("invalid user input!");
                break;
        }
        mainService.getFileManager().saveAccountManager();
    }
}
