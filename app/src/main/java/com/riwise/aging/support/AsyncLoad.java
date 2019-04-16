package com.riwise.aging.support;

import io.reactivex.ObservableEmitter;
import com.riwise.aging.data.SQLiteServer;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.loadInfo.TimeInfo;

public class AsyncLoad extends AsyncBase {
    public AsyncLoad() {
        initData(new LoadInfo());
    }

    @Override
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
        new SQLiteServer().Load(emitter);
    }
}
