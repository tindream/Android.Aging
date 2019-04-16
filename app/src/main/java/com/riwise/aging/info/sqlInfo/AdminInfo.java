package com.riwise.aging.info.sqlInfo;

import java.util.Date;

import com.riwise.aging.data.SQLiteServer;
import com.riwise.aging.support.Config;

public class AdminInfo {
    public int Version = 0;
    public long UserId;
    public String Name;
    public String Pad;
    public String Display;

    //同步pc设置
    //更新比较
    public long UpdateVersion;
    ///报警电量值
    public int BatteryPercent;
}