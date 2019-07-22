package me.niveau3.services.bank;

import me.niveau3.api.objects.Account;
import me.niveau3.payment_methods.CollectiveBill;
import me.niveau3.services.MainService;
import service.api.AbstractRunContinously;

public class AccountOverviewService extends AbstractRunContinously {
    private MainService mainService;

    public AccountOverviewService(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * This Method is a Part the GUI for the Account Management.
     * It Handles every Possible thing that can be done with a Account.
     */
    @Override
    public void execute() {
        //<editor-fold desc="Überprüfen ob einloggen notwenig ist">
        Account account;
        if (mainService.getBankService().getLoggedInAccount() == null) {
            account = mainService.getBankService().login();
        }else {
            account = mainService.getBankService().getLoggedInAccount();
        }

        if (account == null) {
            System.out.println("Aktion abgebrochen.");
            return;
        }
        //</editor-fold>

        //<editor-fold desc="nächste Aktion auswählen">
        System.out.println("hallo " + account.getName());
        System.out.println("bitte gebe ein, was du machen willst:");
        System.out.println("[1] Abheben");
        System.out.println("[2] Hinzufügen");
        System.out.println("[3] Bilanz anzeigen");
        System.out.println("[4] Geld überweisen");
        System.out.println("[5] Rechnung begleichen");
        System.out.println("[6] Abmelden");
        System.out.println("[stop] Zum Hauptmenü zurück.");

        String input = getScanner().next("Invalider Input, versuchen sie es erneut!", "1", "2", "3", "4", "5", "6");

        if (input == null) {
            System.out.println("Auswahl abgebrochen.");
            return;
        }
        //</editor-fold>
        switch (input) {
            case "1":
                System.out.println("Bitte geben sie eine Zahl ein:");

                account.takeMoney(getScanner().nextDouble("Bitte geben sie eine Valide Zahl grösser als 0 ein, deren Betrag sie besitzen!", aDouble -> aDouble > 0  && account.getBalance() - aDouble > 0));
                break;
            case "2":
                System.out.println("Bitte geben sie eine Zahl ein:");
                account.addMoney(getScanner().nextDouble("Bitte geben sie eine Valide Zahl grösser als 0 ein!", aDouble -> aDouble > 0));
                break;
            case "3":
                System.out.println("sie haben Aktuell " + account.getBalance() + " auf ihrem Konto!");
                break;
            case "4":
                //<editor-fold desc="Geld überweisen">
                System.out.println("geben sie den Namen des Accounts an auf den sie Geld überweisen wollen.");
                String targetAccountName = getScanner().next("Dieser Account existiert nicht, bitte versuchen sie er erneut.",
                        mainService.getAccountManager().getAccountNames().toArray(new String[0]));

                if (targetAccountName == null) {
                    System.out.println("Überweisung abgebrochen.");
                    return;
                }

                Account targetAccount = mainService.getAccountManager().getAccount(targetAccountName);
                System.out.println("geben sie bitte jetzt an, wie viel sie überweisen wollen.");
                Double amount = getScanner().nextDouble("Bitte geben sie eine Valide Zahl grösser als 0 ein, deren Betrag sie besitzen!", aDouble -> aDouble > 0 && account.getBalance() - aDouble > 0);


                if (amount == null) {
                    System.out.println("Überweisung abgebrochen.");
                    return;
                }

                account.transfer(targetAccount, amount);
                System.out.println("Du hast " + targetAccountName + " " + amount + " überwiesen.");
                break;
            //</editor-fold>
            case "5":
                if (!CollectiveBill.getInstance().pay()) {
                    System.out.println("Zahlung nicht erfolgreich da keine Items auf ihrer rechung sind.");
                }
                break;
            case "6":
                mainService.getBankService().setLoggedInAccount(null);
                this.setStop(true);
                System.out.println("erfolgreich abgemeldet.");
                break;
        }
        mainService.getFileManager().saveAccountManager();
    }
}
