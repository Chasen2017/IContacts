package com.example.contactsmanager.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 联系人基本信息类
 * Created by asus-pc on 2017/11/30.
 */

public class Person implements Comparable<Person>, Serializable {

    private int pid; //ID
    private String pname; //姓名
    private String gender; //性别，男、女、空
    private String address; // 住址
    private String job; // 工作
    private long QQ; //QQ
    private String wechat; // 微信
    private String email; //邮箱
    private ArrayList<Contact> contactList;  //联系人的联系方式集合
    private int gid;  // 逻辑外键，群组分配的

    /**
     * 无参的构造
     */
    public Person() {
        this.contactList = new ArrayList<>();
    }

    /**
     * 带参的构造方法，指定群组编号的情况
     *
     * @param pname   姓名
     * @param gender  性别
     * @param address 住址
     * @param job     工作
     * @param QQ      QQ
     * @param wechat  微信
     * @param email   邮箱
     * @param gid     群组外键
     */
    public Person(String pname, String gender, String address, String job, long QQ, String wechat, String email, int gid) {
        this(pname, gender, address, job, QQ, wechat, email);
        this.gid = gid;
    }

    /**
     * 带参的构造方法，不指定群组编号的情况
     *
     * @param pname   姓名
     * @param gender  性别
     * @param address 住址
     * @param job     工作
     * @param QQ      QQ
     * @param wechat  微信
     * @param email   邮箱
     */
    public Person(String pname, String gender, String address, String job, long QQ, String wechat, String email) {
        this.pname = pname;
        this.gender = gender;
        this.address = address;
        this.job = job;
        this.QQ = QQ;
        this.wechat = wechat;
        this.email = email;
        this.contactList = new ArrayList<>();
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getQQ() {
        return QQ;
    }

    public void setQQ(long QQ) {
        this.QQ = QQ;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    @Override
    public String toString() {
        return "Person{" +
                "pid=" + pid +
                ", pname='" + pname + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", job='" + job + '\'' +
                ", QQ=" + QQ +
                ", wechat='" + wechat + '\'' +
                ", email='" + email + '\'' +
                ", contactList=" + contactList +
                ", gid=" + gid +
                '}';
    }

    @Override
    public int compareTo(@NonNull Person o) {
        Collator instance = Collator.getInstance(Locale.CHINA);
        return instance.compare(this.pname, o.pname);
    }




}
