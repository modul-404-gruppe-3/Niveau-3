package me.niveau3.services.mark;

import service.api.IProgram;
import service.api.IStopable;

public class ShoppingCartService implements IStopable, IProgram {



    @Override
    public void execute() {
        //todo print shopping cart content
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
            //todo implement remove items from cart
        }else {
            System.out.println("invalid user input!");
        }
    }
}
