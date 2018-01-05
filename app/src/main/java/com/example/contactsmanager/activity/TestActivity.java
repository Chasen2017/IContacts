package com.example.contactsmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.contactsmanager.R;
import com.example.contactsmanager.bean.Contact;
import com.example.contactsmanager.bean.Person;
import com.example.contactsmanager.db.ContactsDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus-pc on 2018/1/5.
 */

public class TestActivity extends AppCompatActivity {

    ContactsDB contactsDB;


    public static void show(Context context) {
        context.startActivity(new Intent(context, TestActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        contactsDB = ContactsDB.getInstance(this);
    }

    @OnClick(R.id.btn_add)
    void onInsert() {
        /*contactsDB.insertGroup(new Group("朋友"));
        contactsDB.insertGroup(new Group("朋友啊"));
        contactsDB.insertGroup(new Group("啊朋友"));*/
    /*    contactsDB.insertPerson(new Person("A", "男", "广州市番禺区", "老师", 746973769,
                "746973769", "746973769@qq.com", 1));*/
        Contact contact1 = new Contact("15626160969", 1);
        Contact contact2 = new Contact("13288066351", 2);
        Contact contact3 = new Contact("15992261507", 2);
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contact2);
        contactList.add(contact3);
        contactsDB.insertContact(contact1);
        contactsDB.insertContactList(contactList);
    }

    @OnClick(R.id.btn_update)
    void onUpdate() {
        //contactsDB.updateGroup(1, "家人");
      /*  Person person = new Person("A", "女", "汕头市龙湖区", "学生", 746973769,
                "746973769", "746973769@qq.com", 5);
        person.setPid(1);
        contactsDB.updatePerson(person);*/
        Contact contact2 = new Contact("12345678901", 2);
        Contact contact3 = new Contact("9876543210.", 2);
        List<Contact> contactList = new ArrayList<>();
        contactList.add(contact2);
        contactList.add(contact3);
        contactsDB.updateContacts(contactList);
    }

    @OnClick(R.id.btn_query)
    void onQuery() {
        //Log.d("Tag", contactsDB.queryGroups().toString());
      /*  Log.d("Tag", contactsDB.vagueQueryGroups("朋友").toString());
        Log.d("Tag", contactsDB.vagueQueryGroups("啊朋友").toString());
        Log.d("Tag", contactsDB.vagueQueryGroups("啊啊朋友").toString());*/
       /* Log.d("Tag", contactsDB.queryGidByGname("朋友啊")+"");*/
/*        Log.d("Tag", "按照pid查询："+contactsDB.queryPersonByPid(1).toString());
        Log.d("Tag", "按照姓名查询："+contactsDB.queryPersonByName("A").toString());
        Log.d("Tag", "全部查询："+contactsDB.queryAllPerson().toString());
        Log.d("Tag", "查询对应的分组："+contactsDB.queryGnameByGid(5).toString());
        Log.d("Tag", "姓名找pid，然后pid设置给联系方式："+contactsDB.queryPidByPname("A")+"");*/
        Log.d("Tag", "查询对应的分组："+contactsDB.queryContacts(2).toString());
    }

    @OnClick(R.id.btn_del)
    void onDel() {
        //contactsDB.delGroup(1);
        //contactsDB.delPerson(1);
        contactsDB.delContactsByPid(2);
    }
}
