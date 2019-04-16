package com.riwise.aging.support;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Date;

public class ConverHelper {
    public static <T> void setValue(T t, Cursor cursor) throws Exception {
        Field[] fields = t.getClass().getFields();
        for (Field field : fields) {
            Object value = null;
            String name = field.getType().getName();
            if (name == boolean.class.getName()) {
                value = cursor.getInt(cursor.getColumnIndex(field.getName())) != 0;
            } else if (name == int.class.getName()) {
                value = cursor.getInt(cursor.getColumnIndex(field.getName()));
            } else if (name == long.class.getName()) {
                value = cursor.getLong(cursor.getColumnIndex(field.getName()));
            } else if (name == double.class.getName()) {
                value = cursor.getDouble(cursor.getColumnIndex(field.getName()));
            } else if (name == String.class.getName()) {
                value = cursor.getString(cursor.getColumnIndex(field.getName()));
                if (value == null) value = "";
            } else if (name == Date.class.getName()) {
                value = new Date(cursor.getLong(cursor.getColumnIndex(field.getName())));
            } else if (name == Time.class.getName()) {
                value = new Time(cursor.getLong(cursor.getColumnIndex(field.getName())));
            }
            // 设置该属性名称与值
            field.set(t, value);
        }
    }

    public static <T> void setValue(T t, ResultSet rs) throws Exception {
        Field[] fields = t.getClass().getFields();
        for (Field field : fields) {
            Object value = null;
            String name = field.getType().getName();
            if (name == boolean.class.getName()) {
                value = rs.getBoolean(field.getName());
            } else if (name == int.class.getName()) {
                value = rs.getInt(field.getName());
            } else if (name == long.class.getName()) {
                value = rs.getLong(field.getName());
            } else if (name == double.class.getName()) {
                value = rs.getDouble(field.getName());
            } else if (name == String.class.getName()) {
                value = rs.getString(field.getName());
                if (value == null) value = "";
            } else if (name == Date.class.getName()) {
                value = rs.getDate(field.getName());
            } else if (name == Time.class.getName()) {
                value = rs.getTime(field.getName());
            }
            // 设置该属性名称与值
            field.set(t, value);
        }
    }

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

    public static <T> boolean iEquals(T t, T obj) throws Exception {
        Field[] fields = t.getClass().getFields();
        for (Field field : fields) {
            // 获取该属性名称与值
            Object value = field.get(t);
            Object value2 = field.get(obj);
            if (!value.equals(value2)) return false;
        }
        return true;
    }

    public static <T> void setValue(T t, T obj) throws Exception {
        Field[] fields = t.getClass().getFields();
        for (Field field : fields) {
            Object value = field.get(obj);
            field.set(t, value);
        }
    }
}
