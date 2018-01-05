package com.example.contactsmanager.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.contactsmanager.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Chasen on 2018/1/5.
 * <p>
 * RecyclerViewAdapter的封装
 */

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
implements View.OnClickListener, View.OnLongClickListener{



    private final List<Data> mDataList = new ArrayList<Data>();


    /**
     * 创建一个ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型,约定为xml布局的Id
     * @return ViewHolder
     */
    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        // 得到LayoutInflater用于把XML初始化为View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 把XML id为ViewType的文件初始化为一个root view
        View root = inflater.inflate(viewType, parent, false);
        // 通过子类必须实现的方法，得到一个ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        // 设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        // 设置View的Tag为ViewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder);

        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);

        return null;
    }

    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到一个Holder上
     *
     * @param holder   ViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        // 得到需要绑定的数据
        Data data = mDataList.get(position);
        // 触发Holder的绑定方法
        holder.bind(data);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private Unbinder unbinder;
        protected Data mData;
        private AdapterCallback<Data> callback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据的时候的回调，必须复写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

        /**
         * Holder自己对自己的Data更新进行操作
         * @param data
         */
        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }

}
