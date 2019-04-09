package me.niveau3.services.mark;

import me.niveau3.objects.Product;
import service.api.IProgram;
import service.api.IStopable;

import java.util.ArrayList;
import java.util.List;

public class ProductService implements IProgram, IStopable {

    private MarkService markService;
    private List<Product> products;


    public ProductService(MarkService markService) {
        this.markService = markService;
        loadProducts();
    }

    @Override
    public void execute() {
        for (Product product : products) {
            System.out.println("[ " + product.getId()+ " ]\t " + product.getName() + ", " + product.getPrice() + " CHF");
        }

        System.out.println("Geben sie eine Aktion an:");
        System.out.println("[1] Produkt zum Warenkorb hinzufügen");
        System.out.println("[stop] Produkt Liste verlassen.");


        String input = getScanner().next("Bitte gebe eine Valide Aktion ein.", "1");

        if (input == null) {
            System.out.println("Warenkorb verlassen.");
            return;
        }

        if (input.equalsIgnoreCase("1")) {
            //todo implement add item to cart
        }else {
            System.out.println("invalid user input!");
        }
    }

    public void loadProducts() {
        products = new ArrayList<>();
        products.add(new Product(1, 10,"p1"));
        products.add(new Product(2, 20,"p2"));
        products.add(new Product(3, 15,"p3"));
        products.add(new Product(4, 12,"p4"));
        products.add(new Product(5, 1,"p5"));
        products.add(new Product(6, 5,"p6"));
        products.add(new Product(7, 5, "p7"));
    }


}
