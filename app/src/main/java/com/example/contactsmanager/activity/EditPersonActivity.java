package com.example.contactsmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contactsmanager.R;
import com.example.contactsmanager.adapter.AddPhoneAdapter;
import com.example.contactsmanager.adapter.GroupAdapter;
import com.example.contactsmanager.bean.Contact;
import com.example.contactsmanager.bean.Group;
import com.example.contactsmanager.bean.Person;
import com.example.contactsmanager.db.ContactsDB;
import com.example.contactsmanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chsen on 2018/1/9.
 *
 * 编辑联系人信息
 */

public class EditPersonActivity extends AppCompatActivity {

    @BindView(R.id.txt_title)
    TextView titleTv;
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
    @BindView(R.id.im_finished)
    ImageView submitBtn;

    private AddPhoneAdapter mAdapter;
    private ContactsDB mContactsDB;
    private Person person;
    private List<Contact> mContactList;
    private List<String> mPhoneList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        titleTv.setText(getString(R.string.edit_contact));
        mContactsDB = ContactsDB.getInstance(this);
        person = (Person) getIntent().getSerializableExtra("person");
        mContactList = new ArrayList<>();
        mPhoneList = new ArrayList<>();
        mContactList = mContactsDB.queryContacts(person.getPid());
        if (mContactList.size() != 0) {
            for (Contact c : mContactList) {
                mPhoneList.add(c.getPhone());
            }
            mAdapter = new AddPhoneAdapter(mPhoneList);
        } else {
            mAdapter = new AddPhoneAdapter();
        }
        mPhoneRecycler.setAdapter(mAdapter);
        mPhoneRecycler.setLayoutManager(new LinearLayoutManager(this));
        showPerson();
    }

    // 显示联系人的信息
    private void showPerson() {
        if (!TextUtils.isEmpty(person.getPname())) {
            portraitTv.setText(Utils.getFinalName(person.getPname()));
        }
        nameEt.setText(person.getPname()+"");
        genderEt.setText(person.getGender()+"");
        addressEt.setText(person.getAddress()+"");
        jobEt.setText(person.getJob()+"");
        if (person.getQQ() == 0) {
            QQEt.setText("");
        } else {
            QQEt.setText(person.getQQ()+"");
        }
        wechatEt.setText(person.getWechat()+"");
        if (!TextUtils.isEmpty(person.getEmail())) {
            emailEt.setText(person.getEmail()+"");
        } else {
            emailEt.setText("");
        }

        if (person.getGid() != 0) {
            String groupName = mContactsDB.queryGnameByGid(person.getPid());
            groupTv.setText(groupName);
        }
    }

    /**
     * 点击x 按钮
     */
    @OnClick(R.id.im_cancel)
    void onCancelClick() {
        Intent intent = new Intent();
        intent.putExtra("person", person);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 点击提交按钮
     */
    @OnClick(R.id.im_finished)
    void onSubmit() {
        // 抢占焦点，使EditText失去焦点
        submitBtn.setFocusable(true);
        submitBtn.setFocusableInTouchMode(true);
        submitBtn.requestFocus();
        submitBtn.requestFocusFromTouch();
        // person类赋值校验
        String pname = nameEt.getText().toString().trim();
        String gender = genderEt.getText().toString().trim();
        String address = addressEt.getText().toString().trim();
        String job = jobEt.getText().toString().trim();
        long QQ;
        String wechat = wechatEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        int gid;
        int pid;
        String groupName = groupTv.getText().toString();
        person.setPname(pname);
        person.setAddress(address);
        person.setJob(job);
        person.setWechat(wechat);
        if (!TextUtils.isEmpty(gender)) {
            if (gender.equals("男") || gender.equals("女") ||
                    gender.toLowerCase().equals("man") || gender.toLowerCase().equals("women")) {
                person.setGender(gender);
            } else {
                Utils.showToast(R.string.error_gender);
                return;
            }
        }
        if (!"".equals(QQEt.getText().toString().trim())) {
            if (QQEt.getText().toString().trim().matches("^[0-9]*[1-9][0-9]*$")) {
                QQ = Long.parseLong(QQEt.getText().toString().trim());
                person.setQQ(QQ);
            } else {
                Utils.showToast(R.string.error_QQ);
                return;
            }
        }
        if (!TextUtils.isEmpty(email)) {
            if (email.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$")) {
                person.setEmail(email);
            } else {
                Utils.showToast(R.string.error_email);
                return;
            }
        }
        if (!getString(R.string.no_group_name).equals(groupName)) {
            gid = mContactsDB.queryGidByGname(groupName);
            if (gid != -1) {
                person.setGid(gid);
            }
        }
        // 插入到TB_PERSON
        mContactsDB.updatePerson(person);
        // 删除TB_CONTACT原来有的号码
        mContactsDB.delContactsByPid(person.getPid());
        pid = person.getPid();
        // 遍历号码，插入到TB_CONTACT
        ArrayList<String> phoneList = (ArrayList<String>) mAdapter.getPhoneList();
        if (phoneList.size() != 0) {
            for (String phone : phoneList) {
                if (!"".equals(phone)) {
                    Contact contact = new Contact(phone, pid);
                    mContactsDB.insertContact(contact);
                }
            }
        }
        Intent intent = new Intent();
        intent.putExtra("person", person);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 选择对应的群组
     */
    @OnClick(R.id.lay_group)
    void chooseGroup() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_show_group, null);
        RecyclerView recycler = view.findViewById(R.id.recycler_choose_group);
        final List<Group> mGroupList = mContactsDB.queryGroups();
        GroupAdapter adapter = new GroupAdapter(mGroupList);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Group group, int position) {
                groupTv.setText(group.getGroupName());
                dialog.dismiss();
            }
            @Override
            public void onItemLongClick(Group group, int position) {

            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        dialog.setContentView(view);
        dialog.setTitle(getString(R.string.choose_group));
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onCancelClick();
    }
}
