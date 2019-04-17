package com.riwise.aging.support;

import java.util.ArrayList;
import java.util.List;

import com.riwise.aging.info.sqlInfo.BaseInfo;
import com.riwise.aging.info.sqlInfo.AgingInfo;

public class Cache {
    public static List<AgingInfo> AgingList = new ArrayList<>();

    private static <T extends BaseInfo> T FindInfo(List<T> list, long id) {
        for (int i = 0; i < list.size(); i++) {
            T info = list.get(i);
            if (info.Id == id) return info;
        }
        return null;
    }

    public static AgingInfo FindAging(long id) {
        return FindInfo(AgingList, id);
    }

    public static AgingInfo FindAging(String name) {
        for (int i = 0; i < AgingList.size(); i++) {
            AgingInfo info = AgingList.get(i);
            if (info.Name == name) return info;
        }
        return new AgingInfo(name);
    }
}
