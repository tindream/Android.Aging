package com.riwise.aging.enums;

import com.riwise.aging.info.loadInfo.LoadInfo;

public interface IListener {
    void setListener(ILoadListener listener);

    void onListener(LoadType type);

    void onListener(LoadInfo e);
}
