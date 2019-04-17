package com.riwise.aging.support;

import android.content.ContentValues;
import android.database.Cursor;

import com.riwise.aging.enums.ILoadSync;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Date;

public class ConverHelper {
    public static <T> void setValue(T t, Field field, String value) throws Exception {
        Object obj = null;
        String name = field.getType().getName();
        if (name == boolean.class.getName()) {
            obj = Boolean.parseBoolean(value);
        } else if (name == int.class.getName()) {
            obj = Integer.parseInt(value);
        } else if (name == long.class.getName()) {
            obj = Long.parseLong(value);
        } else if (name == double.class.getName()) {
            obj = Double.parseDouble(value);
        } else if (name == String.class.getName()) {
            obj = value;
        } else if (name == Date.class.getName()) {
            obj = new Date(Long.parseLong(value));
        } else if (name == Time.class.getName()) {
            obj = new Time(Long.parseLong(value));
        }
        // 设置该属性名称与值
        field.set(t, obj);
    }

    public static <T> ContentValues getValues(T t) throws Exception {
        ContentValues values = new ContentValues();
        Field[] fields = t.getClass().getFields();
        for (Field field : fields) {
            if (field.toGenericString().contains("static")) continue;
            if (field.getName() == "Id") continue;
            String name = field.getType().getName();
            if (name == boolean.class.getName()) {
                values.put(field.getName(), (boolean) field.get(t));
            } else if (name == int.class.getName()) {
                values.put(field.getName(), (int) field.get(t));
            } else if (name == long.class.getName()) {
                values.put(field.getName(), (long) field.get(t));
            } else if (name == double.class.getName()) {
                values.put(field.getName(), (double) field.get(t));
            } else if (name == String.class.getName()) {
                values.put(field.getName(), (String) field.get(t));
            } else if (name == Date.class.getName()) {
                values.put(field.getName(), ((Date) field.get(t)).getTime());
            } else if (name == Time.class.getName()) {
                values.put(field.getName(), ((Time) field.get(t)).getTime());
            }
        }
        return values;
    }
}
