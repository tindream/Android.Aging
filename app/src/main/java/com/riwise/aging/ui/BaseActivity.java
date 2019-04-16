package com.riwise.aging.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.riwise.aging.R;
import com.riwise.aging.support.Method;

public class BaseActivity extends AppCompatActivity {
    protected int layoutResID;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(layoutResID);
            toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitleTextColor(Color.WHITE);
                setSupportActionBar(toolbar);
            }
        } catch (Exception e) {
            Method.log(e);
        }
    }

    //按返回键不销毁当前Activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
