package com.riwise.aging.info.loadInfo;

import com.riwise.aging.enums.LoadType;

public class SetInfo extends LoadInfo {

    public int imageId;
    public boolean iHeard;
    public String desc;

    public SetInfo(int imageId, String msg) {
        super(LoadType.none);
        this.imageId = imageId;
        this.Message = msg;
    }

    public SetInfo(int imageId, String msg, String desc) {
        super(LoadType.none);
        this.imageId = imageId;
        this.Message = msg;
        this.desc = desc;
    }

    public SetInfo(String msg) {
        super(LoadType.none);
        this.Message = msg;
        this.iHeard = true;
    }
}
