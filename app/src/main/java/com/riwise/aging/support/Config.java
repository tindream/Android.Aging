package com.riwise.aging.support;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.File;
import java.util.Currency;
import java.util.Locale;

import com.riwise.aging.R;
import com.riwise.aging.info.sqlInfo.AdminInfo;
import com.riwise.aging.view.View_Base;

public class Config {
    public static final String Text = "Aging";
    public static final String Loading = "Loading...";

    public static AdminInfo Admin = new AdminInfo();
    public static Activity context;
    public static DisplayMetrics display;
    public static File file;
    public static boolean I32;

    public static void load(Activity activity) {
        file = new File(Environment.getExternalStorageDirectory(), "/Tinn/Aging");
        if (!file.exists()) file.mkdirs();
        display = Method.getDisplay(activity);
    }
}