package com.riwise.aging.info.sqlInfo;


import android.database.Cursor;
import android.support.annotation.NonNull;

import java.sql.SQLException;

import com.riwise.aging.enums.ILoadSync;

public class GoodInfo extends BaseInfo implements Comparable<BaseInfo>, ILoadSync {
    public int Index;
    public String Name;
    public double Price;
    public double Quantity;

    public GoodInfo() {
    }

    @Override
    public String toString() {
        return Id + "," + Name;
    }

    public void setValue(Cursor cursor) throws SQLException {
        Id = cursor.getLong(cursor.getColumnIndex("Id"));
        Index = cursor.getInt(cursor.getColumnIndex("Index"));
        Name = cursor.getString(cursor.getColumnIndex("Name"));
        Price = cursor.getDouble(cursor.getColumnIndex("Price"));
        Quantity = cursor.getDouble(cursor.getColumnIndex("Quantity"));
    }

    @Override
    public int compareTo(@NonNull BaseInfo o) {
        if (this.Index >= ((GoodInfo) o).Index) {
            return 1;
        }
        return -1;
    }

    public String getTable() {
        return "Goods";
    }

    public String getSql() {
        return "select Id,[Index],Name,Price,Quantity from " + getTable();
    }
}
