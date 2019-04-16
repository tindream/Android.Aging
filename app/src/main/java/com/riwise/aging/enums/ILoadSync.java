package com.riwise.aging.enums;

import android.database.Cursor;

import java.sql.SQLException;

public interface ILoadSync {
    void setValue(Cursor cursor) throws SQLException;

    String getTable();

    String getSql();

    long getId();

    boolean getUpdate();

    void setUpdate(boolean value);

    void reLoad();
}
