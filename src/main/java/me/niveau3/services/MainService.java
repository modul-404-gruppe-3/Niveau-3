package me.niveau3.services;

import lombok.Getter;
import me.niveau3.manager.AccountManager;
import me.niveau3.manager.FileManager;
import me.niveau3.manager.PaymentMethodManager;
import me.niveau3.manager.ProductManager;
import me.niveau3.services.bank.BankService;
import me.niveau3.services.mark.MarketService;
import me.niveau3.services.mark.ProductService;
import me.niveau3.services.mark.ShoppingCartService;
import service.api.AbstractRunContinously;

/**
 * this is the main service that manages all other services and holds most of the managers and services in it.
 */
@Getter
public class MainService extends AbstractRunContinously  {
    private MarketService marketService;
    private PaymentMethodManager paymentMethodManager;
    private ShoppingCartService shoppingCartService;
    private ProductService productService;
    private AccountManager accountManager;
    private BankService bankService;
    private FileManager fileManager;
    private ProductManager productManager;
    private boolean writeToFile;

    public MainService(boolean writeToFile) {
        this.writeToFile = writeToFile;

        this.fileManager = new FileManager(this);

        this.marketService = new MarketService(this);
        this.shoppingCartService = new ShoppingCartService(this);

        this.bankService = new BankService(this);
        this.accountManager = fileManager.loadAccountManager();

        this.productManager = fileManager.loadProducts();
        this.productService = new ProductService(this);

        this.paymentMethodManager = new PaymentMethodManager();
    }

    /**
     * this method will let you chose between starting the market service and the account service.
     */
    @Override
    public void execute() {
        System.out.println("Wählen sie eine Option aus:");
        System.out.println("[1] Bankkonto verwalten");
        System.out.println("[2] Markt öffnen");
        System.out.println("[stop] Programm beenden");

        String input = getScanner().next("Bitte geben sie eine Valide Option an!","1", "2");

        if (input == null) {
            System.out.println("Programm wird beendet.");
            return;
        }

        switch (input) {
            case "1":
                this.bankService.run();
                break;
            case "2":
                this.marketService.run();
                break;
            default:
                System.out.println("invalid user input!");
                break;
        }
        fileManager.saveAccountManager();
        fileManager.saveProducts();
    }
}
