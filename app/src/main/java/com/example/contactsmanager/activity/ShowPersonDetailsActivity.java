package com.example.contactsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contactsmanager.R;
import com.example.contactsmanager.adapter.AddPhoneAdapter;
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

    @BindView(R.id.txt_portrait)
    TextView portraitTv;
    @BindView(R.id.edit_name)
    EditText nameEt;
    @BindView(R.id.edit_gender)
    EditText genderEt;
    @BindView(R.id.edit_job)
    EditText jobEt;
    @BindView(R.id.edit_address)
    EditText addressEt;
    @BindView(R.id.edit_QQ)
    EditText QQEt;
    @BindView(R.id.edit_wechat)
    EditText wechatEt;
    @BindView(R.id.edit_email)
    EditText emailEt;
    @BindView(R.id.lay_group)
    LinearLayout layout;
    @BindView(R.id.txt_group_name)
    TextView groupTv;
    @BindView(R.id.recycler_phone)
    RecyclerView mPhoneRecycler;
    @BindView(R.id.im_edit)
    ImageView editIm;

    private ShowPhoneAdapter mAdapter;
    private ContactsDB mContactsDB;
    private boolean isEdit = false; // 用于判断是显示详细信息或编辑
    private List<Contact> contactList;
    Person person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        ButterKnife.bind(this);
        init();

    }

    // 初始化信息
    private void init() {
        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra("person");
        //  Log.d("TAG", person.toString());
        contactList = mContactsDB.queryContacts(person.getPid());
        person.setContactList((ArrayList<Contact>) contactList);
        if (contactList.size() == 0) {
            mAdapter = new ShowPhoneAdapter();
        } else {
            ArrayList<String> list = new ArrayList<>();
            for (Contact contact : contactList) {
                list.add(contact.getPhone());
            }
            mAdapter = new ShowPhoneAdapter(list);
        }
        mPhoneRecycler.setAdapter(mAdapter);
        mPhoneRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    // TODO 使控件可选和不可选
    private void editUnable() {
        nameEt.setEnabled(false);
        genderEt.setEnabled(false);
        jobEt.setEnabled(false);

    }


    @OnClick(R.id.im_back)
    void back() {
        finish();
    }

    @OnClick(R.id.im_edit)
    void edit() {
        // TODO 非编辑模式下，显示详细信息
        if (!isEdit) {

        }
        // TODO 编辑模式下，编辑联系人信息，并更新到数据库
        else {

        }
    }

}
