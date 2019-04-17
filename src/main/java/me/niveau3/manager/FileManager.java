package me.niveau3.manager;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.gson.Gson;
import me.niveau3.manager.AccountManager;
import me.niveau3.services.MainService;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

public class FileManager {
    private MainService mainService;

    public FileManager(MainService mainService) {
        this.mainService = mainService;
    }

    public void save() {
        try {
            Reader initialReader = new StringReader(new Gson().toJson(mainService.getAccountManager()));

            File targetFile = new File("data.json");
            targetFile.delete();
            com.google.common.io.Files.touch(targetFile);
            CharSink charSink = com.google.common.io.Files.
                    asCharSink(targetFile, Charset.defaultCharset(), FileWriteMode.APPEND);
            charSink.writeFrom(initialReader);

            initialReader.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AccountManager load() {
        try {
            AccountManager accountManager = new Gson().fromJson(Files.toString(new File("data.json"), Charsets.UTF_8), AccountManager.class);

            return accountManager;
        } catch (IOException e) {
            System.out.println("Error while loading AccountManager, creating new one. er: " + e.getMessage());
            return new AccountManager();
        }
    }
}
