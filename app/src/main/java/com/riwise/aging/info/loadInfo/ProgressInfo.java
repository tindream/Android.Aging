package com.riwise.aging.info.loadInfo;

import com.riwise.aging.enums.LoadType;

public class ProgressInfo extends LoadInfo {

    public String name;
    public String desc;
    public int progress;
    public boolean loading;
    public boolean complete;

    public ProgressInfo(String name, String desc, int progress) {
        super(LoadType.progress);
        this.name = name;
        this.desc = desc;
        this.progress = progress;
        this.loading = true;
    }

    public ProgressInfo(String name, String desc) {
        super(LoadType.progress);
        this.name = name;
        this.desc = desc;
        this.loading = false;
        this.complete = true;
    }
}
