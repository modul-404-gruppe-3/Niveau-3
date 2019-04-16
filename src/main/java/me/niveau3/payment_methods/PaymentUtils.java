package me.niveau3.payment_methods;

import me.niveau3.objects.Account;
import me.niveau3.services.MainService;
import me.niveau3.util.Hasher;
import service.api.IScanner;

public class PaymentUtils {
    /**
     * the method that will go to a account and take the money after verification.
     * @param amount the amount that will be taken
     */
    protected static void payItems(MainService mainService, IScanner scanner, double amount) {
        System.out.println("Du willst " + amount + " mit deinem Bankkonto Bezahlen.");
        System.out.println("Bitte wähle ein Konto aus.");

        String accName = scanner.next(
                "Bitte geben einen Account an der existiert und genug geld auf dem Konto hat."
                , s -> mainService.getAccountManager().getAccount(s).getBilanz() >= amount
                , mainService.getAccountManager().getAccountNames().toArray(new String[0]));

        if (accName == null) {
            System.out.println("Zahlung mit Karte abgebrochen.");
            return;
        }

        while (true) {
            System.out.println("Geben sie ein passwort an!");
            String next = scanner.next();
            if (next == null) {
                System.out.println("Account anmelden abgebrochen!");
                return;
            }

            String password = Hasher.getMd5(next);
            if (mainService.getAccountManager().canLogin(accName, password)) {
                break;
            }else {
                System.out.println("Anmeldung fehlgeschlagen");
            }
        }


        Account account = mainService.getAccountManager().getAccount(accName);

        account.takeMoney(amount);

        System.out.println( amount + " wurde dem von ihnen angegeben account abgezogen. Restilche Billanz: " + account.getBilanz());
    }
}
