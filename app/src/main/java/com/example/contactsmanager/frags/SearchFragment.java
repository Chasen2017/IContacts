package com.example.contactsmanager.frags;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contactsmanager.R;
import com.example.contactsmanager.activity.SearchActivity;

/**
 * 查找联系人的
 */
public class SearchFragment extends Fragment implements SearchActivity.SearchInterface {


    public SearchFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void search(String content) {
        Log.d("TAG", "search()方法调用了");

    }
}
