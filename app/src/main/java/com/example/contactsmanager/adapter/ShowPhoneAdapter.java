package com.example.contactsmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chsen on 2018/1/7.
 *
 * //TODO 显示联系人详细信息中，号码列表的适配器，对外提供使控件不可点击的方法和可点击的方法
 * // TODO cell 的view还没写
 *
 */

public class ShowPhoneAdapter extends RecyclerView.Adapter {

    private List<String> mPhoneList;

    public ShowPhoneAdapter() {
        mPhoneList = new ArrayList<>();
        mPhoneList.add("");
    }

    public ShowPhoneAdapter(List<String> phoneList) {
        this.mPhoneList = phoneList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
