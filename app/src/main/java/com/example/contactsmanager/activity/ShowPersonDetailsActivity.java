package com.example.contactsmanager.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.contactsmanager.R;
import com.example.contactsmanager.adapter.ShowPhoneAdapter;
import com.example.contactsmanager.bean.Contact;
import com.example.contactsmanager.bean.Person;
import com.example.contactsmanager.db.ContactsDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chsen on 2018/1/7.
 * <p>
 * 显示联系人的详细信息，可修改
 */

public class ShowPersonDetailsActivity extends AppCompatActivity {

    @BindView(R.id.recycler_phone)
    RecyclerView mRecycler;
    @BindView(R.id.txt_name)
    TextView nameTv;
    @BindView(R.id.txt_gender)
    TextView genderTv;
    @BindView(R.id.txt_job)
    TextView jobTv;
    @BindView(R.id.txt_address)
    TextView addressTv;
    @BindView(R.id.txt_QQ)
    TextView QQTv;
    @BindView(R.id.txt_wechat)
    TextView wechatTv;
    @BindView(R.id.txt_email)
    TextView emailTv;
    @BindView(R.id.txt_group)
    TextView groupTv;

    private Person person;
    private ContactsDB mContactsDB;
    private List<Contact> mContactList;
    private List<String> mPhoneList;
    private ShowPhoneAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        init();
    }

    // 初始化信息
    private void init() {
        ButterKnife.bind(this);
        person = (Person) getIntent().getSerializableExtra("person");
        mContactsDB = ContactsDB.getInstance(this);
        mContactList = new ArrayList<>();
        mPhoneList = new ArrayList<>();
        mContactList = mContactsDB.queryContacts(person.getPid());
        if (mContactList.size() != 0) {
            for (Contact c : mContactList) {
                mPhoneList.add(c.getPhone());
            }
        }
        mAdapter = new ShowPhoneAdapter(mPhoneList);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        showPerson(person);
    }

    // 显示联系人的信息
    private void showPerson(Person p) {
        if (TextUtils.isEmpty(p.getPname())) {
            nameTv.setText(getString(R.string.new_contact));
        } else {
            nameTv.setText(p.getPname());
        }
        if (TextUtils.isEmpty(p.getGender())) {
            genderTv.setVisibility(View.GONE);
        } else {
            genderTv.setVisibility(View.VISIBLE);
            genderTv.setText("性别："+p.getGender());
        }
        if (TextUtils.isEmpty(p.getJob())) {
            jobTv.setVisibility(View.GONE);
        } else {
            jobTv.setVisibility(View.VISIBLE);
            jobTv.setText("工作："+p.getJob());
        }

        if (TextUtils.isEmpty(p.getAddress())) {
            addressTv.setVisibility(View.GONE);
        } else {
            addressTv.setVisibility(View.VISIBLE);
            addressTv.setText("住址："+p.getAddress());
        }
        if (p.getQQ() == 0) {
            QQTv.setVisibility(View.GONE);
        } else {
            QQTv.setVisibility(View.VISIBLE);
            QQTv.setText("QQ："+p.getQQ());
        }
        if (TextUtils.isEmpty(p.getWechat())) {
            wechatTv.setVisibility(View.GONE);
        } else {
            wechatTv.setVisibility(View.VISIBLE);
            wechatTv.setText("微信："+p.getWechat());
        }
        if (TextUtils.isEmpty(p.getEmail())) {
            emailTv.setVisibility(View.GONE);
        } else {
            emailTv.setVisibility(View.VISIBLE);
            emailTv.setText("邮箱："+p.getEmail());
        }
        if (p.getGid() == 0) {
            groupTv.setVisibility(View.GONE);
        } else {
            groupTv.setVisibility(View.VISIBLE);
            groupTv.setText("群组："+ mContactsDB.queryGnameByGid(p.getGid()));
        }
    }

    @OnClick(R.id.im_edit)
    void editOnClick() {
        Intent intent = new Intent(this, EditPersonActivity.class);
        intent.putExtra("person", person);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.im_back)
    void onBackClick() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                person = (Person) data.getSerializableExtra("person");
                mContactList = mContactsDB.queryContacts(person.getPid());
                if (mContactList.size() != 0) {
                    if (mPhoneList.size() != 0) {
                        mPhoneList.clear();
                    }
                    for (Contact c : mContactList) {
                        mPhoneList.add(c.getPhone());
                    }
                }
                mAdapter.notifyDataSetChanged();
                showPerson(person);
                break;
            default:
                break;
        }
    }


}
