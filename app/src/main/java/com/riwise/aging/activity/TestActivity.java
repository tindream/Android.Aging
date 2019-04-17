package com.riwise.aging.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.riwise.aging.R;
import com.riwise.aging.enums.IListListener;
import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.loadInfo.ProgressInfo;
import com.riwise.aging.info.loadInfo.SetInfo;
import com.riwise.aging.info.setInfo.AdapterInfo;
import com.riwise.aging.info.setInfo.LoaderInfo;
import com.riwise.aging.info.sqlInfo.AgingInfo;
import com.riwise.aging.support.AsyncAging;
import com.riwise.aging.support.AsyncListView;
import com.riwise.aging.support.Cache;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.Method;
import com.riwise.aging.support.MyAdapter;
import com.riwise.aging.support.ViewHolder;
import com.riwise.aging.ui.ChildActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

public class TestActivity extends ChildActivity implements View.OnClickListener, IListListener, ILoadListener {
    private AgingInfo info;
    private AsyncAging aging;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_test;
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        info = Cache.FindAging(title, Config.I32);
        findViewById(R.id.test_btn).setOnClickListener(this);
        loadListview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_btn:
                TextView textView = findViewById(R.id.test_btn);
                if (textView.getText().equals("开始")) start();
                else stop();
                break;
        }
    }

    private void exec(LoadInfo info) {
        switch (info.Types) {
            case progress:
                ProgressInfo temp = (ProgressInfo) info;
                ProgressBar progress = findViewById(R.id.test_progressBar);
                progress.setProgress(temp.progress);

                ListView listView = findViewById(R.id.test_listView);
                MyAdapter adapter = (MyAdapter) listView.getAdapter();
                List infoList = adapter.get();
                for (int i = 0; i < infoList.size(); i++) {
                    SetInfo setInfo = (SetInfo) infoList.get(i);
                    if (setInfo.Message.equals(temp.name)) {
                        setInfo.updateTest(temp.desc, temp.loading, temp.complete);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
            case complete:
                Method.hit(info.Types + "");
                stop();
                break;
            case cancel:
                Method.hit(info.Types + "");
                break;
            case error:
                Method.hit(info.getMsg());
                break;
        }
    }

    private void start() {
        TextView textView = findViewById(R.id.test_btn);
        textView.setText("停止");
        textView.setBackgroundResource(R.drawable.selector_stroke_radiu_goldlight);
        ListView listView = findViewById(R.id.test_listView);
        MyAdapter adapter = (MyAdapter) listView.getAdapter();
        List infoList = adapter.get();
        for (int i = 0; i < infoList.size(); i++) {
            SetInfo setInfo = (SetInfo) infoList.get(i);
            setInfo.updateTest(setInfo.desc, false, false);
        }
        adapter.notifyDataSetChanged();
        aging = new AsyncAging(info);
        aging.setListener(this);
    }

    private void stop() {
        TextView textView = findViewById(R.id.test_btn);
        textView.setText("开始");
        if (aging != null) aging.stop();
        textView.setBackgroundResource(R.drawable.selector_stroke_radiu_primarylight);
    }

    private void loadListview() {
        List<SetInfo> list = new ArrayList();
        list.add(new SetInfo("剩余空间", info.Last + "G(" + (Config.I32 ? "" : ">") + " 32G)", true));
        list.add(new SetInfo("碎片化", ""));
        list.add(new SetInfo("图片", info.Image + ""));
        list.add(new SetInfo("音频", info.Audio + ""));
        list.add(new SetInfo("视频", info.Video + ""));
        list.add(new SetInfo("联系人", info.Contact + ""));
        list.add(new SetInfo("信息", info.Sms + ""));
        list.add(new SetInfo("通话记录", info.Call + ""));
        list.add(new SetInfo("三方应用", info.App + ""));
        list.add(new SetInfo("填充文件", info.File4 + ":" + info.File8 + ":" + info.File128));
        new AsyncListView().setListener(this, this).init(this, list, R.layout.item_test);
    }

    @Override
    public <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        if (!(object instanceof SetInfo)) return;
        SetInfo obj = (SetInfo) object;

        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.test_name, obj.Message));
        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.test_desc, obj.desc));
        emitter.onNext(new LoaderInfo(LoadType.setImageId, holder, R.id.test_img, obj.imageId));
        emitter.onNext(new LoaderInfo(LoadType.setBackColor, holder, R.id.test_conext, obj.colorId));
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case setNoRight:
                LoaderInfo loader = (LoaderInfo) info;
                boolean noRight = Boolean.parseBoolean(loader.msg);
                loader.holder.getView(loader.id).setVisibility(noRight ? View.GONE : View.VISIBLE);
                break;
            case setImageId:
                loader = (LoaderInfo) info;
                loader.holder.getView(loader.id).setVisibility(loader.imageId == 0 ? View.GONE : View.VISIBLE);
                break;
            case setBackColor:
                loader = (LoaderInfo) info;
                LinearLayout view = loader.holder.getView(loader.id);
                view.setBackgroundColor(getResources().getColor(loader.imageId > 0 ? loader.imageId : R.color.Transparent));
                TextView textView = loader.holder.getView(R.id.test_name);
                textView.setTextColor(getResources().getColor(loader.imageId > R.color.Transparent ? R.color.While : R.color.BlackLight));
                textView = loader.holder.getView(R.id.test_desc);
                textView.setTextColor(getResources().getColor(loader.imageId > R.color.Transparent ? R.color.While : R.color.colorGrayDark));
                break;
            case setAdapter:
                ListView listView = findViewById(R.id.test_listView);
                //设置listView的Adapter
                listView.setAdapter(((AdapterInfo) info).adapter);
                break;
            default:
                exec(info);
                break;
        }
    }
}
