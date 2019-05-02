package me.niveau3.services.bank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.niveau3.api.objects.Account;
import me.niveau3.services.MainService;
import me.niveau3.api.util.PasswordHelper;
import service.api.AbstractRunContinously;

/**
 * the main class behind the account system, this class is most responsible for the Gui part and it will call more low
 * level Methods.
 *
 * This Service is not designed to be able to work Concurrent, it can handle multiple accounts but it can not really
 * handle multiple Users at once.
 */
@Getter
public class BankService extends AbstractRunContinously {
    private MainService mainService;
    @Setter(AccessLevel.PACKAGE)
    private Account loggedInAccount;

    public BankService(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * This Method will be executed every time the Program gets started. This means that it will be executed every time
     * the Program in inits self will be started as well es every time a Action like see Bank balance is complete and the
     * user decides not to stop the program via the stop command.
     */
    @Override
    public void execute() {
        //<editor-fold desc="Bei 0 Accounts">
        if (mainService.getAccountManager().getAccountCount() < 1) {
            handleAccountCreation();
            return;
        }
        //</editor-fold>

        //<editor-fold desc="nächste Aktion auswählen">
        System.out.println("Bitte gebe ein ob du einen Account erstellen oder dich bei einem anmelden möchtest:");
        System.out.println("[1] Account erstellen");
        System.out.println("[2] sich bei Account anmelden!");
        System.out.println("[3] Accounts auflisten");
        System.out.println("[stop] Programm beenden");
        String input = getScanner().next("Fehlerhafte eingabe, versuchen sie es erneut!", "1", "2", "3");

        if (input == null) {
            //TODO stop
            return;
        }
        //</editor-fold>

        switch (input) {
            case "1":
                handleAccountCreation();
                break;
            case "2":
                mainService.getAccountOverviewService().run();
                break;
            case "3":
                System.out.println("Alle Accounts:");
                for (String s : mainService.getAccountManager().getAccountNames()) {
                    System.out.println(s);
                }
                break;
        }
    }

    /**
     * This Method is a Part the GUI for the Account Management.
     * It Handles the creation of the Account as well as the default amount.
     */
    private void handleAccountCreation() {
        System.out.println("bitte geben sie den Namen des Accounts an, den sie erstellen möchten:");
        String name = getScanner().next();

        if (name == null) {
            System.out.println("Account erstellen abgebrochen.");
            return;
        }

        if (mainService.getAccountManager().exists(name)) {
            System.out.println("Dieser Account existiert bereits, Account erstellen abgebrochen.");
            return;
        }
        System.out.println("Geben Sie das passwort an:");
        String passwordhash = PasswordHelper.getPBKDF(getScanner().next());

        if (passwordhash == null) {
            System.out.println("account erstellen abgeborchen.");
            return;
        }

        System.out.println("Bitte geben sie Ihr startkapital ein.");
        Double startKapital = getScanner().nextDouble("Bitte geben sie eine Valide Zahl grösser oder gleich 0 ein!", aDouble -> aDouble >= 0);

        if (startKapital == null) {
            System.out.println("Account erstellen abgebrochen.");
            return;
        }

        mainService.getAccountManager().addAccount(new Account(startKapital, name), passwordhash);
        System.out.println("Account mit dem Namen " + name + " wurde erstellt!");
        mainService.getFileManager().saveAccountManager();
    }



    /**
     * TUI for logging into a Account.
     * @return the account that will be logged into. null if there was a error or the User enters stop.
     */
    public Account login() {
        //<editor-fold desc="GetAccountName">
        System.out.println("Bitte geben sie den Namen ihres Accounts an.");
        String[] strings = mainService.getAccountManager().getAccountNames().toArray(new String[0]);
        String accName = getScanner().next(
                "Bitte geben einen Account an der existiert und genug geld auf dem Konto hat."
                , strings);

        if (accName == null) {
            System.out.println("Zahlung mit Karte abgebrochen.");
            return null;
        }
        //</editor-fold>

        //<editor-fold desc="Passwort Check">
        while (true) {
            System.out.println("Geben sie ihr Passwort an!");
            String next = getScanner().next();
            if (next == null) {
                System.out.println("Account anmelden abgebrochen!");
                return null;
            }

            if (this.getMainService().getAccountManager().canLogin(accName, next)) {
                loggedInAccount = this.getMainService().getAccountManager().getAccount(accName);
                return loggedInAccount;
            } else {
                System.out.println("Anmeldung fehlgeschlagen");
            }
        }
        //</editor-fold>
    }
}