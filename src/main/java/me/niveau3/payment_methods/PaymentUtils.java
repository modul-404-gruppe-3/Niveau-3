package me.niveau3.payment_methods;

import me.niveau3.objects.Account;
import me.niveau3.objects.ShoppingCart;
import me.niveau3.services.MainService;

import java.util.List;

public class PaymentUtils {
    /**
     * the method that will go to a account and take the money after verification.
     */
    protected static void payItems(MainService mainService, List<ShoppingCart.ShoppingCartItem> items) {

        double amount = items
                .stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getAmount())
                .sum();

        System.out.println("Du willst " + amount + " mit deinem Bankkonto Bezahlen.");

        Account account;

        if (mainService.getBankService().getLoggedInAccount() == null) {
            System.out.println("Wenn sie sich Anmelden werden alle einträge vom öffentlichen Warenkorb in ihr eigener verschoben.");
            account = mainService.getBankService().login();

            OnAccount.getNoLoginBill().transferTo(account.getBill());
            OnAccount.getNoLoginBill().getItems().clear();
        }else {
            account = mainService.getBankService().getLoggedInAccount();
        }

        if (account == null) {
            System.out.println("Aktion abgebrochen.");
            return;
        }

        account.takeMoney(amount);

        System.out.println( amount + " wurde dem von ihnen angegeben account abgezogen. Restilche Billanz: " + account.getBilanz());
    }
}
