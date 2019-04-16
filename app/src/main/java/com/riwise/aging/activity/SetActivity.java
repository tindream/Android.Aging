package com.riwise.aging.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.riwise.aging.R;
import com.riwise.aging.enums.IListListener;
import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.loadInfo.SetInfo;
import com.riwise.aging.info.setInfo.AdapterInfo;
import com.riwise.aging.info.setInfo.LoaderInfo;
import com.riwise.aging.support.AsyncListView;
import com.riwise.aging.support.Config;
import com.riwise.aging.support.Method;
import com.riwise.aging.support.ViewHolder;
import com.riwise.aging.ui.ChildActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

public class SetActivity extends ChildActivity implements IListListener, ILoadListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_set;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<SetInfo> list = new ArrayList();
        list.add(new SetInfo());
        list.add(new SetInfo(getString(R.string.btn_10)));
        list.add(new SetInfo(getString(R.string.btn_18)));
        list.add(new SetInfo(getString(R.string.btn_24)));
        list.add(new SetInfo());
        list.add(new SetInfo(getString(R.string.btn_log)));
        list.add(new SetInfo(getString(R.string.btn_about), getString(R.string.version), true));
        new AsyncListView().setListener(this, this).init(this, list, R.layout.item_set);
    }

    @Override
    public <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        if (!(object instanceof SetInfo)) return;
        SetInfo obj = (SetInfo) object;

        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.set_name, obj.Message));
        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.set_desc, obj.desc));
        emitter.onNext(new LoaderInfo(LoadType.setImageId, holder, R.id.set_img, obj.imageId));
        if (obj.iHeard) {
            emitter.onNext(new LoaderInfo(LoadType.setLine, holder, R.id.set_name));
            emitter.onNext(new LoaderInfo(LoadType.setLine, holder, R.id.set_desc));
        }
        if (obj.noRight) {
            emitter.onNext(new LoaderInfo(LoadType.setNoRight, holder, R.id.set_right));
        }
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case setLine:
                LoaderInfo loader = (LoaderInfo) info;
                TextView textView = loader.holder.getView(loader.id);
                textView.setPadding(0, 0, 0, 0);
                textView.setBackgroundColor(getResources().getColor(R.color.colorGray));
                Method.setSize(textView, 0, 10 * Config.display.density);
                loader.holder.getView(R.id.set_right).setVisibility(View.GONE);
                break;
            case setNoRight:
                loader = (LoaderInfo) info;
                loader.holder.getView(loader.id).setVisibility(View.GONE);
                break;
            case setImageId:
                loader = (LoaderInfo) info;
                if (loader.imageId == 0) {
                    loader.holder.getView(loader.id).setVisibility(View.GONE);
                }
                break;
            case setAdapter:
                ListView listView = findViewById(R.id.set_listView);
                //设置listView的Adapter
                listView.setAdapter(((AdapterInfo) info).adapter);
                listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    //我们需要的内容，跳转页面或显示详细信息
                    SetInfo setInfo = (SetInfo) ((AdapterInfo) info).list.get(position);
                    if (setInfo.iHeard) return;
                    switch (setInfo.Message) {
                        case "日志":
                            Intent intent = new Intent(this, LogActivity.class);
                            //将text框中的值传入
                            intent.putExtra("title", "日志");
                            File file = new File(Environment.getExternalStorageDirectory(), "/Tinn/Aging/log.txt");
                            intent.putExtra("file", "file://" + file.toString());
                            startActivity(intent);
                            break;
                        case "关于":
                            Method.show(this);
                            break;
                    }
                });
                break;
        }
    }
}
