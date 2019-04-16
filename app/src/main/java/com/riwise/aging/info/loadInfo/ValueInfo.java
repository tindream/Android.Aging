package com.riwise.aging.info.loadInfo;

public class ValueInfo extends LoadInfo {

    public int value;
    public double total;

    public ValueInfo() {
    }

    public ValueInfo(int value, double total) {
        this.value = value;
        this.total = total;
    }

    @Override
    public String getMsg() {
        return value + "";
    }

    @Override
    public String toString() {
        return value + "";
    }
}
