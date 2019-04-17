package me.niveau3.manager;

import me.niveau3.api.objects.Account;
import me.niveau3.api.util.PasswordHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * With this class you can manage all accounts.
 * This class will be Serialized and written to JSON after every account changing thing.
 */
public class AccountManager implements Serializable {
    private HashMap<String, Account> accounts;
    private HashMap<String, String> pwhashes; // account name -> pw hash

    public AccountManager() {
        accounts = new HashMap<>();
        pwhashes = new HashMap<>();
    }

    /**
     * sets an Account, even if it already exists.
     */
    public void addAccount(Account account, String passwordHash) {
        pwhashes.put(account.getName(), passwordHash);
        accounts.put(account.getName(), account);
    }

    /**
     * @param name the name of the account
     * @return a Account Object according to the given name
     */
    public Account getAccount(String name) {
        return accounts.get(name);
    }

    /**
     * @return the Amount of Accounts that exists at the moment.
     */
    public int getAccountCount() {
        return accounts.size();
    }

    /**
     * @return all Accounts names that exist.
     */
    public Set<String> getAccountNames() {
        return accounts.keySet();
    }

    /**
     * checks if the given account exists.
     *
     * @param accountName the name of the account to check for.
     * @return true of the account exists, false if it does not.
     */
    public boolean exists(String accountName) {
        return accounts.containsKey(accountName);
    }

    public boolean canLogin(String accountName, String password) {
        return PasswordHelper.validatePassword(password, pwhashes.get(accountName));
    }
}