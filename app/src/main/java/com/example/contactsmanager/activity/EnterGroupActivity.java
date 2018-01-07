package com.example.contactsmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.contactsmanager.R;
import com.example.contactsmanager.adapter.PersonAdapter;
import com.example.contactsmanager.bean.Person;
import com.example.contactsmanager.db.ContactsDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chsen on 2018/1/6.
 *
 * 在群组列表，点击群组之后进入到此Activity，显示群组成员
 */

public class EnterGroupActivity extends AppCompatActivity implements PersonAdapter.OnItemClickListener{

    @BindView(R.id.recycle_person)
    RecyclerView mRecycler;

    private PersonAdapter mAdapter;
    private List<Person> mPersonList;
    private ContactsDB mContactsDB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_group);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        initToolbar((Toolbar) findViewById(R.id.toolbar));
        Intent intent = getIntent();
        String groupName = intent.getStringExtra("group_name");
        int gid = intent.getIntExtra("group_id", 0);
        setTitle(groupName);
        mPersonList = new ArrayList<>();
        mContactsDB = ContactsDB.getInstance(this);
        mPersonList = mContactsDB.queryPersonByGid(gid);
        mAdapter = new PersonAdapter(mPersonList);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    /**
     * 初始化ToolBar
     *
     * @param toolbar Toolbar
     */
    private void initToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        // 返回键的监听事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initTitleNeedBack();
    }

    /**
     * 设置左上角的返回按钮为实际的返回效果
     */
    private void initTitleNeedBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //         Log.d("TAG", "actionbar != null");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    // TODO 在群成员列表，点击事件的实现
    @Override
    public void onItemClick(Person person, int position) {

    }

    @Override
    public void onItemLongClick(Person person, int position) {

    }
}
