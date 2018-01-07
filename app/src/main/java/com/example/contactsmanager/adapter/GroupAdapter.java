package com.example.contactsmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contactsmanager.R;
import com.example.contactsmanager.bean.Group;

import java.util.List;


/**
 * Created by Chsen on 2018/1/6.
 * 群组列表的适配器
 */

public class GroupAdapter extends RecyclerView.Adapter {

    private List<Group> mGroupList;
    private OnItemClickListener mListener;

    public GroupAdapter(List<Group> groupList) {
        this.mGroupList = groupList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_group_item, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Group group = mGroupList.get(position);
        GroupViewHolder viewHolder = (GroupViewHolder) holder;
        viewHolder.gnameTv.setText(group.getGroupName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(group, position);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onItemLongClick(group, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{

        TextView gnameTv;

        public GroupViewHolder(View itemView) {
            super(itemView);
            gnameTv = itemView.findViewById(R.id.txt_group_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Group group, int position);

        void onItemLongClick(Group group, int position);
    }

}
