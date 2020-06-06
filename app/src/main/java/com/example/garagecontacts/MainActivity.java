package com.example.garagecontacts;

import android.content.Intent;
import android.os.Bundle;

import com.example.garagecontacts.data.AddContactViewModel;
import com.example.garagecontacts.data.ContactsDB;
import com.example.garagecontacts.data.ContactsDao;
import com.example.garagecontacts.data.ContactsEntry;
import com.example.garagecontacts.data.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

/**
 * Displays list of contacts that were entered and stored in the app.
 */

public class MainActivity extends AppCompatActivity implements ContactAdapter.ItemClickListener {
    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private ContactsDB mContactsDB;

    //Set observer to notify the ContactAdapter
    Observer<List<ContactsEntry>> observer = new Observer<List<ContactsEntry>>() {

        @Override
        public void onChanged(List<ContactsEntry> contactEntries) {
            if (contactEntries.size() == 0) {
                insertDummyData();
            }
            mAdapter.setContacts(contactEntries);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditContactActivity.class);
                startActivity(intent);
            }
        });

        // Set the RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new ContactAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        //Get instance of the data base and get data form it
        mContactsDB = ContactsDB.getInstance(this);
        retrieveData();

        // Delete contact when a user swipes left or right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                ContactExecutor.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<ContactsEntry> contactEntry = mAdapter.getmContactsEntries();
                        mContactsDB.contactsDao().deleteContact(contactEntry.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            ContactExecutor.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mContactsDB.contactsDao().deleteAll();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(this, EditContactActivity.class);
        // Launch EditContactActivity adding the itemId as an extra in the intent
        intent.putExtra(EditContactActivity.EXTRA_CONTACT_ID, itemId);
        startActivity(intent);

    }

    //Helper method to get Data out of the Data base.
    private void retrieveData() {
        MainViewModel model = ViewModelProviders.of(this).get(MainViewModel.class);
        model.getLiveData().observe(this, observer);
    }

    private void insertDummyData() {
        final List<ContactsEntry> contactsEntryList = new ArrayList<ContactsEntry>();
        contactsEntryList.add(new ContactsEntry("Мухаммед", "", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Исаак", "Ньютон", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Будда", "Будда Шакьямуни", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Конфуций", "", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Павел", "Апостол", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Цай", "Лунь", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Иоганн", "Гутенберг", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Христофор", "Колумб", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Альберт", "Эйнштейн", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Луи", "Пастер", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Галилей", "Галилео", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Аристотель", "", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Евклид", "", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Моисей", "", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Чарльз", "Дарвин", "pigeonmail@sad.true"));
        contactsEntryList.add(new ContactsEntry("Цинь", "Шихуанди", "pigeonmail@sad.true"));

        ContactExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (ContactsEntry contactsEntry : contactsEntryList) {
                    mContactsDB.contactsDao().insertContact(contactsEntry);
                }
            }
        });
    }
}
