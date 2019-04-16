package com.riwise.aging.info.setInfo;

import java.io.File;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.ViewHolder;

public class LoaderInfo extends LoadInfo {

    public ViewHolder holder;
    public int id;
    public String msg;
    public File file;
    public int imageId;

    public LoaderInfo(LoadType type, ViewHolder holder) {
        this.Types = type;
        this.holder = holder;
    }

    public LoaderInfo(LoadType type, ViewHolder holder, int id, String msg) {
        this(type, holder);
        this.id = id;
        this.msg = msg;
    }

    public LoaderInfo(LoadType type, ViewHolder holder, int id, File file) {
        this(type, holder);
        this.id = id;
        this.file = file;
    }

    public LoaderInfo(LoadType type, ViewHolder viewHolder, int id, int imageId) {
        this(type, viewHolder);
        this.id = id;
        this.imageId = imageId;
    }
}
