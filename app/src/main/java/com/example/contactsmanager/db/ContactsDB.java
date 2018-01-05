package com.example.contactsmanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.contactsmanager.bean.Contact;
import com.example.contactsmanager.bean.Group;
import com.example.contactsmanager.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装常用的数据库操作
 */

public class ContactsDB {

    private static final String DB_NAME = "DB_CONTACTS";
    private static int VERSION = 2;
    private SQLiteDatabase db;
    private static ContactsDB mContactsDB;

    private ContactsDB(Context context) {
        ContactsDBOpenHelper dbHelper = new ContactsDBOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 单例模式，获取ContactsDB实例
     *
     * @param context 上下文
     * @return ContactsDB
     */
    public static ContactsDB getInstance(Context context) {
        if (mContactsDB == null) {
            return new ContactsDB(context);
        }
        return mContactsDB;
    }

    // 以下是对群组的操作

    /**
     * 保存群组信息到数据库
     *
     * @param group 群组
     */
    public void insertGroup(Group group) {
        db.execSQL("insert into TB_GROUP(gname) values(?)", new Object[]{group.getGroupName()});
    }

    /**
     * 根据群组的编号删除群组
     *
     * @param gid 群组的编号
     */
    public void delGroup(int gid) {
        db.execSQL("delete from TB_GROUP where gid=?", new Object[]{gid});
    }

    /**
     * 根据群组的编号gid，修改群组的名称
     *
     * @param gid   群组的编号
     * @param gname 群组的名称
     */
    public void updateGroup(int gid, String gname) {
        db.execSQL("update TB_GROUP set gname = ? where gid = ?", new Object[]{gname, gid});
    }

    /**
     * 查询所有的群组
     *
     * @return 群组列表
     */
    public List<Group> queryGroups() {
        List<Group> groups = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from TB_GROUP", null);
        while (cursor.moveToNext()) {
            Group group = new Group();
            group.setGid(cursor.getInt(cursor.getColumnIndex("gid")));
            group.setGroupName(cursor.getString(cursor.getColumnIndex("gname")));
            groups.add(group);
        }
        if (cursor != null) {
            cursor.close();
        }
        return groups;
    }

    /**
     * 模糊查询群组
     *
     * @return 群组列表
     */
    public List<Group> vagueQueryGroups(String gname) {
        List<Group> groups = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from TB_GROUP where gname like ?", new String[]{"%" + gname + "%"});
        while (cursor.moveToNext()) {
            Group group = new Group();
            group.setGid(cursor.getInt(cursor.getColumnIndex("gid")));
            group.setGroupName(cursor.getString(cursor.getColumnIndex("gname")));
            groups.add(group);
        }
        if (cursor != null) {
            cursor.close();
        }
        return groups;
    }

    /**
     * 根据群组名判断该群组是否已经存在了
     *
     * @param gname 群组名称
     * @return 存在返回true
     */
    public boolean isGroupExists(String gname) {
        Cursor cursor = db.rawQuery("select * from TB_GROUP where gname = ?", new String[]{gname});
        if (cursor.moveToNext()) {
            return true;
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    /**
     * 根据群组名找到对应的gid
     *
     * @param gname 群组名
     * @return 找到就返回群组编号，否则返回-1；
     */
    public int queryGidByGname(String gname) {
        Cursor cursor = db.rawQuery("select * from TB_GROUP where gname = ?", new String[]{gname});
        if (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex("gid"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return -1;
    }

    // 以下是对联系人基本信息的操作

    /**
     * 保存联系人的基本信息
     *
     * @param person 联系人的基本信息
     */
    public void insertPerson(Person person) {
        db.execSQL("insert into TB_PERSON(pname, gender, address, job, e_mail, QQ, wechat, gid)" +
                " values(?,?,?,?,?,?,?,?)", new Object[]{person.getPname(), person.getGender(),
                person.getAddress(), person.getJob(), person.getEmail(), person.getQQ(),
                person.getWechat(), person.getGid()});
    }

    /**
     * 根据联系人的编号删除联系人的基本信息
     *
     * @param pid 联系人的编号
     */
    public void delPerson(int pid) {
        db.execSQL("delete from TB_PERSON where pid = ?", new Object[]{pid});
        // 删除之后要删除联系人的联系方式
        delContactsByPid(pid);
    }

    /**
     * 修改联系人的基本信息
     *
     * @param person Person
     */
    public void updatePerson(Person person) {
        db.execSQL("update TB_PERSON set pname=?, gender=?, address=?, job=?, e_mail=?, QQ=?, " +
                "wechat=?, gid=? where pid=?", new Object[]{person.getPname(), person.getGender(),
                person.getAddress(), person.getJob(), person.getEmail(), person.getQQ(),
                person.getWechat(), person.getGid(), person.getPid()});
    }

    /**
     * 根据基本信息的编号查询联系人的基本信息
     *
     * @param pid 基本信息的编号
     */
    public Person queryPersonByPid(int pid) {
        Cursor cursor = db.rawQuery("select * from TB_PERSON where pid = ?", new String[]{pid + ""});
        Person person = null;
        while (cursor.moveToNext()) {
            person = new Person();
            person.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
            person.setPname(cursor.getString(cursor.getColumnIndex("pname")));
            person.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            person.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            person.setJob(cursor.getString(cursor.getColumnIndex("job")));
            person.setEmail(cursor.getString(cursor.getColumnIndex("e_mail")));
            person.setQQ(cursor.getLong(cursor.getColumnIndex("QQ")));
            person.setWechat(cursor.getString(cursor.getColumnIndex("wechat")));
            person.setGid(cursor.getInt(cursor.getColumnIndex("gid")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return person;
    }

    /**
     * 根据联系人的姓名拿到基本的信息
     * 可能有同名的情况，支持模糊搜索
     *
     * @param name 姓名
     * @return 联系人的基本信息列表
     */
    public List<Person> queryPersonByName(String name) {
        List<Person> personList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from TB_PERSON where pname like ?", new String[]{"%" + name + "%"});
        while (cursor.moveToNext()) {
            Person person = new Person();
            person.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
            person.setPname(cursor.getString(cursor.getColumnIndex("pname")));
            person.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            person.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            person.setJob(cursor.getString(cursor.getColumnIndex("job")));
            person.setEmail(cursor.getString(cursor.getColumnIndex("e_mail")));
            person.setQQ(cursor.getLong(cursor.getColumnIndex("QQ")));
            person.setWechat(cursor.getString(cursor.getColumnIndex("wechat")));
            person.setGid(cursor.getInt(cursor.getColumnIndex("gid")));
            personList.add(person);
        }
        if (cursor != null) {
            cursor.close();
        }
        return personList;
    }

    /**
     * 拿到全部人的基本的信息
     *
     * @return 全部人的联系人的基本信息
     */
    public List<Person> queryAllPerson() {
        List<Person> personList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from TB_PERSON", null);
        while (cursor.moveToNext()) {
            Person person = new Person();
            person.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
            person.setPname(cursor.getString(cursor.getColumnIndex("pname")));
            person.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            person.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            person.setJob(cursor.getString(cursor.getColumnIndex("job")));
            person.setEmail(cursor.getString(cursor.getColumnIndex("e_mail")));
            person.setQQ(cursor.getLong(cursor.getColumnIndex("QQ")));
            person.setWechat(cursor.getString(cursor.getColumnIndex("wechat")));
            person.setGid(cursor.getInt(cursor.getColumnIndex("gid")));
            personList.add(person);
        }
        if (cursor != null) {
            cursor.close();
        }
        return personList;
    }

    /**
     * 根据联系人的gid，找到联系人对应的分组名
     *
     * @param gid 基本信息表中的gid
     * @return 分组名
     */
    public String queryGnameByGid(int gid) {
        Cursor cursor = db.rawQuery("select gname from TB_GROUP where gid = ?", new String[]{gid + ""});
        String name = null;
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("gname"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return name;
    }

    /**
     * 根据姓名去查找pid
     * 利用pid去找到对应的联系方式
     *
     * @param name 姓名
     * @return pid 基本信息的编号
     */
    public int queryPidByPname(String name) {
        int pid = -1;
        Cursor cursor = db.rawQuery("select pid from TB_PERSON where pname = ?", new String[]{name});
        while (cursor.moveToNext()) {
            // 重名的情况下，// pid是自增的，选取最后一个即可
            pid = cursor.getInt(cursor.getColumnIndex("pid"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return pid;
    }


    // 以下是对联系人联系方式的操作

    /**
     * 保存联系人的联系方式
     *
     * @param contact 联系人的一些联系方式
     */
    public void insertContact(Contact contact) {
        db.execSQL("insert into TB_CONTACTS(phone, pid) values(?,?)", new Object[]{contact.getPhone(), contact.getPid()});
    }

    /**
     * 批量插入联系方式
     *
     * @param contactList List<Contact>
     */
    public void insertContactList(List<Contact> contactList) {
        for (Contact contact : contactList) {
            insertContact(contact);
        }
    }

    /**
     * 根据cid，删除单个的联系方式
     *
     * @param cid 联系方式编号
     */
  /*  public void delContactsByCid(int cid) {
        db.execSQL("delete from TB_CONTACTS where cid = ?", new Object[]{cid + ""});
    }*/

    /**
     * 根据pid，逻辑外键删除某个联系人的全部联系方式
     *
     * @param pid pid
     */
    public void delContactsByPid(int pid) {
        db.execSQL("delete from TB_CONTACTS where pid = ?", new Object[]{pid + ""});
    }

    /**
     * 更新一条联系方式
     *
     * @param contact 联系方式
     *//*
    public void updateContacts(Contact contact) {
        db.execSQL();
    }*/

    /**
     * 批量更新联系方式
     *
     * @param contactList List<Contact>
     */
    public void updateContacts(List<Contact> contactList) {
        // 先删除，再插入新的
        delContactsByPid(contactList.get(0).getPid());
        insertContactList(contactList);
    }

    /**
     * 根据联系人基本信息表中的pid，查询该联系人的所有联系方式
     *
     * @return List<Contact>
     */
    public List<Contact> queryContacts(int pid) {
        List<Contact> contactList = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from TB_CONTACTS where pid = ?", new String[]{pid + ""});
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.setCid(cursor.getInt(cursor.getColumnIndex("cid")));
            contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            contact.setPid(cursor.getInt(cursor.getColumnIndex("pid")));
            contactList.add(contact);
        }
        if (cursor != null) {
            cursor.close();
        }
        return contactList;
    }

}
