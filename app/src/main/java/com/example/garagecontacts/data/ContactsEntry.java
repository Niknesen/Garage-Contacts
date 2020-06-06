package com.example.garagecontacts.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class ContactsEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // Name, surname,email of a contact.
    private String name, surname,email;

    public ContactsEntry(int id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
@Ignore()
    public ContactsEntry(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
