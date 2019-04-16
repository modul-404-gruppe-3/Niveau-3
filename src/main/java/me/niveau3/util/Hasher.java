package me.niveau3.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
    /**
     * a simple hasher for passwords etc.
     * @param input the string to be hashed
     * @return the hashed string
     */
    public static String getMd5(String input)
    {
        try {
            String hashtext =  new BigInteger(1, MessageDigest.getInstance("MD5").digest(input.getBytes())).toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSHA(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);
            return null;
        }
    }


    public static void main(String[] args) {
        System.out.println(getSHA("baum"));
        System.out.println(getSHA("baum"));
    }
}
