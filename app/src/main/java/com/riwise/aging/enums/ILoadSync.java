package com.riwise.aging.enums;

import android.database.Cursor;

import java.sql.SQLException;

public interface ILoadSync {
    void setValue(Cursor cursor) throws SQLException;

    String getTable();

    String getSql();

    long getId();

    void setId(long id);
}
