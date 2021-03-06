package me.niveau3.api.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
/**
 * Simple data holder Object for the Account information.
 */
public class Account implements Serializable {
    private double balance;
    private String name;
    private ShoppingCart cart;
    private Bill bill;

    public Account(double balance, String name) {
        this.balance = balance;
        this.name = name;
        this.cart = new ShoppingCart();
        this.bill = new Bill();
    }

    /**
     * adds the given amount the the balance double.
     * @param amount the amount to be added to the Account.
     */
    public void addMoney(double amount){
        if (amount <= 0 ) {
            throw new IllegalStateException(amount + " ist keine gültiger Wert, die zahl muss grösser als 0 sein!");
        }
        this.balance += amount;
    }

    /**
     *
     * removes the given amount the the balance double.
     * @param amount the amount to be removed from the Account.
     */
    public void takeMoney(double amount){
        if (amount <= 0) {
            throw new IllegalStateException(amount + " ist keine gültiger Wert, die zahl muss grösser als 0 sein!");
        }
        this.balance -= amount;
    }

    /**
     * If the Account is null this will give a NullPointerException.
     * @param account The Target Account.
     * @param amount the Amount that should be send from the current account to the account from the first parameter.
     */
    public void transfer(Account account, double amount) {
        this.takeMoney(amount);
        account.addMoney(amount);
    }
}
