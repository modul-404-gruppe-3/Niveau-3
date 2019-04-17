package me.niveau3.api.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * this object is used to make it possible to theoretically have multiple shopping carts.
 * It serves as a List of ShoppingCartItems with additional Functionality.
 */
public class ShoppingCart implements Serializable {
    @Getter
    HashMap<Integer, ShoppingCartItem> items;
    int current = 0;

    public ShoppingCart() {
        items = new HashMap<>();
    }

    /**
     *
     * @param amount the amount of Product.
     * @param product the product object
     * @return the id in the shopping cart
     */
    public int addItem(int amount, Product product) {
        items.put(current, new ShoppingCartItem(current, amount,product));
        current += 1;
        return current-1;
    }

    /**
     * @return the total coast as a double
     */
    public double getTotalAmount() {
        return items.values().stream().mapToDouble(value -> value.getAmount() * value.getProduct().getPrice()).sum();
    }

    /**
     * removes a ShoppingCartItem according to its id.
     * @param id the id that is assigned when adding the item to the cart.
     * @return
     */
    public ShoppingCartItem removeItem(int id) {
        var item = items.remove(id);
        current += 1;
        return item;
    }

    /**
     * removes every Item in the list and resets the id count.
     * @return all Items in the List.
     */
    public List<ShoppingCartItem> clear() {
        var list = new ArrayList<ShoppingCartItem>();
        for (ShoppingCartItem item : items.values()) {
            list.add(item.clone());
        }
        items.clear();
        return list;
    }

    /**
     * a simple container object with the sole purpose of storing the amount and the Product and the product its self.
     */
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