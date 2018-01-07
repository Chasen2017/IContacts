package com.example.contactsmanager.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chsen on 2018/1/6.
 * <p>
 * activity集合，用于随时退出activity
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    /**
     * 添加activity
     *
     * @param activity Activity
     */
    public static void addActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    /**
     * 移除activity
     *
     * @param activity Activity
     */
    public static void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    /**
     * 移除其全部activitiy
     */
    public static void finishAll() {
        if (activities.size() == 0) {
            return;
        }
        for (Activity activity : activities) {
            activity.finish();
        }
    }

}
