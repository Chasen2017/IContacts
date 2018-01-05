package com.example.contactsmanager;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by asus-pc
 * on 2017/11/17.
 *
 * RecyclerView的封装
 */

public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {

    private final List<Data> mDataList;

    private AdapterListener<Data> mListener;

    /**
     * 构造函数模块
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }


    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，其实复写后返回的都是xml文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局文件的Id
     *
     * @param position 坐标
     * @param data     Data数据
     * @return 布局文件的id，用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建一个ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型,定为XML布局的Id
     * @return ViewHolder
     */
    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        //得到LayoutInflater用与吧XML初始化为View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //把 XML id为viewTye的文件初始化为root View
        View root = inflater.inflate(viewType, parent, false);
        //通过子类必须实现的方法，得到一个ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);
        //设置点击事件
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        //设置View的Tag为ViewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        //进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        //绑定callback
        holder.adapterCallback = this;

        return holder;
    }


    /**
     * 得到一个新的VIewHolder
     *
     * @param root     根布局
     * @param viewType 布局类型，XML的Id
     * @return ViewHolder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到一个Holder上
     *
     * @param holder   ViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        //得到需要绑定的数据
        Data data = mDataList.get(position);
        //触发Holder的绑定方法
        holder.bind(data);
    }

    /**
     * 得到当前集合的数据量
     *
     * @return 集合的数据量
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data Data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeChanged(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeChanged(startPos, dataList.size());
        }
    }

    /**
     * 删除操纵
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList 一个新的集合
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        // 得到当前ViewHolder的坐标
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
            // 进行数据的移除和更新
            mDataList.remove(pos);
            mDataList.add(pos, data);
            //  通知这个坐标下的数据有更新
            notifyItemChanged(pos);
        }
    }

    /**
     * ViewHolder点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            //得到viewHolder当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            //回调方法
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    /**
     * ViewHolder长按事件
     *
     * @param view
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            //得到viewHolder当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            //回调方法
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听
     *
     * @param adapterListener AdapterListener
     */
    public void setListener(AdapterListener<Data> adapterListener) {
        this.mListener = adapterListener;
    }

    /**
     * 自定义监听器
     *
     * @param <Data> 泛型
     */
    public interface AdapterListener<Data> {
        //当Cell点击的时候触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        //当Cell长按的时候触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    /**
     * 自定义的ViewHolder
     *
     * @param <Data> 泛型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        protected Data mData;

        private Unbinder unbinder;

        private AdapterCallback adapterCallback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的出发
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
         * Holder自己对自己对应的Data进行更新
         *
         * @param data Data数据
         */
        public void updateData(Data data) {
            if (this.adapterCallback != null) {
                this.adapterCallback.update(data, this);
            }
        }
    }

    /**
     * 对回调接口做一次实现
     * @param <Data>
     */
    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data> {

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }
}