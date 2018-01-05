package com.example.contactsmanager.bean;

/**
 *  群组信息类
 * Created by asus-pc on 2017/11/30.
 */

public class Group {
    private int gid; //ID
    private String groupName; //群组名称

    public Group(){}

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "gid=" + gid +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
