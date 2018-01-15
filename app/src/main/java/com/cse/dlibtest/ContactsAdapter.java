package com.cse.dlibtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sy081 on 2018-01-15.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private Context mContext;
    private ArrayList<AddressBook> mAddressLists;
    public ContactsAdapter(Context context, ArrayList<AddressBook> result){
        mContext = context;
        mAddressLists = result;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.address_item,parent,false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        AddressBook addressBook = mAddressLists.get(position);
        holder.mName.setText(addressBook.getName());
        holder.mPhoto.setImageBitmap(addressBook.getFace().getIcon());
    }

    @Override
    public int getItemCount() {
        return mAddressLists.size();
    }


    public class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName;
        ImageView mPhoto;


        public ContactsViewHolder(View itemView){
            super(itemView);
            mName = (TextView)itemView.findViewById(R.id.tv_name);
            mPhoto = (ImageView)itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            AddressBook addressBook = mAddressLists.get(index);
          /*  Intent i = new Intent(mContext, AttendanceActivity.class);
            i.putExtra(mContext.getString(R.string.extra_subject_code),subjectInfo.getSubjectCode());
            i.putExtra(mContext.getString(R.string.extra_subject_name),subjectInfo.getSubjectName());
            mContext.startActivity(i);*/
        }
    }
}
