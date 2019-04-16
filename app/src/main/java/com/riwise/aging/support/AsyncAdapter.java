package com.riwise.aging.support;

import io.reactivex.ObservableEmitter;
import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.enums.IObservableListener;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;

public class AsyncAdapter extends AsyncBase {
    protected IObservableListener observableListener;

    protected void onListener(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        if (observableListener != null)
            observableListener.onReady(emitter, info);
    }

    public AsyncAdapter setListener(IObservableListener observableListener, ILoadListener loadListener) {
        this.observableListener = observableListener;
        this.loadListener = loadListener;
        return this;
    }

    public void init() {
        initData(new LoadInfo());
    }

    public void init(LoadType type) {
        initData(new LoadInfo(type));
    }

    public void init(LoadInfo info) {
        initData(info);
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        onListener(emitter, info);
    }
}
