package me.niveau3.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class Product implements Serializable {
    int id;
    double price;
    String name;
}
