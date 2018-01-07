package com.example.contactsmanager.frags;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.contactsmanager.R;
import com.example.contactsmanager.activity.ShowPersonDetailsActivity;
import com.example.contactsmanager.adapter.PersonAdapter;
import com.example.contactsmanager.bean.Contact;
import com.example.contactsmanager.bean.Person;
import com.example.contactsmanager.db.ContactsDB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 联系人的Fragment
 */
public class PersonFragment extends Fragment implements PersonAdapter.OnItemClickListener{

    @BindView(R.id.recycle_person)
    RecyclerView mRecycler;

    private List<Person> mPersonList;
    private PersonAdapter mAdapter;
    private Unbinder unbinder;
    private ContactsDB mContactsDb;

    public PersonFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        mPersonList = new ArrayList<>();
        mContactsDb = ContactsDB.getInstance(getContext());
        mPersonList = mContactsDb.queryAllPerson();
        Collections.sort(mPersonList);
        mAdapter = new PersonAdapter(mPersonList);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * 添加联系人并刷新界面
     * @param person
     */
    @Subscribe
    public void addPerson(Person person) {
        replace(mPersonList);
    }


    /**
     * 替换从数据库查询出来的新数据
     * @param personList List<Person>
     */
    private void replace(List<Person> personList) {
        personList.clear();
        List<Person> persons = mContactsDb.queryAllPerson();
        Collections.sort(persons);
        personList.addAll(persons);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 点击后显示详细信息,跳转到显示详细信息的Activity
     * @param person Person
     * @param position 坐标
     */
    @Override
    public void onItemClick(Person person, int position) {
        Intent intent = new Intent(getActivity(), ShowPersonDetailsActivity.class);
        intent.putExtra("person", person);
        getActivity().startActivity(intent);
    }

    /**
     * 长按事件， 删除
     * @param person
     * @param position
     */
    @Override
    public void onItemLongClick(Person person, int position) {
        delPerson(person);
    }

    /**
     * 删除联系人信息
     * @param person
     */
    private void delPerson(final Person person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = new String[]{"删除"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除
                if (which == 0) {
                    mContactsDb.delPerson(person.getPid());
                    replace(mPersonList);
                }
            }
        }).create().show();
    }
}
