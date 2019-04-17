package com.riwise.aging.info.loadInfo;

import android.view.View;

import java.sql.Time;
import java.text.SimpleDateFormat;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.sqlInfo.AgingInfo;
import com.riwise.aging.support.Method;

public class TestInfo extends LoadInfo {
    public AgingInfo info;

    public TestInfo(AgingInfo info) {
        super(LoadType.aging);
        this.info = info;
    }
}
