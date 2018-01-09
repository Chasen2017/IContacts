package com.example.contactsmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
 * 显示联系人详细信息中，号码列表的适配器
 */

public class ShowPhoneAdapter extends RecyclerView.Adapter {

    private List<String> mPhoneList;
    private Context mContext;

    public ShowPhoneAdapter() {
        this.mPhoneList = new ArrayList<>();
    }

    public ShowPhoneAdapter(List<String> phoneList) {
        this.mPhoneList = phoneList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_phone_show_detail, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (mPhoneList.size() == 0) {
            return;
        } else {
            final String s = mPhoneList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.phoneEt.setText(s);
            viewHolder.phoneEt.setEnabled(false); // 显示号码，设置为不可点击
            viewHolder.callIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到拨打号码的界面
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+s));
                    mContext.startActivity(intent);

                }
            });
            viewHolder.messageIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到发送信息的界面
                   Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+s));
                    mContext.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mPhoneList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView callIm;
        ImageView messageIm;
        EditText phoneEt;

        public ViewHolder(View itemView) {
            super(itemView);
            callIm = itemView.findViewById(R.id.im_call);
            messageIm = itemView.findViewById(R.id.im_message);
            phoneEt = itemView.findViewById(R.id.edit_phone);
        }
    }

}
