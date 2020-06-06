package com.example.garagecontacts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garagecontacts.data.ContactsDB;
import com.example.garagecontacts.data.ContactsEntry;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    // Сatch item clicks
    final private ItemClickListener mItemClickListener;

    //Data Holders

    private List<ContactsEntry> mContactsEntries;
    private Context mContext;

    public ContactAdapter(Context mContext, ItemClickListener listener){
        this.mContext = mContext;
        mItemClickListener = listener;
    }
    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
    ContactsEntry contactsEntry = mContactsEntries.get(position);
    //Setting the Contact name from ContactsEntry
    if(!contactsEntry.getName().matches(""))holder.name.setText(contactsEntry.getName());
    else if(!contactsEntry.getSurname().matches(""))holder.name.setText(contactsEntry.getSurname());
    else if(!contactsEntry.getEmail().matches(""))holder.name.setText(contactsEntry.getEmail());
    }

    @Override
    public int getItemCount() {
        if (mContactsEntries == null) {
            return 0;
        }
        return mContactsEntries.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView name;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_item_name);
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mContactsEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }

    public  List<ContactsEntry> getmContactsEntries(){
        return  mContactsEntries;
    }

    /**
     * This method updates the list of ContactEntries
     * and notifies the adapter to use the new values on it
     */
    public void setContacts(List<ContactsEntry> contactsEntries) {
        mContactsEntries = contactsEntries;
        notifyDataSetChanged();
    }

}
