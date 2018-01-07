package com.example.contactsmanager.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.contactsmanager.R;
import com.example.contactsmanager.bean.Group;
import com.example.contactsmanager.db.ContactsDB;
import com.example.contactsmanager.frags.GroupFragment;
import com.example.contactsmanager.frags.PersonFragment;
import com.example.contactsmanager.helper.NavHelper;
import com.example.contactsmanager.utils.Utils;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    private NavHelper<Integer> mNavHelper;
    private ContactsDB contactsDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        initWidget();
        // 利用web来调试数据库
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        contactsDB = ContactsDB.getInstance(MainActivity.this);
        //TestActivity.show(MainActivity.this);

    }

    private void initWidget() {
        // 添加底部导航栏监听
        mNavigation.setOnNavigationItemSelectedListener(this);
        // 初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_contact, new NavHelper.Tab<>(PersonFragment.class, R.string.action_contact))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.action_group));
        // 进行首次触发
        mNavigation.getMenu().performIdentifierAction(R.id.action_contact, 0);
    }

    /**
     * 导航栏搜索按钮点击事件
     */
    @OnClick(R.id.im_search)
    void onSearchItemClick() {
        SearchActivity.show(this);
    }

    /**
     * 导航栏“+”按钮点击事件
     */
    @OnClick(R.id.im_add)
    void onMoreItemClick() {
        // Log.d("TAG", "click");
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.im_add));
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add_contact:
                        addPerson();
                        break;
                    case R.id.menu_add_group:
                        addGroup();
                        break;
                    case R.id.menu_exit:
                        ActivityCollector.finishAll();
                        break;
                    default:
                        break;
                }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNavHelper.performClickMenu(item.getItemId());
    }

    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
    }

    /**
     * 添加群组
     */
    private void addGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_add_group, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        final EditText groupNameEt = view.findViewById(R.id.edit_group_name);
        Button confirmBtn = view.findViewById(R.id.btn_confirm);
        Button cancelBtn = view.findViewById(R.id.btn_cancel);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = groupNameEt.getText().toString().trim();
                if (TextUtils.isEmpty(groupName)) {
                    Utils.showToast(R.string.error_group_name_empty);
                    return;
                } else if (contactsDB.isGroupExists(groupName)) {
                    Utils.showToast(R.string.error_group_name_exists);
                    groupNameEt.setText("");
                    return;
                }
                // 通知重新加载群组数据并进行界面刷新
                EventBus.getDefault().post(new Group(groupName));
                Utils.showToast(R.string.insert_success);
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 添加联系人
     */
    private void addPerson() {
        AddPersonActivity.show(this);
    }
}
