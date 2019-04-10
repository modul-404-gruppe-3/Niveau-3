package me.niveau3.services;

import me.niveau3.services.bank.BankService;
import me.niveau3.services.mark.MarkService;
import service.api.IProgram;
import service.api.IStopable;

public class MainService implements IProgram, IStopable {

    private MarkService markService;
    private BankService bankService;
    private boolean stop;

    public MainService() {
        stop = false;
        this.markService = new MarkService();
        this.bankService = new BankService();
    }

    @Override
    public void execute() {
        System.out.println("Wählen sie eine Option aus:");
        System.out.println("[1] Bankkonto verwalten");
        System.out.println("[2] Markt öffnen");
        System.out.println("[stop] Programm beenden");

        String input = getScanner().next("Bitte geben sie eine Valide Option an!","1", "2");

        if (input == null) {
            this.stop = true;
            System.out.println("Programm wird beendet.");
            return;
        }

        switch (input) {
            case "1":
                this.bankService.execute();
                break;
            case "2":
                this.markService.execute();
                break;
            default:
                System.out.println("invalid user input!");
                break;
        }
    }

    public boolean isStop() {
        return stop;
    }
}
