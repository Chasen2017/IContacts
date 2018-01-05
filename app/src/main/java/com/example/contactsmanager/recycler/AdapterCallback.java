package com.example.contactsmanager.recycler;

/**
 * Created by Chsen on 2018/1/5.
 *
 * 适配器的接口
 */

public interface AdapterCallback<Data> {

    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);

}
