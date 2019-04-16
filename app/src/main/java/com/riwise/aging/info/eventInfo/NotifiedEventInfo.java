package com.riwise.aging.info.eventInfo;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.Method;

public class NotifiedEventInfo extends LoadInfo {

    public LoadType from;

    public NotifiedEventInfo(LoadType type) {
        super(type);
        this.Message = "Success";
    }

    public NotifiedEventInfo(LoadType from, String msg) {
        super(LoadType.Error);
        this.from = from;
        this.Message = msg;
    }

    @Override
    public String getMsg() {
        String desc = "";
        if (Types == LoadType.Error) {
            desc += from + " " + Types;
        } else {
            desc += Types;
            if (!Method.isEmpty(Message)) desc += " " + Message;
        }
        return desc;
    }
}
