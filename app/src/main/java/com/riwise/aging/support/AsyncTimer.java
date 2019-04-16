package com.riwise.aging.support;

import io.reactivex.ObservableEmitter;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.loadInfo.TimeInfo;

public class AsyncTimer extends AsyncBase {
    public AsyncTimer() {
        this(1000);
    }

    public AsyncTimer(int time) {
        initData(new TimeInfo(time));
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        long time = ((TimeInfo) info).onlyTime;
        while (true) {
            Thread.sleep(time);
            emitter.onNext(info);
        }
    }
}
