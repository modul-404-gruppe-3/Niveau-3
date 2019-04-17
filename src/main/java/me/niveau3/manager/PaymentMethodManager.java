package me.niveau3.manager;

import me.niveau3.api.AbstractPaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Manager that holds all the information about payment methods so they can be
 * added without the need of them to be hardcoded.
 */
public class PaymentMethodManager {
    int i = 1;
    private HashMap<String, AbstractPaymentMethod> paymentMethods;

    public PaymentMethodManager() {
        this.paymentMethods = new HashMap<>();
    }

    /**
     * registers a Payment method to the manager
     * @param method the Payment method to be registed. this only needs to be done once!
     */
    public void register(AbstractPaymentMethod method) {
        this.paymentMethods.put(i+"" ,method);
        i += 1;
    }

    /**
     * a utility method to get all the keys for the ids of the payment method.
     * @return a list of strings with all the keys from the paymentMethods HashMap.
     */
    public List<String> getIndexesAsStrings() {
        return new ArrayList<>(paymentMethods.keySet());
    }

    /**
     * more or less a delegate implementation from the Hashmap to get access to the map without giving the whole map.
     */
    public AbstractPaymentMethod get(String n) {
        return paymentMethods.get(n);
    }
}
