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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chsen on 2018/1/6.
 * <p>
 * 添加联系人的Activity，包括添加联系人的基本信息和联系方式
 */

public class AddPersonActivity extends AppCompatActivity {

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

    /**
     * AddPersonActivity的入口方法
     *
     * @param context 上下文，从哪里跳转过来的
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AddPersonActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mAdapter = new AddPhoneAdapter();
        mPhoneRecycler.setAdapter(mAdapter);
        mPhoneRecycler.setLayoutManager(new LinearLayoutManager(this));
        mContactsDB = ContactsDB.getInstance(this);
    }

    /**
     * 点击x 按钮
     */
    @OnClick(R.id.im_cancel)
    void onCancelClick() {
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
        Person person = new Person();
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
            if (email.matches("^[\\\\w-]+(\\\\.[\\\\w-]+)*@[\\\\w-]+(\\\\.[\\\\w-]+)+$")) {
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
        mContactsDB.insertPerson(person);
        pid = mContactsDB.queryPidByPname(person.getPname());
        // 遍历号码，插入到TB_CONTACT
        ArrayList<String> phoneList = (ArrayList<String>) mAdapter.getPhoneList();
        for (String phone : phoneList) {
            if (!"".equals(phone)) {
                Contact contact = new Contact(phone, pid);
                mContactsDB.insertContact(contact);
            }
        }
        //post一个事件到PersonFragment
        EventBus.getDefault().post(person);
        Utils.showToast(R.string.add_success);
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

}
