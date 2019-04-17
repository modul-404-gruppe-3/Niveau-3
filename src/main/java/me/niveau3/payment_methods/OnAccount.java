package me.niveau3.payment_methods;

import lombok.Getter;
import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.objects.Bill;
import me.niveau3.objects.ShoppingCart;
import me.niveau3.services.MainService;
import service.api.IScanner;

/**
 * the method that allows payment on account payment.
 */
public class OnAccount extends AbstractPaymentMethod {
    @Getter
    private static Bill noLoginBill;
    @Getter
    private static OnAccount instance;

    public OnAccount(MainService mainService) {
        super(mainService);
        instance = this;
        noLoginBill = new Bill();
    }


    public Bill getBill() {
        return  (getMainService().getBankService().getLoggedInAccount() == null)
                ? noLoginBill
                : getMainService().getBankService().getLoggedInAccount().getBill();
    }

    @Override
    public String getDisplayName() {
        return "Auf Rechnung";
    }

    /**
     * this method will only add the items in the cart to the list to be payed later.
     */
    @Override
    public void execute(IScanner scanner) {
        ShoppingCart cart = getMainService().getShoppingCartService().getCart();

        getBill().getItems().addAll(cart.getItems().values());
        cart.clear();
    }

    /**
     * this method will remove the money from the account.
     */
    public void pay() {
        PaymentUtils.payItems(getMainService(), getBill().getItems());
    }


}
