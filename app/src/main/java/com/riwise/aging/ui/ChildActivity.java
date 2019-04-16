package com.riwise.aging.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.riwise.aging.R;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.support.Method;

public class ChildActivity extends AppCompatActivity {
    protected int layoutResID;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(layoutResID);
            toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        } catch (Exception e) {
            Method.log(e);
        }
    }

    protected String getValue(int id) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        }
        if (view instanceof Spinner) {
            return ((LoadInfo) ((Spinner) view).getSelectedItem()).Message;
        }
        return null;
    }

    protected void setValue(int id, Object value) {
        View view = findViewById(id);
        if (view instanceof TextView) {
            ((TextView) view).setText(value.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
