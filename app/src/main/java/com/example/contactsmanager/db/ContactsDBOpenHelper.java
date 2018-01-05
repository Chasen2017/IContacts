package com.example.contactsmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 数据库帮助类
 */

public class ContactsDBOpenHelper extends SQLiteOpenHelper {

    // 创建联系人表语句
    private static final String CREATE_CONTACTS = "create table TB_CONTACTS (" +
            "cid integer primary key autoincrement," +
            "phone text," +
            "pid int)";
    // 创建个人基本信息表语句
    private static final String CREATE_PERSON = "create table TB_PERSON(" +
            "pid integer primary key autoincrement," +
            "pname text," +
            "gender text," +
            "address text," +
            "job text," +
            "e_mail text," +
            "QQ bigint," +
            "wechat text," +
            "gid int)";
    // 创建群组表语句
    private static final String CREATE_GROUP = "create table TB_GROUP(" +
            "gid integer primary key autoincrement, " +
            "gname text not null)";
    // 在基本信息表上建立姓名的索引
    private static final String CREATE_PERSON_INDEX = "create index name_index on TB_PERSON(pname)";
    // 在联系方式表上建立pid的索引
    private static final String CREATE_CONTACTS_INDEX = "create index pid_index on TB_CONTACTS(pid)";


    private Context mContext;
    public ContactsDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GROUP);
        db.execSQL(CREATE_PERSON);
        db.execSQL(CREATE_CONTACTS);
        db.execSQL(CREATE_PERSON_INDEX);
        db.execSQL(CREATE_CONTACTS_INDEX);
        Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists TB_CONTACTS");
        db.execSQL("drop table if exists TB_PERSON");
        db.execSQL("drop table if exists TB_GROUP");
        onCreate(db);
    }
}
