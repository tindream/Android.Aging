package com.riwise.aging.support;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.util.DisplayMetrics;

import java.util.Currency;
import java.util.Locale;

import com.riwise.aging.info.sqlInfo.AdminInfo;
import com.riwise.aging.view.View_Base;

public class Config {
    public static final String Text = "aging";
    public static final String Loading = "Loading...";

    public static AdminInfo Admin = new AdminInfo();

    public static Activity context;
    public static DisplayMetrics display;

    public static void load(Activity activity) {
        display = Method.getDisplay(activity);
    }
}