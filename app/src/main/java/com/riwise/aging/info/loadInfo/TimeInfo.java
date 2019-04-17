package com.riwise.aging.info.loadInfo;

import android.view.View;

import java.sql.Time;
import java.text.SimpleDateFormat;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.support.Method;

public class TimeInfo extends LoadInfo {
    public long time;

    public TimeInfo(long time) {
        super(LoadType.time);
        this.time = time;
    }
}
