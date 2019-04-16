package com.riwise.aging.info.sqlInfo;


import android.database.Cursor;

import java.sql.SQLException;
import java.util.Date;

import com.riwise.aging.enums.ILoadSync;

public class AdminBaseInfo extends BaseInfo implements ILoadSync {
    public String Name;
    public String Value;

    public AdminBaseInfo() {
    }

    public AdminBaseInfo(long id, String name, String value) {
        this.Id = id;
        this.Name = name;
        this.Value = value;
    }

    public void setValue(Cursor cursor) throws SQLException {
        Id = cursor.getLong(cursor.getColumnIndex("Id"));
        Name = cursor.getString(cursor.getColumnIndex("Name"));
        Value = cursor.getString(cursor.getColumnIndex("Value"));
    }

    public String getTable() {
        return "Admins";
    }

    public String getSql() {
        return "select Id,Name,Value from " + getTable();
    }
}
