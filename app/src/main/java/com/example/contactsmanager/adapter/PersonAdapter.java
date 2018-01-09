package com.example.contactsmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contactsmanager.R;
import com.example.contactsmanager.bean.Person;
import com.example.contactsmanager.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chsen on 2018/1/6.
 * <p>
 * 联系人展示列表的适配器
 */

public class PersonAdapter extends RecyclerView.Adapter {

    private List<Person> mPersonList;
    private OnItemClickListener mListener;

    public PersonAdapter(List<Person> personList) {
        this.mPersonList = personList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Person person = mPersonList.get(position);
        ContactViewHolder viewHolder = (ContactViewHolder) holder;
        if (TextUtils.isEmpty(person.getPname())) {
            viewHolder.portraitTv.setText("-");
            viewHolder.nameTv.setText("-");
        } else {
            viewHolder.portraitTv.setText(Utils.getFinalName(person.getPname()));
            viewHolder.nameTv.setText(person.getPname());
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(person, position);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onItemLongClick(person, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

    /**
     * 联系人列表的ViewHolder
     */
    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView portraitTv;
        TextView nameTv;

        public ContactViewHolder(View itemView) {
            super(itemView);
            portraitTv = itemView.findViewById(R.id.txt_portrait);
            nameTv = itemView.findViewById(R.id.txt_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Person person, int position);

        void onItemLongClick(Person person, int position);
    }

}
