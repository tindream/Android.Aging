package com.riwise.aging.info.sqlInfo;


import android.support.annotation.NonNull;

public class BaseInfo implements Comparable<BaseInfo> {
    public long Id;

    public BaseInfo() {
    }

    @Override
    public int compareTo(@NonNull BaseInfo o) {
        //自定义比较方法，如果认为此实体本身大则返回1，否则返回-1
        if (this.Id >= o.Id) {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        BaseInfo info = (BaseInfo) obj;
        return Id == info.Id;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
