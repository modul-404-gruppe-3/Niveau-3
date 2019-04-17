package me.niveau3.api.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Bill implements Serializable {
    private List<ShoppingCart.ShoppingCartItem> items;

    public Bill() {
        items = new ArrayList<>();
    }

    public void transferTo(Bill bill) {
        bill.getItems().addAll(items);
    }
}
