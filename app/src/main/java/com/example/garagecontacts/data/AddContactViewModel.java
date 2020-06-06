package com.example.garagecontacts.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddContactViewModel extends ViewModel {

    public AddContactViewModel(ContactsDB appBase, int contactId) {
        mContact = appBase.contactsDao().loadContactById(contactId);
    }

    private LiveData<ContactsEntry> mContact;

    public LiveData<ContactsEntry> getmContact() {
        return mContact;
    }
}