package com.example.contactsmanager.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.contactsmanager.R;
import com.example.contactsmanager.bean.Group;
import com.example.contactsmanager.db.ContactsDB;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 利用web来调试数据库
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        ButterKnife.bind(this);
        ContactsDB contactsDB = ContactsDB.getInstance(MainActivity.this);
      /*  TestActivity.show(MainActivity.this);*/

    }

    @OnClick(R.id.im_search)
    void onSearchItemClick() {
        SearchActivity.show(this);
    }

    @OnClick(R.id.im_add)
    void onMoreItemClick() {
       // Log.d("TAG", "click");
        PopupMenu popupMenu = new PopupMenu(this,findViewById(R.id.im_add));
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        // 利用反射显示出图标
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
            mHelper.setForceShowIcon(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.show();
    }


}
