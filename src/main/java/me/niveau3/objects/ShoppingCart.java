package me.niveau3.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCart {
    @Getter
    HashMap<Integer, ShoppingCartItem> items;
    int current = 0;

    public ShoppingCart() {
        items = new HashMap<>();
    }

    public int addItem(int amount, Product product) {
        items.put(current, new ShoppingCartItem(current, amount,product));
        current += 1;
        return current-1;
    }

    public ShoppingCartItem removeItem(int id) {
        var item = items.remove(id);
        current += 1;
        return item;
    }

    public List<ShoppingCartItem> clear() {
        var list = new ArrayList<ShoppingCartItem>();
        for (ShoppingCartItem item : items.values()) {
            list.add(item.clone());
        }
        items.clear();
        return list;
    }

    @Getter
    @AllArgsConstructor
    public class ShoppingCartItem implements Cloneable{
        int id;
        int amount;
        Product product;

        @Override
        public ShoppingCartItem clone() {
            return new ShoppingCartItem(id, amount, product);
        }
    }
}