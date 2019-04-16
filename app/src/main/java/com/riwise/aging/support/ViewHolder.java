package com.riwise.aging.support;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
    private SparseArray<View> mViews;   //存储ListView 的 item中的View
    private View item;                  //存放convertView
    private int position;               //游标
    private Context context;            //Context上下文

    //构造方法，完成相关初始化
    private ViewHolder(Context context, ViewGroup parent, int layoutRes) {
        mViews = new SparseArray<>();
        this.context = context;
        View convertView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
        convertView.setTag(this);
        item = convertView;
    }

    public static ViewHolder bind(Context context, View convertView, ViewGroup parent,
                                  int layoutRes, int position) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(context, parent, layoutRes);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.item = convertView;
        }
        holder.position = position;
        return holder;
    }

    public <T extends View> T getView(int id) {
        T t = (T) mViews.get(id);
        if (t == null) {
            t = (T) item.findViewById(id);
            mViews.put(id, t);
        }
        return t;
    }

    /**
     * 获取当前条目
     */
    public View getItemView() {
        return item;
    }

    /**
     * 获取条目位置
     */
    public int getItemPosition() {
        return position;
    }

    /**
     * 设置文字
     */
    public ViewHolder setText(int id, CharSequence text) {
        View view = getView(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }

    /**
     * 设置图片
     */
    public ViewHolder setImageResource(int id, int drawableRes) {
        View view = getView(id);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(drawableRes);
        } else {
            view.setBackgroundResource(drawableRes);
        }
        return this;
    }

    /**
     * 设置点击监听
     */
    public ViewHolder setOnClickListener(View.OnClickListener listener) {
        item.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置可见
     */
    public ViewHolder setVisibility(int id, int visible) {
        getView(id).setVisibility(visible);
        return this;
    }
}
