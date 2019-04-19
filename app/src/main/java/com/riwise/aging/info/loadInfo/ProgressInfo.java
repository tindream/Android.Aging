package com.riwise.aging.info.loadInfo;

import com.riwise.aging.enums.LoadType;

public class ProgressInfo extends LoadInfo {

    public String name;
    public String desc;
    public int index;
    public boolean loading;
    public boolean complete;

    public ProgressInfo(String name, String desc, int index) {
        super(LoadType.progress);
        this.name = name;
        this.desc = desc;
        this.index = index;
        this.loading = true;
    }

    public ProgressInfo(String name, String desc, boolean complete) {
        super(LoadType.progress);
        this.name = name;
        this.desc = desc;
        this.loading = false;
        this.complete = complete;
    }

    public ProgressInfo(String name, String desc) {
        this(name, desc, true);
        this.index = 100;
    }
}
