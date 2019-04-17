package com.riwise.aging.info.loadInfo;

import com.riwise.aging.enums.LoadType;

public class LoadInfo {

    public LoadType Types;
    public String Message;

    public LoadInfo() {
    }

    public LoadInfo(LoadType type) {
        this.Types = type;
    }

    public LoadInfo(LoadType type, String msg) {
        this(type);
        this.Message = msg;
    }

    public String getMsg() {
        String desc = Types + "";
        if (Message != null) {
            if (Types == LoadType.error) desc += "\n";
            else desc += "\t";
            desc += Message;
        }
        return desc;
    }

    @Override
    public String toString() {
        return Types + "=>" + getMsg();
    }
}
