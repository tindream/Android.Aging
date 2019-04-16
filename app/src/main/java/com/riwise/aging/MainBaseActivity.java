package com.riwise.aging;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.fragments.Fragment_Home;
import com.riwise.aging.fragments.Fragment_My;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.FragmentAdapter;
import com.riwise.aging.support.Method;
import com.riwise.aging.ui.BaseActivity;
import com.riwise.aging.ui.View_Navigation;

public class MainBaseActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, ILoadListener {
    protected List<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    public void onReady(LoadInfo info) {
    }

    //横坚屏切换
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Config.load(this);
        if (Config.view_base != null)
            Config.view_base.autoSize();
    }

    protected void initFragmentsHome() {
        Fragment_Home fragment_home = new Fragment_Home();
        fragment_home.setListener(this);
        fragmentList.add(fragment_home);

        ViewPager vp = findViewById(R.id.mainViewPager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        vp.setAdapter(fragmentAdapter);
    }

    protected void initFragmentsOther() {
        View_Navigation navigation = findViewById(R.id.view_Navigation);
        navigation.setCustomListener(this);

        Fragment_My fragment_my = new Fragment_My();
        fragment_my.setListener(this);
        fragmentList.add(fragment_my);

        ViewPager vp = findViewById(R.id.mainViewPager);
        vp.getAdapter().notifyDataSetChanged();
    }

    protected void onReadyBase(LoadInfo info) {
        switch (info.Types) {
            case load:
                Config.load();
                initFragmentsOther();
                onReady(new LoadInfo(LoadType.home));
                break;
            case Error:
                Fragment_Home fragment_home = (Fragment_Home) fragmentList.get(0);
                fragment_home.error(info);
                break;
            case complete:
                onReady(new LoadInfo(LoadType.home));
                fragment_home = (Fragment_Home) fragmentList.get(0);
                fragment_home.load(true);
                break;
        }
    }

    //PopupWindow的监听回调事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onPageSelected(int position) {
        View_Navigation navigation = findViewById(R.id.view_Navigation);
        switch (position) {
            case 0:
                navigation.onClick(LoadType.home);
                break;
            case 1:
                navigation.onClick(LoadType.report);
                break;
            case 2:
                navigation.onClick(LoadType.order);
                break;
            case 3:
                navigation.onClick(LoadType.my);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
