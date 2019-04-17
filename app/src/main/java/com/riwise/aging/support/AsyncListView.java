package com.riwise.aging.support;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.ObservableEmitter;

import com.riwise.aging.R;
import com.riwise.aging.enums.IListListener;
import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.info.setInfo.AdapterInfo;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.setInfo.LoaderInfo;

public class AsyncListView extends AsyncBase {
    protected IListListener listListener;
    private Context context;

    protected <T> void onListener(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        try {
            if (listListener != null)
                listListener.onReady(emitter, holder, object);
        } catch (Exception ex) {
            Method.log(ex);
        }
    }

    public AsyncListView setListener(IListListener listListener, ILoadListener loadListener) {
        this.listListener = listListener;
        this.loadListener = loadListener;
        return this;
    }

    public <T> void init(Context context, List<T> list, int resId) {
        this.context = context;
        new AsyncAdapter().setListener((emitter, info) -> {
            MyAdapter<T> myAdapter1 = new MyAdapter<T>(context, resId, list) {
                @Override
                public void bindView(ViewHolder holder, T obj) {
                    new AsyncAdapter().setListener((emitter, info) -> {
                        onListener(emitter, holder, obj);
                    }, obj2 -> {
                        onCallUI(obj2);
                    }).init();
                }
            };
            emitter.onNext(new AdapterInfo(myAdapter1, list));
        }, obj2 -> {
            onCallUI(obj2);
        }).init();
    }

    @Override
    protected void onCallUI(LoadInfo info) {
        switch (info.Types) {
            case setText:
                LoaderInfo loader = (LoaderInfo) info;
                loader.holder.setText(loader.id, loader.msg);
                break;
            case setImageId:
                loader = (LoaderInfo) info;
                ImageView imageView = loader.holder.getView(loader.id);
                if (loader.imageId != 0)
                    imageView.setImageDrawable(Config.context.getDrawable(loader.imageId));
                break;
        }
        super.onCallUI(info);
    }
}
