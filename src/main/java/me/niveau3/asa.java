package me.niveau3;

import java.util.UUID;

public class asa {
    public static void main(String[] args) {
        a("2e49bcec-8225-4040-aaa2-fe3bd289a653");
        System.out.println("===");
        b("2e49bcec-8225-4040-aaa2-fe3bd289a653");
    }

    public static void a(String s) {
        UUID a = UUID.fromString(s);
        UUID b = a;

        a = null;

        System.out.println(b);
        System.out.println(a);
    }


    public static void b(String s) {
        UUID a = UUID.fromString(s);
        UUID b = a;

        a = UUID.randomUUID();

        System.out.println(b);
        System.out.println(a);
    }

}
