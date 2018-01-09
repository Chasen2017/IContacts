package com.example.contactsmanager.frags;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contactsmanager.R;
import com.example.contactsmanager.activity.SearchActivity;
import com.example.contactsmanager.activity.ShowPersonDetailsActivity;
import com.example.contactsmanager.adapter.PersonAdapter;
import com.example.contactsmanager.bean.Contact;
import com.example.contactsmanager.bean.Person;
import com.example.contactsmanager.db.ContactsDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 查找联系人的
 */
public class SearchFragment extends Fragment implements SearchActivity.SearchInterface, PersonAdapter.OnItemClickListener{

    @BindView(R.id.txt_empty)
    TextView mEmptyTv;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private PersonAdapter mAdapter;
    private List<Person> mPersonList;
    private ContactsDB mContactsDb;
    private Unbinder unbinder;

    public SearchFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        mPersonList = new ArrayList<>();
        mAdapter = new PersonAdapter(mPersonList);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactsDb = ContactsDB.getInstance(getContext());
        search(""); // 打开自动执行第一次搜索
    }

    @Override
    public void search(String content) {
        Log.d("TAG", "search()方法调用了");
        if (mPersonList.size() != 0) {
            mPersonList.clear();
        }
        mPersonList.addAll(mContactsDb.queryPersonByName(content));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(Person person, int position) {
        Intent intent = new Intent(getActivity(), ShowPersonDetailsActivity.class);
        intent.putExtra("person", person);
        getContext().startActivity(intent);
    }

    @Override
    public void onItemLongClick(Person person, int position) {

    }
}
