package me.niveau3.api.util;

import me.niveau3.api.objects.Account;
import me.niveau3.api.objects.ShoppingCart;
import me.niveau3.payment_methods.CollectiveBill;
import me.niveau3.services.MainService;

import java.util.List;

/**
 * A Collection of Methods that is used as help for PaymentMethods.
 */
public class PaymentUtils {
    /**
     * the method that will go to a account and take the money after verification.
     */
    public static boolean payItems(MainService mainService, List<ShoppingCart.ShoppingCartItem> items) {
        //<editor-fold desc="Amount berechnen">
        double amount = items
                .stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getAmount())
                .sum();
        //</editor-fold>

        System.out.println("Du willst " + amount + " mit deinem Bankkonto Bezahlen.");


        //<editor-fold desc="Account bekommen">
        Account account;
        if (mainService.getBankService().getLoggedInAccount() == null) {
            //<editor-fold desc="Nicht angemeldet">
            System.out.println("Wenn sie sich Anmelden werden alle einträge vom öffentlichen Warenkorb in ihren persönlichen verschoben.");
            account = mainService.getBankService().login();

            if (account == null) {
                return false;
            }

            CollectiveBill.getNoLoginBill().transferTo(account.getBill());
            CollectiveBill.getNoLoginBill().getItems().clear();
            //</editor-fold>
        }else {
            account = mainService.getBankService().getLoggedInAccount();
        }

        if (account == null) {
            System.out.println("Aktion abgebrochen.");
            return false;
        }
        //</editor-fold>

        account.takeMoney(amount);

        System.out.println( amount + " wurde dem von ihnen angegeben account abgezogen. Restilche Billanz: " + account.getBalance());
        return true;
    }
}
