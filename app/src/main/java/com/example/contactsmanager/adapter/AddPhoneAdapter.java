package com.example.contactsmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.contactsmanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chsen on 2018/1/7.
 * <p>
 * 添加联系人信息时，添加联系号码列表的适配器
 */

public class AddPhoneAdapter extends RecyclerView.Adapter<AddPhoneAdapter.ViewHolder> {

    private List<String> mPhoneList;

    public AddPhoneAdapter() {
        mPhoneList = new ArrayList<>();
        // 自动添加第一个
        mPhoneList.add("");
    }

    public AddPhoneAdapter(List<String> list) {
        this.mPhoneList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_phone_item, null);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ViewHolder viewHolder = holder;
        String phone = mPhoneList.get(position);
        //添加新的号码
        viewHolder.addIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneList.add("");
                notifyItemChanged(mPhoneList.size());
                Log.d("TAG", "add mPhoneList：" + mPhoneList.toString());
            }
        });
        // 删除号码
        viewHolder.removeIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保留一个
                if (mPhoneList.size() != 1) {
                    mPhoneList.remove(position);
                }
                Log.d("TAG", "remove mPhoneList：" + mPhoneList.toString());
                notifyDataSetChanged();
            }
        });
        viewHolder.phoneEt.setText(phone+"");
        // 定位到最后面
        if (viewHolder.phoneEt.getText() != null) {
            viewHolder.phoneEt.setSelection(viewHolder.phoneEt.getText().toString().length());
        }

        // 当重新获取焦点的时候，有可能是要改变号码，显示提交按钮
        viewHolder.phoneEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String phone = viewHolder.phoneEt.getText().toString().trim();
                if(!hasFocus) {
                    if (!TextUtils.isEmpty(phone) && !mPhoneList.contains(phone)) {
                        mPhoneList.set(position, phone);
                        Log.d("TAG", "finish onClick mPhoneList：" + mPhoneList.toString());
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPhoneList.size();
    }

    /**
     * 对外提供获取mPhoneList的方法
     *
     * @return mPhoneList
     */
    public List<String> getPhoneList() {
        return mPhoneList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView addIm;
        ImageView removeIm;
        EditText phoneEt;

        public ViewHolder(View itemView) {
            super(itemView);
            addIm = itemView.findViewById(R.id.im_add);
            removeIm = itemView.findViewById(R.id.im_remove);
            phoneEt = itemView.findViewById(R.id.edit_phone);
        }
    }
}
