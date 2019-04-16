package com.riwise.aging.enums;

import io.reactivex.ObservableEmitter;
import com.riwise.aging.info.loadInfo.LoadInfo;

public interface IObservableListener {
    void onReady(ObservableEmitter<LoadInfo> emitter, LoadInfo info) throws Exception;
}
