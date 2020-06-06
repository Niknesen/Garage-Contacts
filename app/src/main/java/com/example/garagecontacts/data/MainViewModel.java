package com.example.garagecontacts.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<ContactsEntry>> liveData;

    public LiveData<List<ContactsEntry>> getLiveData() {
        return liveData;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        ContactsDB mDB = ContactsDB.getInstance(this.getApplication());
        liveData = mDB.contactsDao().loadAllContacts();
    }
}