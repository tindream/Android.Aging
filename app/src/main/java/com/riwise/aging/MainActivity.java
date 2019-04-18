package com.riwise.aging;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.AsyncLoad;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.CrashHandlers;
import com.riwise.aging.support.Method;

public class MainActivity extends MainBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config.load(this);
        Method.log("Version=" + this.getString(R.string.version));
        CrashHandlers.getInstance().init();
        super.layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        Config.context = this;
        new AsyncLoad().setListener(this);
        initFragments();
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.WRITE_CALL_LOG
        };
        Method.requestPower(permissions);
    }

    @Override
    public void onReady(LoadInfo info) {
        onReadyBase(info);
        ViewPager vp = findViewById(R.id.mainViewPager);
        switch (info.Types) {
            case home:
                vp.setCurrentItem(0, false);
                break;
        }
    }
}
