package com.example.garagecontacts.data;

import android.content.Context;
import android.database.DatabaseUtils;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.garagecontacts.ContactExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {ContactsEntry.class}, version = 1, exportSchema = false)
public abstract class ContactsDB extends RoomDatabase {
    private static final String LOG_TAG = ContactsDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "contactsDB";
    private static ContactsDB mInstance;

    //Creating DB and populating it with dummy data
    public static ContactsDB getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        ContactsDB.class, ContactsDB.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting database instance");
        return mInstance;
    }

    public abstract ContactsDao contactsDao();


}