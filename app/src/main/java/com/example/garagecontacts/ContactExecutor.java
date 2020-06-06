package com.example.garagecontacts;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ContactExecutor {
    private static final Object LOCK = new Object();
    private static  ContactExecutor sInstance;
    private final Executor diskIO;

    private ContactExecutor(Executor diskIO) {
        this.diskIO = diskIO;

    }
    public static ContactExecutor getInstance() {
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = new ContactExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return  sInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

}
