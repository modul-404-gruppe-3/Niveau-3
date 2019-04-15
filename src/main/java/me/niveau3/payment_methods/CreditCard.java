package me.niveau3.payment_methods;

import me.niveau3.api.AbstractPaymentMethod;
import me.niveau3.services.MainService;
import service.api.IScanner;

public class CreditCard extends AbstractPaymentMethod {

    public CreditCard(MainService mainService) {
        super(mainService);
    }

    @Override
    public String getDisplayName() {
        return "Kreditkarte";
    }

    @Override
    public void execute(IScanner scanner) {
        final double amount = getCart().getItems()
                .values().stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getAmount())
                .sum();

        PaymentUtils.payItems(getMainService(), scanner, amount);
    }
}
