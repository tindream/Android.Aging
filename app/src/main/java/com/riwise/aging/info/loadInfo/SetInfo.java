package com.riwise.aging.info.loadInfo;

import com.riwise.aging.enums.LoadType;

public class SetInfo extends LoadInfo {

    public int imageId;
    public boolean iHeard;
    public String desc;
    public boolean noRight;

    public SetInfo() {
        super(LoadType.none);
        this.iHeard = true;
    }

    public SetInfo(String msg) {
        super(LoadType.none, msg);
    }

    public SetInfo(String msg, String desc, boolean noRight) {
        super(LoadType.none, msg);
        this.desc = desc;
        this.noRight = noRight;
    }

    public SetInfo(int imageId, String msg) {
        super(LoadType.none, msg);
        this.imageId = imageId;
    }

    public SetInfo(int imageId, String msg, String desc) {
        super(LoadType.none, msg);
        this.imageId = imageId;
        this.desc = desc;
    }
}
