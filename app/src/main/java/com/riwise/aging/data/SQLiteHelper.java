package com.riwise.aging.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.riwise.aging.support.Config;
import com.riwise.aging.support.Method;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context) {
        super(context, "self.db", null, 7);
    }

    @Override
    //数据库第一次创建时被调用
    public void onCreate(SQLiteDatabase db) {
        Method.log("CreateTables");
        db.execSQL("create table Admins(Id integer primary key autoincrement not null" +
                ", Name nvarchar, Value nvarchar, DateTime datetime, unique(Id asc));");
        db.execSQL("create index main.admin_id on Admins (Id ASC);");

        db.execSQL("create table Goods(Id integer not null" +
                ", [Index] int, Name nvarchar, Price double, Quantity double, unique(Id asc));");
        db.execSQL("create index main.Goods_id on Goods (Id ASC);");
    }

    public void loadUpdate() {
        if (Config.Admin.Version == 0) {
//            update1();
        }
    }

    private void update1() {
        exec("alter table Goods add UserId integer");
        Config.Admin.Version = 1;
        updateAdmin("Version", Config.Admin.Version);
    }

    public void exec(String sql) {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            db.execSQL(sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public void updateAdmin(String name, Object value) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            String sql = "select * from Admins where Name = ?";
            cursor = db.rawQuery(sql, new String[]{name});
            if (cursor.moveToFirst()) {
                db.execSQL("update Admins set Value = ? where Name = ?", new String[]{value + "", name});
            } else {
                db.execSQL("insert into Admins(Value,Name) values(?,?)",
                        new String[]{value + "", name});
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    //软件版本号发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Method.log("onUpgrade");
    }
}
