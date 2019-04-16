package com.riwise.aging.support;

import com.riwise.aging.enums.IListener;
import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;

public abstract class ListenerBase implements IListener {
    protected ILoadListener loadListener;

    public void setListener(ILoadListener listener) {
        this.loadListener = listener;
    }

    public void onListener(LoadType type) {
        onListener(new LoadInfo(type));
    }

    public void onListener(LoadInfo e) {
        try {
            if (loadListener != null)
                loadListener.onReady(e);
        } catch (Exception ex) {
            Method.log(ex);
        }
    }
}
