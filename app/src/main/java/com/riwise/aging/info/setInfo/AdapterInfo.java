package com.riwise.aging.info.setInfo;

import java.util.List;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.MyAdapter;

public class AdapterInfo extends LoadInfo {

    public MyAdapter adapter;
    public List list;

    public AdapterInfo(MyAdapter adapter, List list) {
        this.Types = LoadType.setAdapter;
        this.adapter = adapter;
        this.list = list;
    }
}
