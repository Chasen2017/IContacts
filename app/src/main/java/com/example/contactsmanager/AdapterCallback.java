package com.example.contactsmanager;

/**
 * Created by asus-pc on 2017/11/17.
 */

public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);

}
