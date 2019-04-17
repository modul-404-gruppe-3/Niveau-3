package me.niveau3.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.niveau3.api.objects.Product;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This Manager is a simple Holder for all Products that exist.
 */
@AllArgsConstructor
public class ProductManager implements Serializable {
    @Getter
    private HashMap<Integer, Product> products;

    public ProductManager() {
        products = new HashMap<>();
    }
}
