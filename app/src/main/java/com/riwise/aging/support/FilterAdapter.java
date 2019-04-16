package com.riwise.aging.support;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter<T> extends MyAdapter<T> implements Filterable {
    private List<T> filterlist;//过路器（MyFilter）数据源
    private final Object mLock = new Object();
    private MyFilter mFilter;

    public FilterAdapter(Context context, int resource, List<T> list) {
        super(context, resource, list);
        this.mFilter = new MyFilter();
    }

    @Override
    public void bindView(ViewHolder holder, T obj) {
        holder.setText(android.R.id.text1, obj.toString());
    }

    public T addNone() {
        return null;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class MyFilter extends Filter {
        /**
         * 过滤autoCompleteEditext中的字 改成包含
         */
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (filterlist == null) {
                synchronized (mLock) {
                    filterlist = new ArrayList<T>(list);
                }
            }
            List<T> values = new ArrayList<T>();
            for (int i = 0; i < filterlist.size(); i++) {
                T info = filterlist.get(i);
                String value = info.toString();
                if (null != value && null != constraint && value.contains(constraint)) {
                    values.add(info);
                }
            }
            if (values.size() == 0) {
                T t = addNone();
                if (t != null) values.add(t);
            }
            results.values = values;
            results.count = values.size();
            return results;
        }

        /**
         * 在FilterResults方法中把过滤好的数据传入此方法中 results过滤好的数据源重新给data赋值显示新的适配内容
         * 并刷新适配器
         */
        @Override
        protected void publishResults(CharSequence arg0, FilterResults results) {
            list = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}

