package me.niveau3.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * a simple date holder for all the products.
 */
@AllArgsConstructor
@Getter
public class Product implements Serializable {
    int id;
    double price;
    String name;
}
