package com.riwise.aging.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;

import com.riwise.aging.R;
import com.riwise.aging.activity.TestActivity;
import com.riwise.aging.enums.IListListener;
import com.riwise.aging.enums.ILoadListener;
import com.riwise.aging.enums.LoadType;
import com.riwise.aging.info.loadInfo.SetInfo;
import com.riwise.aging.info.setInfo.AdapterInfo;
import com.riwise.aging.info.loadInfo.LoadInfo;
import com.riwise.aging.info.setInfo.LoaderInfo;
import com.riwise.aging.support.AsyncListView;
import com.riwise.aging.support.ViewHolder;

public class Fragment_Home extends Fragment_Base implements IListListener, ILoadListener {
    private boolean isComplete;
    private LoadInfo error;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_home, container, false);
        return messageLayout;
    }

    private void onClick(String text) {
        switch (text) {
            case "老化测试模型":
            case "10个月老化模型":
            case "18个月老化模型":
            case "24个月老化模型":
                Intent intent = new Intent(getActivity(), TestActivity.class);
                //将text框中的值传入
                intent.putExtra("title", text);
                startActivity(intent);
                return;
        }
    }

    //去服务器下载数据
    //仅加载一次
    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        super.load(R.id.home_context, R.id.home_load, R.id.home_text, false);

        List<SetInfo> list = new ArrayList();
        list.add(new SetInfo(getString(R.string.btn_0)));
        list.add(new SetInfo(getString(R.string.btn_10)));
        list.add(new SetInfo(getString(R.string.btn_18)));
        list.add(new SetInfo(getString(R.string.btn_24)));

        new AsyncListView().setListener(this, this).init(getActivity(), list, R.layout.item_grid);
        load(isComplete);
        if (error != null) error(error);
    }

    @Override
    public <T> void onReady(ObservableEmitter<LoadInfo> emitter, ViewHolder holder, T object) {
        if (!(object instanceof SetInfo)) return;
        SetInfo obj = (SetInfo) object;

        emitter.onNext(new LoaderInfo(LoadType.setText, holder, R.id.grid_name, obj.Message));
    }

    @Override
    public void onReady(LoadInfo info) {
        switch (info.Types) {
            case setAdapter:
                GridView listView = getActivity().findViewById(R.id.home_gridView1);
                //设置listView的Adapter
                listView.setAdapter(((AdapterInfo) info).adapter);
                listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                    //我们需要的内容，跳转页面或显示详细信息
                    SetInfo setInfo = (SetInfo) ((AdapterInfo) info).list.get(position);
                    onClick(setInfo.Message);
                });
                break;
        }
    }

    public void load(boolean complete) {
        if (!isFirstVisible) {
            this.isComplete = complete;
            return;
        }
        super.load(R.id.home_context, R.id.home_load, R.id.home_text, complete);
    }

    public void error(LoadInfo info) {
        if (!isFirstVisible) {
            this.error = info;
            return;
        }
        TextView textView = getActivity().findViewById(R.id.home_text);
        String desc = info.Types + "\n" + info.Message;
        int end = desc.indexOf("\n");
        int color = getActivity().getResources().getColor(R.color.colorRed);
        SpannableString span = new SpannableString(desc);
        span.setSpan(new ForegroundColorSpan(color), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }
}
