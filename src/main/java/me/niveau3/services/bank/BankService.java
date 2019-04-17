package me.niveau3.services.bank;

import lombok.Getter;
import me.niveau3.api.objects.Account;
import me.niveau3.payment_methods.CollectiveBill;
import me.niveau3.services.MainService;
import me.niveau3.api.util.Hasher;
import service.api.AbstractProgram;
import service.api.IStopable;

/**
 * the main class behind the account system, this class is most responsible for the Gui part and it will call more low
 * level Methods.
 *
 * This Service is not designed to be able to work Concurrent, it can handle multiple accounts but it can not really
 * handle multiple Users at once.
 */
@Getter
public class BankService extends AbstractProgram implements IStopable {
    boolean stop = false;
    private MainService mainService;
    private Account loggedInAccount;

    public BankService(MainService mainService) {
        this.mainService = mainService;
    }

    /**
     * This Method will be executed every time the Program gets started. This means that it will be executed every time
     * the Program in inits self will be started as well es every time a Action like see Bank balance is complete and the
     * user choses not to stop the program via the stop command.
     */
    @Override
    public void execute() {
        if (mainService.getAccountManager().getAccountCount() < 1) {
            handleAccountCreation();
            return;
        }

        System.out.println("Bitte gebe ein ob du einen Account erstellen oder dich bei einem anmelden möchtest:");
        System.out.println("[1] Account erstellen");
        System.out.println("[2] sich bei Account anmelden!");
        System.out.println("[3] Accounts auflisten");
        System.out.println("[stop] Programm beenden");
        String input = getScanner().next("Fehlerhafte eingabe, versuchen sie es erneut!", "1", "2", "3");

        if (input == null) {
            stop = true;
            return;
        }

        switch (input) {
            case "1":
                handleAccountCreation();
                break;
            case "2":
                handleAccountOperations();
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
        String passwordhash = Hasher.getPBKDF(getScanner().next());

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
    }

    /**
     * This Method is a Part the GUI for the Account Management.
     * It Handles every Possible thing that can be done with a Account.
     */
    private void handleAccountOperations() {
        System.out.println("geben sie einen Accountnamen ein:");

        Account account = this.login();

        if (account == null) {
            System.out.println("Aktion abgebrochen.");
            return;
        }

        System.out.println("bitte gebe ein, was du machen willst:");
        System.out.println("[1] Abheben");
        System.out.println("[2] Hinzufügen");
        System.out.println("[3] Bilanz anzeigen");
        System.out.println("[4] Geld überweisen");
        System.out.println("[5] Rechnung begleichen");
        System.out.println("[6] Abmelden");
        System.out.println("[stop] Programm beenden");

        String input = getScanner().next("Invalider Input, versuchen sie es erneut!", "1", "2", "3", "4", "5", "6");

        if (input == null) {
            System.out.println("Auswahl abgebrochen.");
            return;
        }
        switch (input) {
            case "1":
                System.out.println("Bitte geben sie eine Zahl ein:");

                account.takeMoney(getScanner().nextDouble("Bitte geben sie eine Valide Zahl grösser als 0 ein, deren Betrag sie besitzen!", aDouble -> aDouble > 0  && account.getBilanz() - aDouble > 0));
                break;
            case "2":
                System.out.println("Bitte geben sie eine Zahl ein:");
                account.addMoney(getScanner().nextDouble("Bitte geben sie eine Valide Zahl grösser als 0 ein!", aDouble -> aDouble > 0));
                break;
            case "3":
                System.out.println("sie haben Aktuell " + account.getBilanz() + " auf ihrem Konto!");
                break;
            case "4":
                System.out.println("geben sie den Namen des Accounts an auf den sie Geld überweisen wollen.");
                String targetAccountName = getScanner().next("Dieser Account existiert nicht, bitte versuchen sie er erneut.",
                        mainService.getAccountManager().getAccountNames().toArray(new String[0]));

                if (targetAccountName == null) {
                    System.out.println("Überweisung abgebrochen.");
                    return;
                }

                Account targetAccount = mainService.getAccountManager().getAccount(targetAccountName);
                System.out.println("geben sie bitte jetzt an, wie viel sie überweisen wollen.");
                Double amount = getScanner().nextDouble("Bitte geben sie eine Valide Zahl grösser als 0 ein, deren Betrag sie besitzen!", aDouble -> aDouble > 0 && account.getBilanz() - aDouble > 0);


                if (amount == null) {
                    System.out.println("Überweisung abgebrochen.");
                    return;
                }

                account.transfer(targetAccount, amount);
                System.out.println("Du hast " + targetAccountName + " " + amount + " überwiesen.");
                break;
            case "5":
                CollectiveBill.getInstance().pay();
                break;
            case "6":
                loggedInAccount =null;
                System.out.println("erfolgreich abgemeldet.");
                break;
        }
    }

    public Account login() {
        System.out.println("Bitte geben sie den Namen ihres Accounts an.");
        String[] strings = mainService.getAccountManager().getAccountNames().toArray(new String[0]);
        String accName = getScanner().next(
                "Bitte geben einen Account an der existiert und genug geld auf dem Konto hat."
                , strings);

        if (accName == null) {
            System.out.println("Zahlung mit Karte abgebrochen.");
            return null;
        }

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
    }

    @Override
    public void stop() {
        mainService.getFileManager().save();
    }
}
