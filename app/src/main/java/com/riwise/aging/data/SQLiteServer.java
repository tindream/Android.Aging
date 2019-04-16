package com.riwise.aging.data;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.ObservableEmitter;
import com.riwise.aging.enums.ILoadSync;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.sqlInfo.AdminBaseInfo;
import com.riwise.aging.info.sqlInfo.AdminInfo;
import com.riwise.aging.info.sqlInfo.GoodInfo;
import com.riwise.aging.support.Cache;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.ConverHelper;
import com.riwise.aging.support.Method;

public class SQLiteServer {
    private SQLiteHelper dbHelper;

    public SQLiteServer() {
        dbHelper = new SQLiteHelper(Config.context);
    }

    public void Load(ObservableEmitter<LoadInfo> emitter) throws Exception {
        loadAdmin();
        dbHelper.loadUpdate();
        emitter.onNext(new LoadInfo(LoadType.load));
        Cache.GoodList = queryList(GoodInfo.class, new GoodInfo().getSql());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Cache.GoodList.sort(Comparator.naturalOrder());
        }
        Method.log("Load本地完成");
        emitter.onNext(new LoadInfo(LoadType.complete));
    }

    public void loadAdmin() throws Exception {
        List<AdminBaseInfo> list = queryList(AdminBaseInfo.class, new AdminBaseInfo().getSql());
        Field[] fields = AdminInfo.class.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            for (AdminBaseInfo info : list) {
                if (info.Name.equals(name)) {
                    ConverHelper.setValue(Config.Admin, field, info.Value);
                    break;
                }
            }
        }
    }

    public <T extends ILoadSync> void insert(List<T> sqlList, List<T> sqliteList, Class type) throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = sqlList.size() - 1; i >= 0; i--) {
                T temp = sqlList.get(i);
                //add
                T info = (T) type.newInstance();
                ConverHelper.setValue(info, temp);
                sqliteList.add(info);
                db.insert(info.getTable(), null, ConverHelper.getValues(info));
            }
        } finally {
            //显示的设置数据事务是否成功
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public <T extends ILoadSync> void update(List<T> sqlList, List<T> sqliteList, Class type) throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < sqliteList.size(); i++) {
                T info = sqliteList.get(i);
                int index = sqlList.indexOf(info);
                if (index > -1) {
                    T temp = sqlList.get(index);
                    if (!ConverHelper.iEquals(temp, info)) {
                        //update
                        ConverHelper.setValue(info, temp);
                        info.reLoad();
                        String[] args = {info.getId() + ""};
                        db.update(temp.getTable(), ConverHelper.getValues(temp), "id=?", args);
                    }
                }
            }
        } finally {
            //显示的设置数据事务是否成功
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public <T extends ILoadSync> void sync(List<T> sqlList, List<T> sqliteList, Class type) throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < sqlList.size(); i++) {
                sqlList.get(i).setUpdate(true);
            }
            for (int i = 0; i < sqliteList.size(); i++) {
                sqliteList.get(i).setUpdate(true);
            }
            for (int i = 0; i < sqliteList.size(); i++) {
                T info = sqliteList.get(i);
                int index = sqlList.indexOf(info);
                if (index > -1) {
                    T temp = sqlList.get(index);
                    info.setUpdate(false);
                    temp.setUpdate(false);
                    if (!ConverHelper.iEquals(temp, info)) {
                        //update
                        ConverHelper.setValue(info, temp);
                        info.reLoad();
                        String[] args = {info.getId() + ""};
                        db.update(temp.getTable(), ConverHelper.getValues(temp), "id=?", args);
                    }
                }
            }
            for (int i = sqliteList.size() - 1; i >= 0; i--) {
                T info = sqliteList.get(i);
                if (info.getUpdate()) {
                    //delete
                    String[] args = {info.getId() + ""};
                    db.delete(info.getTable(), "id=?", args);
                    sqliteList.remove(i);
                }
            }
            for (int i = sqlList.size() - 1; i >= 0; i--) {
                T temp = sqlList.get(i);
                if (temp.getUpdate()) {
                    //add
                    T info = (T) type.newInstance();
                    ConverHelper.setValue(info, temp);
                    sqliteList.add(info);
                    db.insert(info.getTable(), null, ConverHelper.getValues(info));
                }
            }
        } finally {
            //显示的设置数据事务是否成功
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public void updateAdmin(String name, Object value) {
        dbHelper.updateAdmin(name, value);
    }

    public void execSQL(String sql) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            db.execSQL(sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public <T extends ILoadSync> T query(Class<T> type, String sql) throws Exception {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                T info = (T) type.newInstance();
                info.setValue(cursor);
                return info;
            }
            return null;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    public <T extends ILoadSync> List<T> queryList(Class<T> type, String sql) throws Exception {
        List<T> list = new ArrayList<T>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                T info = (T) type.newInstance();
                info.setValue(cursor);
                list.add(info);
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return list;
    }
}
