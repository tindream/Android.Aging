package com.riwise.aging.info.sqlInfo;


public class UpdateLogInfo {
    public long Id;
    public String Name;
    public int Type;
    public String Ids;

    public UpdateLogInfo() {
    }

    public String getSql() {
        return "select * from UpdateLogs";
    }
}
