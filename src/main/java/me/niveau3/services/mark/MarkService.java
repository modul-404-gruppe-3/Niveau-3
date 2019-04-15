package me.niveau3.services.mark;

import lombok.Getter;
import me.niveau3.services.MainService;
import service.api.AbstractProgram;
import service.api.IStopable;
@Getter
public class MarkService extends AbstractProgram implements IStopable {
    private MainService mainService;

    public MarkService(MainService mainService) {
        this.mainService = mainService;
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
                this.mainService.getProductService().execute();
                break;
            case "2":
                this.mainService.getShoppingCartService().execute();
                break;
            default:
                System.out.println("invalid user input!");
                break;
        }
    }
}
