package com.riwise.aging.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

import com.riwise.aging.R;
import com.riwise.aging.enums.ILoadSync;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.sqlInfo.AdminBaseInfo;
import com.riwise.aging.info.sqlInfo.AdminInfo;
import com.riwise.aging.info.sqlInfo.AgingInfo;
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
        Cache.AgingList = queryList(AgingInfo.class, new AgingInfo().getSql());
        if (Cache.AgingList.size() == 0) {
            addAging(Config.context.getString(R.string.btn_0), true, 20, 11, 12, 13, 14, 15, 16, 17, 80, 15, 5);
            addAging(Config.context.getString(R.string.btn_0), false, 20, 11, 12, 13, 14, 15, 16, 17, 80, 15, 5);

            addAging(Config.context.getString(R.string.btn_10), true, 2, 1000, 200, 20, 1000, 3000, 500, 50, 80, 15, 5);
            addAging(Config.context.getString(R.string.btn_10), false, 5, 3000, 300, 30, 1000, 3000, 500, 60, 80, 15, 5);

            addAging(Config.context.getString(R.string.btn_18), true, 2, 1500, 250, 25, 2000, 5400, 500, 55, 80, 15, 5);
            addAging(Config.context.getString(R.string.btn_18), false, 3, 4500, 450, 45, 2000, 5400, 500, 80, 80, 15, 5);

            addAging(Config.context.getString(R.string.btn_24), true, 1.5, 2000, 300, 30, 3000, 7200, 500, 60, 100, 0, 0);
            addAging(Config.context.getString(R.string.btn_24), false, 2, 5400, 540, 54, 3000, 7200, 500, 100, 100, 0, 0);
        }
        Method.log("Load Completed");
        emitter.onNext(new LoadInfo(LoadType.complete));
    }

    private void addAging(String name, boolean i32, double last, int image, int audio, int video, int contact, int sms, int call, int app, int file4, int file8, int file128) throws Exception {
        AgingInfo info = new AgingInfo();
        info.Name = name;
        info.I32 = i32;
        info.Last = last;
        info.Image = image;
        info.Audio = audio;
        info.Video = video;
        info.Contact = contact;
        info.Sms = sms;
        info.Call = call;
        info.App = app;
        info.File4 = file4;
        info.File8 = file8;
        info.File128 = file128;
        insert(info);
        Cache.AgingList.add(info);
    }

    public void loadAdmin() throws Exception {
        List<AdminBaseInfo> list = queryList(AdminBaseInfo.class, new AdminBaseInfo().getSql());
        Field[] fields = AdminInfo.class.getFields();
        for (Field field : fields) {
            if (field.toGenericString().contains("static")) continue;
            String name = field.getName();
            for (AdminBaseInfo info : list) {
                if (info.Name.equals(name)) {
                    ConverHelper.setValue(Config.Admin, field, info.Value);
                    break;
                }
            }
        }
    }

    public <T extends ILoadSync> void insert(T info) throws Exception {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
           long id= db.insert(info.getTable(), null, ConverHelper.getValues(info));
           info.setId(id);
        } finally {
            if (db != null) db.close();
        }
    }

    public <T extends ILoadSync> void update(T info) throws Exception {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] args = {info.getId() + ""};
            db.update(info.getTable(), ConverHelper.getValues(info), "id=?", args);
        } finally {
            if (db != null) db.close();
        }
    }

    public void updateAdmin(String name, Object value) {
        dbHelper.updateAdmin(name, value);
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
