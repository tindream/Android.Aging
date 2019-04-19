package com.riwise.aging.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.riwise.aging.R;
import com.riwise.aging.data.SQLiteServer;
import com.riwise.aging.info.sqlInfo.AgingInfo;
import com.riwise.aging.support.Cache;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.Method;
import com.riwise.aging.ui.ChildActivity;

public class AgingActivity extends ChildActivity implements SeekBar.OnSeekBarChangeListener {
    private AgingInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_aging;
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        info = Cache.FindAging(title, Config.I32);

        SeekBar seekBar = findViewById(R.id.aging_4K);
        seekBar.setTag(R.id.aging_4Ks);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(info.File4);

        seekBar = findViewById(R.id.aging_8K);
        seekBar.setTag(R.id.aging_8Ks);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(info.File8);

        seekBar = findViewById(R.id.aging_128K);
        seekBar.setTag(R.id.aging_128Ks);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(info.File128);
        setValue(R.id.aging_file, info.File);
        {
            setValue(R.id.aging_last, info.Last);
            setValue(R.id.aging_image, info.Image);
            setValue(R.id.aging_audio, info.Audio);
            setValue(R.id.aging_video, info.Video);
            setValue(R.id.aging_contact, info.Contact);
            setValue(R.id.aging_sms, info.Sms);
            setValue(R.id.aging_call, info.Call);
            setValue(R.id.aging_app, info.App);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = (int) seekBar.getTag();
        setValue(id, progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                info.File4 = getSeekBar(R.id.aging_4K);
                info.File8 = getSeekBar(R.id.aging_8K);
                info.File128 = getSeekBar(R.id.aging_128K);
                info.File = getValue(R.id.aging_file);
                info.Last = Double.parseDouble(getValue(R.id.aging_last));
                info.Image = Integer.parseInt(getValue(R.id.aging_image));
                info.Audio = Integer.parseInt(getValue(R.id.aging_audio));
                info.Video = Integer.parseInt(getValue(R.id.aging_video));
                info.Contact = Integer.parseInt(getValue(R.id.aging_contact));
                info.Sms = Integer.parseInt(getValue(R.id.aging_sms));
                info.Call = Integer.parseInt(getValue(R.id.aging_call));
                info.App = Integer.parseInt(getValue(R.id.aging_app));
                try {
                    new SQLiteServer().update(info);
                    Method.hit("保存完成");
                    onBackPressed();
                } catch (Exception e) {
                    Method.log(e);
                    Method.hit(e.getMessage());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
