package com.example.contactsmanager.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;
import com.example.contactsmanager.activity.MyApplication;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * Created by Chsen on 2018/1/6.
 *
 * 封装常用的工具类
 */

public class Utils {

    /**
     * 显示一个Toast
     *
     * @param msg 字符串
     */
    public static void showToast(final String msg) {
        // Toast只能在主线程中显示，所以需要进行线程转换，保证一定是在主线程进行的show操作
        //Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();

        Run.onUiAsync(new Action() {
            // 这里进行回调的时候一定是主线程状态
            @Override
            public void call() {
                Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示一个Toast
     *
     * @param msgId 字符串资源
     */
    public static void showToast(@StringRes int msgId) {
        showToast(MyApplication.getContext().getString(msgId));
    }

    /**
     * 得到姓名的最后一个字符，将其设置为头像
     *
     * @param name 姓名
     * @return 返回姓名最后一个字符
     */
    public static String getFinalName(String name) {
        name = name.trim();
        if (name.length() == 1) {
            return name;
        }
        return name.substring(name.length() - 1, name.length());
    }
}
