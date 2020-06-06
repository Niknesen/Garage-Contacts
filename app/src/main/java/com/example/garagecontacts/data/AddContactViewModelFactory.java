package com.example.garagecontacts.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddContactViewModelFactory extends ViewModelProvider.NewInstanceFactory{


    public AddContactViewModelFactory(ContactsDB mDb, int mContactId) {
        this.mDb = mDb;
        this.mContactId = mContactId;
    }

    private final ContactsDB mDb;
    private final int mContactId;

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddContactViewModel(mDb,mContactId);
    }
}
