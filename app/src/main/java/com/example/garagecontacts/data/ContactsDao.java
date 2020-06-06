package com.example.garagecontacts.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactsDao {


    @Query("SELECT * FROM contacts")
    LiveData<List<ContactsEntry>> loadAllContacts();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateContact(ContactsEntry contactsEntry);

    @Delete()
    void deleteContact(ContactsEntry contactsEntry);

    @Insert()
    void insertContact(ContactsEntry contactsEntry);

    @Query("SELECT * FROM contacts WHERE id = :id")
    LiveData<ContactsEntry> loadContactById(int id);

    @Query("DELETE FROM contacts")
    void deleteAll();
}

