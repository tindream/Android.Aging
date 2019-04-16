package com.riwise.aging.enums;

import io.reactivex.ObservableEmitter;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.ViewHolder;

public interface IListListener {
    <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object);
}
