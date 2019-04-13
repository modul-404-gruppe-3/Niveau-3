package me.niveau3.objects;

import me.niveau3.api.AbstractPaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentMethodManager {
    int i = 1;
    private HashMap<String, AbstractPaymentMethod> paymentMethods;

    public PaymentMethodManager() {
        this.paymentMethods = new HashMap<>();
    }

    public void register(AbstractPaymentMethod method) {
        this.paymentMethods.put(i+"" ,method);
        i += 1;
    }

    public List<String> getIndexesAsStrings() {
        return new ArrayList<>(paymentMethods.keySet());
    }

    public AbstractPaymentMethod get(String n) {
        return paymentMethods.get(n);
    }
}
