package com.example.contactsmanager.bean;

/**
 * 联系方式表
 * Created by asus-pc on 2017/11/30.
 */

public class Contact {

    private int cid; //ID
    private String phone; //手机号码

    private int pid; //联系人基本信息外键


    // 无参的构造
    public Contact() {}

    // 带参的构造
    public Contact(String phone,int pid) {
        this.phone = phone;
        this.pid = pid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "cid=" + cid +
                ", phone='" + phone + '\'' +
                ", pid=" + pid +
                '}';
    }
}
