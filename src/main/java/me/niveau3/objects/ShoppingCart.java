package me.niveau3.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    List<ShoppingCartItem> items;
    int current = 0;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public int addItem(int amount, Product product) {
        items.add(new ShoppingCartItem(current, amount,product));
        current += 1;
        return current;
    }


    public void removeItem(int amount, Product product) {
        items.remove(new ShoppingCartItem(current, amount,product));
        current += 1;

    }

    @Getter
    @AllArgsConstructor
    public class ShoppingCartItem {
        int id;
        int amount;
        Product product;
    }
}



