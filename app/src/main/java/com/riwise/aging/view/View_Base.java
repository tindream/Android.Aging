package com.riwise.aging.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.riwise.aging.R;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.ListenerBase;
import com.riwise.aging.support.Method;

public abstract class View_Base extends ListenerBase {
    public Dialog alert;
    public Activity activity;
    protected View view_custom;
    protected static boolean onlyone;

    public void show() {
        if (onlyone) return;
        onlyone = true;
        alert.setCanceledOnTouchOutside(true);
        alert.setOnCancelListener(dialog -> {
            onlyone = false;
        });
        alert.show();
        autoSize();
    }

    public void autoSize() {
        autoSize(85);
    }

    protected void autoSize(int rateWeight) {
        autoWeight(rateWeight);
    }

    protected void autoWeight(int rate) {
        WindowManager.LayoutParams params = alert.getWindow().getAttributes();  //获取对话框当前的参数值、
        params.width = Config.display.widthPixels * rate / 100;    //宽度设置全屏宽度
        alert.getWindow().setAttributes(params);     //设置生效
    }

    protected int autoWeight(int id, int rate) {
        ViewGroup.LayoutParams params = alert.getWindow().getAttributes();  //获取对话框当前的参数值、
        View view = view_custom.findViewById(id);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = params.width * rate / 100;
        view.setLayoutParams(layoutParams);
        return layoutParams.width;
    }

    protected void autoHeight(int id, double rate) {
        View view = view_custom.findViewById(id);
        double height = Config.display.heightPixels * rate / 100;
        Method.setSize(view, 0, height);
    }

    protected boolean onBtnClick(View v) {
        return true;
    }

    protected void init(Activity activity, int viewId) {
        init(activity, viewId, new int[]{});
    }

    protected void init(Activity activity, int viewId, int[] btnId) {
        this.activity = activity;
        this.alert = new Dialog(activity, R.style.dialogStyle);
        //加载自定义的那个View,同时设置下
        LayoutInflater inflater = activity.getLayoutInflater();
        view_custom = inflater.inflate(viewId, null, false);
        alert.setContentView(view_custom);
        //按返回键不能退出
        alert.setCancelable(false);
        //外部背景透明
//        alert.getWindow().setDimAmount(0);
        Method.loadLanguage();

        if (btnId == null) return;
        for (int id : btnId) {
            view_custom.findViewById(id).setOnClickListener(v -> {
                if (onBtnClick(v)) alert.cancel();
            });
        }
    }
}
