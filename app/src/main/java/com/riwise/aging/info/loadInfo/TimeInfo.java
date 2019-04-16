package com.riwise.aging.info.loadInfo;

import android.view.View;

import java.sql.Time;
import java.text.SimpleDateFormat;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.support.Method;

public class TimeInfo extends LoadInfo {
    private long t;
    private SimpleDateFormat format;

    public String times;
    public String msg;
    public View view;
    public long onlyTime;
    public long endTime;

    public TimeInfo(LoadType type) {
        this.Types = type;
    }

    public TimeInfo(long onlyTime) {
        this.Types = LoadType.time;
        this.onlyTime = onlyTime;
    }

    public TimeInfo(View view, long time, long onlyTime, String msg) {
        this.Types = LoadType.time;
        this.endTime = time + onlyTime;
        if (Method.isEmpty(msg)) msg = "";
        this.msg = msg;
        this.onlyTime = onlyTime;
        this.view = view;
        this.t = new Time(0, 0, 0).getTime();
        this.format = new SimpleDateFormat("HH:mm:ss");
    }

    public boolean iWait() {
        return endTime > System.currentTimeMillis();
    }

    public String getTimes() {
        return format.format(new Time(t + endTime - System.currentTimeMillis())) + msg;
    }
}
