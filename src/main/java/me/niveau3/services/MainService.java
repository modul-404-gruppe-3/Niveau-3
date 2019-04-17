package me.niveau3.services;

import lombok.Getter;
import me.niveau3.objects.AccountManager;
import me.niveau3.objects.FileManager;
import me.niveau3.services.bank.BankService;
import me.niveau3.objects.PaymentMethodManager;
import me.niveau3.services.mark.MarketService;
import me.niveau3.services.mark.ProductService;
import me.niveau3.services.mark.ShoppingCartService;
import service.api.AbstractProgram;
import service.api.IStopable;

/**
 * this is the main service that manages all other services and holds most of the managers and services in it.
 */
@Getter
public class MainService extends AbstractProgram implements IStopable {
    private MarketService marketService;
    private PaymentMethodManager paymentMethodManager;
    private ShoppingCartService shoppingCartService;
    private ProductService productService;
    private boolean stop;
    private AccountManager accountManager;
    private BankService bankService;
    private FileManager fileManager;

    public MainService() {
        this.stop = false;
        this.fileManager = new FileManager(this);
        this.marketService = new MarketService(this);
        this.bankService = new BankService(this);
        this.accountManager = fileManager.load();
        this.shoppingCartService = new ShoppingCartService(this);
        this.productService = new ProductService(this);
        this.paymentMethodManager = new PaymentMethodManager();
    }


    /**
     * this method is used in the psvm to make it that the program does not stop after 1 action.
     */
    public void run() {
        while (!this.isStop()) {
            this.execute();
        }
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
            this.stop = true;
            System.out.println("Programm wird beendet.");
            return;
        }

        switch (input) {
            case "1":
                this.bankService.execute();
                break;
            case "2":
                this.marketService.execute();
                break;
            default:
                System.out.println("invalid user input!");
                break;
        }
    }

    public boolean isStop() {
        return stop;
    }

    @Override
    public void stop() {
        fileManager.save();
    }
}
