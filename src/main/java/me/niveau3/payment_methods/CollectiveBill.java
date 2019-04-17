package me.niveau3.payment_methods;

import lombok.Getter;
import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.api.objects.Bill;
import me.niveau3.api.objects.ShoppingCart;
import me.niveau3.services.MainService;
import me.niveau3.api.util.PaymentUtils;

/**
 * the method that allows payment on account payment.
 */
public class CollectiveBill extends AbstractPaymentMethod {
    @Getter
    private static Bill noLoginBill;
    @Getter
    private static CollectiveBill instance;

    public CollectiveBill(MainService mainService) {
        super(mainService);
        instance = this;
        noLoginBill = new Bill();
    }

    private Bill getBill() {
        return  (getMainService().getBankService().getLoggedInAccount() == null)
                ? noLoginBill
                : getMainService().getBankService().getLoggedInAccount().getBill();
    }

    @Override
    public String getDisplayName() {
        return "Auf Rechnung";
    }

    @Override
    public boolean execute() {
        ShoppingCart cart = getMainService().getShoppingCartService().getCart();

        getBill().getItems().addAll(cart.clear());
        return true;
    }

    @Override
    public boolean pay() {
        return PaymentUtils.payItems(getMainService(), getBill().getItems());
    }
}