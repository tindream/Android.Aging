package com.riwise.aging.info.sqlInfo;


import android.database.Cursor;
import android.support.annotation.NonNull;

import java.sql.SQLException;

import com.riwise.aging.enums.ILoadSync;

public class AgingInfo extends BaseInfo implements Comparable<BaseInfo>, ILoadSync {
    public String Name;
    public boolean I32;
    public double Last;
    public int Image;
    public int Audio;
    public int Video;
    public int Contact;
    public int Sms;
    public int Call;
    public int App;
    public int File4;
    public int File8;
    public int File128;

    public AgingInfo() {
    }

    public AgingInfo(String name) {
        this.Name = name;
    }

    @Override
    public String toString() {
        return Id + "," + Name;
    }

    public void setValue(Cursor cursor) throws SQLException {
        Id = cursor.getLong(cursor.getColumnIndex("Id"));
        I32 = cursor.getInt(cursor.getColumnIndex("I32")) != 0;
        Name = cursor.getString(cursor.getColumnIndex("Name"));
        Last = cursor.getDouble(cursor.getColumnIndex("Last"));
        Image = cursor.getInt(cursor.getColumnIndex("Image"));
        Audio = cursor.getInt(cursor.getColumnIndex("Audio"));
        Video = cursor.getInt(cursor.getColumnIndex("Video"));
        Contact = cursor.getInt(cursor.getColumnIndex("Contact"));
        Sms = cursor.getInt(cursor.getColumnIndex("Sms"));
        Call = cursor.getInt(cursor.getColumnIndex("Call"));
        App = cursor.getInt(cursor.getColumnIndex("App"));
        File4 = cursor.getInt(cursor.getColumnIndex("File4"));
        File8 = cursor.getInt(cursor.getColumnIndex("File8"));
        File128 = cursor.getInt(cursor.getColumnIndex("File128"));
    }

    public String getTable() {
        return "Agings";
    }

    public String getSql() {
        return "select * from " + getTable();
    }
}
