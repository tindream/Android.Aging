package com.riwise.aging.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.riwise.aging.R;
import com.riwise.aging.info.sqlInfo.AgingInfo;
import com.riwise.aging.support.Cache;
import com.riwise.aging.ui.ChildActivity;

public class AgingSetActivity extends ChildActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_agingset;
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        AgingInfo info = Cache.FindAging(title);
//            setValue(R.id.login_user, "3");
//            setValue(R.id.login_pad, "1");
        findViewById(R.id.aging_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aging_btn:
                break;
        }
    }
}
