package com.riwise.aging.info.loadInfo;

import com.riwise.aging.enums.LoadType;

public class ProgressInfo extends LoadInfo {

    public int value;

    public ProgressInfo(int value) {
        this.Types = LoadType.progress;
        this.value = value;
    }
}
