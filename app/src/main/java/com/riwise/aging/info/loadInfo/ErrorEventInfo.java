package com.riwise.aging.info.loadInfo;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;

public class ErrorEventInfo extends LoadInfo {

    public LoadType FromTypes;

    public ErrorEventInfo(LoadType from, String msg) {
        super(LoadType.error, msg);
        this.FromTypes = from;
    }
}
