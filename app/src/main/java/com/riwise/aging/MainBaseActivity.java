package com.riwise.aging;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.riwise.aging.activity.SetActivity;
import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.fragments.Fragment_Home;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.FragmentAdapter;
import com.riwise.aging.ui.BaseActivity;

public class MainBaseActivity extends BaseActivity implements ILoadListener {
    protected List<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    public void onReady(LoadInfo info) {
    }

    protected void initFragments() {
        Fragment_Home fragment_home = new Fragment_Home();
        fragment_home.setListener(this);
        fragmentList.add(fragment_home);

        ViewPager vp = findViewById(R.id.mainViewPager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        vp.setAdapter(fragmentAdapter);
    }

    protected void onReadyBase(LoadInfo info) {
        switch (info.Types) {
            case load:
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set:
                Intent intent = new Intent(this, SetActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }
}
