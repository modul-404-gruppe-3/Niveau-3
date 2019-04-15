package me.niveau3.services;

import lombok.Getter;
import me.niveau3.objects.AccountManager;
import me.niveau3.services.bank.AccountService;
import me.niveau3.services.bank.BankService;
import me.niveau3.objects.PaymentMethodManager;
import me.niveau3.services.mark.MarkService;
import me.niveau3.services.mark.ProductService;
import me.niveau3.services.mark.ShoppingCartService;
import service.api.AbstractProgram;
import service.api.IStopable;

@Getter
public class MainService extends AbstractProgram implements IStopable {
    private MarkService markService;
    private BankService bankService;
    private PaymentMethodManager paymentMethodManager;
    private ShoppingCartService shoppingCartService;
    private ProductService productService;
    private boolean stop;
    private AccountManager accountManager;
    private AccountService accountService;

    public MainService() {
        this.stop = false;
        this.markService = new MarkService(this);
        this.bankService = new BankService(this);
        this.accountService = new AccountService(this);
        this.accountManager = new AccountManager();
        this.shoppingCartService = new ShoppingCartService(this);
        this.productService = new ProductService(this);
        this.paymentMethodManager = new PaymentMethodManager();
    }

    public void run() {
        while (!this.isStop()) {
            this.execute();
        }
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
