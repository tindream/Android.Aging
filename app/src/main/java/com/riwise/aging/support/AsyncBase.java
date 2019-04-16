package com.riwise.aging.support;

import android.os.Handler;
import android.os.Message;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import com.riwise.aging.info.loadInfo.ErrorEventInfo;
import com.riwise.aging.info.loadInfo.LoadInfo;

public abstract class AsyncBase extends ListenerBase {
    protected void onLoadData(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception {
    }

    protected void initData(LoadInfo info) {
        try {
            Observable.create((ObservableOnSubscribe<LoadInfo>) emitter -> new Thread(() -> {
                try {
                    onLoadData(emitter, info);
                } catch (Exception e) {
                    Method.log("Load Error=>", e);
                    emitter.onNext(new ErrorEventInfo(info.Types, e.getMessage()));
                }
            }).start()).subscribe(info1 -> {
                Message m = new Message();
                m.obj = info1;
                uiHandler.sendMessage(m);
            });
        } catch (Exception e) {
            Method.log(e);
        }
    }

    protected void onCallUI(LoadInfo info) {
        onListener(info);
    }

    protected Handler uiHandler = new Handler(msg -> {
        try {
            if (msg.what != 0) return false;
            onCallUI((LoadInfo) msg.obj);
        } catch (Exception e) {
            Method.log(e);
        }
        return false;
    });
}
