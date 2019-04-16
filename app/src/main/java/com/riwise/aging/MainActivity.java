package com.riwise.aging;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.AsyncLoad;
import com.riwise.aging.support.AsyncWait;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.CrashHandlers;
import com.riwise.aging.support.Method;
import com.riwise.aging.ui.View_Navigation;

public class MainActivity extends MainBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Method.log("Version=" + this.getString(R.string.version));
        Config.load(this);
        CrashHandlers.getInstance().init();
        super.layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        Config.context = this;
        new AsyncLoad().setListener(this);
        initFragmentsHome();
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        };
        Method.requestPower(permissions);
        ViewPager vp = findViewById(R.id.mainViewPager);
        vp.addOnPageChangeListener(this);
    }

    @Override
    public void onReady(LoadInfo info) {
        onReadyBase(info);
        ViewPager vp = findViewById(R.id.mainViewPager);
        switch (info.Types) {
            case home:
                vp.setCurrentItem(0, false);
                break;
            case report:
                vp.setCurrentItem(1, false);
                break;
            case order:
                vp.setCurrentItem(2, false);
                break;
            case my:
                vp.setCurrentItem(3, false);
                break;
        }
        switch (info.Types) {
            case home:
            case report:
            case order:
            case my:
                View_Navigation navigation = findViewById(R.id.view_Navigation);
                navigation.onClick(info.Types);
                break;
        }
    }
}
