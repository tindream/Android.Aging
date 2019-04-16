package com.riwise.aging.support;

import java.util.ArrayList;
import java.util.List;

import com.riwise.aging.info.sqlInfo.BaseInfo;
import com.riwise.aging.info.sqlInfo.GoodInfo;

public class Cache {
    public static List<GoodInfo> GoodList = new ArrayList<GoodInfo>();

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
}
