package com.riwise.aging.support;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.LinkedList;
import java.util.List;

public abstract class MyAdapter<T> extends ArrayAdapter<T> {
    protected List<T> list;
    private int mLayoutRes;           //布局id

    public MyAdapter(Context context, int resource, List<T> list) {
        super(context, resource, list);
        this.mLayoutRes = resource;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract void bindView(ViewHolder holder, T obj);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.bind(parent.getContext(), convertView, parent, mLayoutRes, position);
        bindView(holder, getItem(position));
        return holder.getItemView();
    }

    //添加一个元素
    public void add(T data) {
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(data);
        notifyDataSetChanged();
    }

    //往特定位置，添加一个元素
    public void add(int position, T data) {
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if (list != null) {
            list.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (list != null) {
            list.remove(position);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if (list != null) {
            list.clear();
        }
        notifyDataSetChanged();
    }
}

