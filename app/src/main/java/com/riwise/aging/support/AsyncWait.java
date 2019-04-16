package com.riwise.aging.support;

import io.reactivex.ObservableEmitter;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.loadInfo.TimeInfo;

public class AsyncWait extends AsyncBase {
    public AsyncWait() {
        this(0);
    }

    public AsyncWait(int time) {
        initData(new TimeInfo(time));
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        Thread.sleep(((TimeInfo) info).time);
        emitter.onNext(info);
    }
}
