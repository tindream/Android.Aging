package com.riwise.aging.support;

import java.util.ArrayList;
import java.util.List;

import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.eventInfo.NotifiedEventInfo;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.sqlInfo.BaseInfo;
import com.riwise.aging.info.sqlInfo.GoodInfo;

public class Cache {
    public static List<GoodInfo> GoodList = new ArrayList<GoodInfo>();
    public static List<LoadInfo> NotifiedList = new ArrayList<LoadInfo>();

    private static <T extends BaseInfo> T FindInfo(List<T> list, long id) {
        for (int i = 0; i < list.size(); i++) {
            T info = list.get(i);
            if (info.Id == id) return info;
        }
        return null;
    }

    public static GoodInfo FindGood(long id) {
        return FindInfo(GoodList, id);
    }

    public static void addNotified(LoadType type) {
        addNotified(type, null);
    }

    public static void addNotified(LoadType from, String msg) {
        LoadInfo e = new NotifiedEventInfo(from);
        if (msg != null)
            e = new NotifiedEventInfo(from, msg);
        Method.hit(e.getMsg());
        NotifiedList.add(0, e);
    }
}
