package me.niveau3.services.bank;

import lombok.Getter;
import me.niveau3.objects.Account;
import me.niveau3.objects.AccountManager;
import me.niveau3.services.MainService;
import me.niveau3.util.Hasher;
import service.api.IProgram;
import service.api.IStopable;

/**
 * the main class behind the account system, this class is most responsible for the Gui part and it will call more low
 * level Methods.
 *
 * This Service is not designed to be able to work Concurrent, it can handle multiple accounts but it can not really
 * handle multiple Users at once.
 */
@Getter
public class AccountService implements IStopable, IProgram {
    boolean stop = false;
    private AccountManager accountManager;
    private MainService mainService;

    public AccountService(MainService mainService) {
        this.mainService = mainService;
        this.accountManager = new AccountManager();
    }

    /**
     * This Method will be executed every time the Program gets started. This means that it will be executed every time
     * the Program in initsself will be started as well es every time a Action like see Bank balance is complete and the
     * user choses not to stop the program via the stop command.
     */
    @Override
    public void execute() {
        if (accountManager.getAccountCount() < 1) {
            handleAccountCreation();
            return;
        }

        System.out.println("Bitte gebe ein ob du einen Account erstellen oder dich bei einem anmelden möchtest:");
        System.out.println("1 - Account erstellen");
        System.out.println("2 - sich bei Account anmelden!");
        System.out.println("3 - Accounts auflisten");
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
                for (String s : accountManager.getAccountNames()) {
                    System.out.println(s);
                }
                break;
        }
    }

    /**
     * This Method is a Part the GUI for the Account Management.
     * It Handles the creation of the Account as well as the default ammount.
     */
    private void handleAccountCreation() {
        System.out.println("bitte geben sie den Namen des Accounts an, den sie erstellen möchten:");
        String name = getScanner().next();

        if (name == null) {
            System.out.println("Account erstellen abgebrochen.");
            return;
        }

        if (accountManager.exists(name)) {
            System.out.println("Dieser Account existiert bereits, Account erstellen abgebrochen.");
            return;
        }
        System.out.println("Geben Sie das passwort an:");
        String passwordhash = Hasher.getMd5(getScanner().next());

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

        accountManager.addAccount(new Account(startKapital, name), passwordhash);
        System.out.println("Account mit dem Namen " + name + " wurde erstellt!");
    }

    /**
     * This Method is a Part the GUI for the Account Management.
     * It Handles every Possible thing that can be done with a Account.
     */
    private void handleAccountOperations() {
        System.out.println("geben sie einen Accountnamen ein:");

        String[] allAccountNames = accountManager.getAccountNames().toArray(new String[0]);
        System.out.println(String.join(" ", allAccountNames));
        String accountName = getScanner().next("Dieser Account existiert nicht, versuchen sie es erneut!", allAccountNames);

        if (accountName == null) {
            System.out.println("Account anmelden abgebrochen!");
            return;
        }

        while (true) {
            System.out.println("Geben sie ein passwort an!");
            String next = getScanner().next();
            if (next == null) {
                System.out.println("Account anmelden abgebrochen!");
                return;
            }

            String password = Hasher.getMd5(next);
            System.out.println("pw hash: " + password);
            if (accountManager.canLogin(accountName, password)) {
                break;
            }else {
                System.out.println("Anmeldung fehlgeschlagen");
            }
        }

        Account account = accountManager.getAccount(accountName);

        allAccountNames = accountManager.getAccountNames().stream().filter(s -> !s.equalsIgnoreCase(accountName)).toArray(String[]::new);


        System.out.println("bitte gebe ein, was du machen willst:");
        System.out.println("1 - Abheben");
        System.out.println("2 - Hinzufügen");
        System.out.println("3 - Bilanz anzeigen");
        System.out.println("4 - Geld überweisen");
        System.out.println("5 - Rechnung begleichen");

        String input = getScanner().next("Invalider Input, versuchen sie es erneut!", "1", "2", "3", "4", "5");

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
                String targetAccountName = getScanner().next("Dieser Account existiert nicht, bitte versuchen sie er erneut.", allAccountNames);

                if (targetAccountName == null) {
                    System.out.println("Überweisung abgebrochen.");
                    return;
                }

                Account targetAccount = accountManager.getAccount(targetAccountName);
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


                break;
        }
    }
}
