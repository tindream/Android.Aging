package com.riwise.aging.info.loadInfo;

import com.riwise.aging.R;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.support.Method;

import java.io.File;

public class SetInfo extends LoadInfo {

    public int imageId;
    public boolean iHeard;
    public String desc;
    public boolean noRight;
    public int colorId;

    public SetInfo() {
        super(LoadType.none);
        this.iHeard = true;
        this.noRight = true;
    }

    public SetInfo(String msg) {
        super(LoadType.none, msg);
    }

    public SetInfo(String msg, boolean noRight) {
        super(LoadType.none, msg);
        this.noRight = noRight;
    }

    public SetInfo(String msg, String desc) {
        super(LoadType.none, msg);
        updateDesc(desc);
    }

    public void updateDesc(String desc) {
        if (!Method.isEmpty(desc)) {
            if (desc.contains(";")) {
                desc = desc.split(";").length + "";
            } else {
                desc = new File(desc).getName();
                if (desc.length() > 12) desc = desc.substring(0, 12) + "...";
            }
        }
        this.desc = desc;
    }

    public void updateDesc(String desc, boolean loading, boolean complete) {
        if (desc != null && desc.length() > 25) desc = desc.substring(0, 25) + "...";
        this.desc = desc;
        this.colorId = loading ? R.color.colorPrimaryLight : R.color.Transparent;
        this.imageId = complete ? R.drawable.ic_correct_dark : 0;
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
