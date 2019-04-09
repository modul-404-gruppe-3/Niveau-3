package me.niveau3;

import me.niveau3.services.MainService;

public class Main {
    public static void main(String[] args) {
        MainService mainService = new MainService();
        while (!mainService.isStop()) {
            mainService.execute();
        }
    }
}
