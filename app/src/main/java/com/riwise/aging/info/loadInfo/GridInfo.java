package com.riwise.aging.info.loadInfo;

import com.riwise.aging.enums.LoadType;

public class GridInfo extends LoadInfo {

    public int imageId;

    public GridInfo(int imageId,  String msg) {
        super(LoadType.none);
        this.imageId = imageId;
        this.Message = msg;
    }
}
