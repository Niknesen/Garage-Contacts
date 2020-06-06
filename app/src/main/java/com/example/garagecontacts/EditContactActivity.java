package com.example.garagecontacts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.garagecontacts.data.AddContactViewModel;
import com.example.garagecontacts.data.AddContactViewModelFactory;
import com.example.garagecontacts.data.ContactsDB;
import com.example.garagecontacts.data.ContactsEntry;

public class EditContactActivity extends AppCompatActivity {
    // Extra for the ContactID to be received in the intent
    public static final String EXTRA_CONTACT_ID = "extraContactId";
    // Extra for the ContactID to be received after rotation
    public static final String INSTANCE_CONTACT_ID = "instanceContactId";
    // Constant for default ContactId
    private static final int DEFAULT_CONTACT_ID = -1;
    // Logging
    private static final String TAG = EditContactActivity.class.getSimpleName();
    // Fields for views
    private EditText mName, mSurname, mEmail;
    //Data base
    private ContactsDB mContactsDb;
    //Helper variables
    int mContactID = DEFAULT_CONTACT_ID;
    Context context;// Find all relevant views that we will need to read user input from

    // Create touch listener to know when user change edittext views
    private boolean mContactHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mContactHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contact);
        mContactsDb = ContactsDB.getInstance(this);
        initViews();

        context = this;
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_CONTACT_ID)) {
            mContactID = savedInstanceState.getInt(INSTANCE_CONTACT_ID, DEFAULT_CONTACT_ID);
        }
        Intent intent = getIntent();
        //Check if we open existing contact. If yes - populate UI using view model via view model factory.
        if (intent != null && intent.hasExtra(EXTRA_CONTACT_ID)) {
            setTitle("Edit contact");
            mContactID = intent.getIntExtra(EXTRA_CONTACT_ID, DEFAULT_CONTACT_ID);
            AddContactViewModelFactory viewModelFactory = new AddContactViewModelFactory(mContactsDb, mContactID);
            final AddContactViewModel addTaskViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddContactViewModel.class);
            addTaskViewModel.getmContact().observe(this, new Observer<ContactsEntry>() {
                @Override
                public void onChanged(@Nullable ContactsEntry contactsEntry) {
                    addTaskViewModel.getmContact().removeObserver(this);
                    populateUI(contactsEntry);
                }
            });
        }
    }

    //Helper method to init views. Launches from onCreate method
    private void initViews() {
        mName = findViewById(R.id.edit_name);
        mSurname = findViewById(R.id.edit_surname);
        mEmail = findViewById(R.id.edit_email);
        mName.setOnTouchListener(mTouchListener);
        mSurname.setOnTouchListener(mTouchListener);
        mEmail.setOnTouchListener(mTouchListener);

    }

    //Helper method to populate Views in Edit existing contact mode.
    // @param contactsEntry the ContactsEntry to populate the UI
    private void populateUI(ContactsEntry contactsEntry) {
        if (contactsEntry == null) return;
        mName.setText(contactsEntry.getName());
        mSurname.setText(contactsEntry.getSurname());
        mEmail.setText(contactsEntry.getEmail());
    }

    //Saving Contact into the Data base
    public void saveContact() {
        final String name, surname, email;
        name = mName.getText().toString();
        surname = mSurname.getText().toString();
        email = mEmail.getText().toString();
        final ContactsEntry contactsEntry = new ContactsEntry(name, surname, email);
        // Insert or update contact if edit text is not empty
        if (checkEditText()) {
            ContactExecutor.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    //Insert contact
                    if (mContactID == DEFAULT_CONTACT_ID)
                        mContactsDb.contactsDao().insertContact(contactsEntry);
                    else {
                        //Update contact
                        contactsEntry.setId(mContactID);
                        mContactsDb.contactsDao().updateContact(contactsEntry);
                    }
                }
            });
            Toast.makeText(getApplicationContext(), "Contact Saved", Toast.LENGTH_LONG).show();
            finish();
        } else
            Toast.makeText(getApplicationContext(), R.string.ui_warning_empty_contact, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_CONTACT_ID, mContactID);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editcontact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_menu_item) {
            saveContact();
        }
        if(id == android.R.id.home){
            if(mContactHasChanged){
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditContactActivity.this);
                            }
                        };
            showUnsavedChangesDialog(discardButtonClickListener);
            return true;}

        }
        return super.onOptionsItemSelected(item);
    }

    //Check EditText views not to be empty
    private boolean checkEditText() {
        String name,surname,email;
        name = mName.getText().toString();
        surname = mSurname.getText().toString();
        email = mEmail.getText().toString();
        if (!name.matches("") || !surname.matches("") || !email.matches("")) return true;
        else return false;
    }

    //User warning
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.ui_warning_no_save);
        builder.setNegativeButton(R.string.ui_warning_stay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setPositiveButton(R.string.ui_warning_leave,discardButtonClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (mContactHasChanged) {
            DialogInterface.OnClickListener discardButtonClickListener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    };
            showUnsavedChangesDialog(discardButtonClickListener);
        } else super.onBackPressed();
    }

}
