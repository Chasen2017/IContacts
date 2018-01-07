package com.example.contactsmanager.frags;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.contactsmanager.R;
import com.example.contactsmanager.activity.EnterGroupActivity;
import com.example.contactsmanager.adapter.GroupAdapter;
import com.example.contactsmanager.bean.Group;
import com.example.contactsmanager.db.ContactsDB;
import com.example.contactsmanager.utils.Utils;

import net.qiujuer.genius.ui.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 群组的Fragment
 */
public class GroupFragment extends Fragment implements GroupAdapter.OnItemClickListener {

    @BindView(R.id.recycler_group)
    RecyclerView mRecycler;

    private Unbinder mUnbinder;
    private GroupAdapter mAdapter;
    private List<Group> mGroupList;
    private ContactsDB mContactsDb;

    public GroupFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        mGroupList = new ArrayList<>();
        mContactsDb = ContactsDB.getInstance(getContext());
        mGroupList = mContactsDb.queryGroups();
        Collections.sort(mGroupList);
        mAdapter = new GroupAdapter(mGroupList);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onItemClick(Group group, int position) {
        Intent intent = new Intent(getActivity(), EnterGroupActivity.class);
        intent.putExtra("group_name", group.getGroupName());
        intent.putExtra("group_id", group.getGid());
        getActivity().startActivity(intent);
    }

    /**
     * 长按事件，重命名、删除
     *
     * @param group    Group
     * @param position 下标
     */
    @Override
    public void onItemLongClick(final Group group, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = new String[]{"重命名", "删除"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {  // 重命名
                    renameDialog(group);
                } else if (which == 1) { // 删除
                    mContactsDb.delGroup(group.getGid());
                    replace(mGroupList);
                }
            }
        }).create().show();
    }

    /**
     * 群组重命名
     */
    private void renameDialog(final Group group) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_add_group, null);
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
                } else if (mContactsDb.isGroupExists(groupName)) {
                    Utils.showToast(R.string.error_group_name_exists);
                    groupNameEt.setText("");
                    return;
                }
                // 重新加载群组数据并进行界面刷新
                mContactsDb.updateGroup(group.getGid(), groupName);
                replace(mGroupList);
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
     * 添加群组
     *
     * @param group Group
     */
    @Subscribe
    public void addGroup(Group group) {
        mContactsDb.insertGroup(group);
        replace(mGroupList);
    }

    /**
     * 替换从数据库查询出来的新数据
     *
     * @param groupList
     */
    private void replace(List<Group> groupList) {
        groupList.clear();
        List<Group> groups = mContactsDb.queryGroups();
        Collections.sort(groups);
        groupList.addAll(groups);
        mAdapter.notifyDataSetChanged();
    }
}
