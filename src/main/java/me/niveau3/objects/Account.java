package me.niveau3.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/**
 * Simple data holder Object for the Account information.
 */
public class Account {
    private double bilanz;
    private String name;

    /**
     * adds the given amount the the bilanz double.
     * @param amount the amount to be added to the Account.
     */
    public void addMoney(double amount){
        if (amount < 1) {
            throw new IllegalStateException(amount + " ist keine gültiger Wert, die zahl muss grösser als 0 sein!");
        }
        this.bilanz += amount;
    }

    /**
     *
     * removes the given amount the the bilanz double.
     * @param amount the amount to be removed from the Account.
     */
    public void takeMoney(double amount){
        if (amount < 1) {
            throw new IllegalStateException(amount + " ist keine gültiger Wert, die zahl muss grösser als 0 sein!");
        }
        this.bilanz -= amount;
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
