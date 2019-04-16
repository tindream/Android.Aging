package com.riwise.aging.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.riwise.aging.R;
import com.riwise.aging.support.Method;
import com.riwise.aging.ui.ChildActivity;

public class WebActivity extends ChildActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.layoutResID = R.layout.activity_web;
        super.onCreate(savedInstanceState);
        try {
            String title = getIntent().getStringExtra("title");
            toolbar.setTitle(title);
            String file = getIntent().getStringExtra("file");
            Method.log(file);

            WebView web = findViewById(R.id.webView1);
            //支持javascript
//            web.getSettings().setJavaScriptEnabled(true);
            // 设置可以支持缩放
            web.getSettings().setSupportZoom(true);
            // 设置是否可缩放，会出现缩放工具
            web.getSettings().setBuiltInZoomControls(true);
            //隐藏缩放工具
            web.getSettings().setDisplayZoomControls(false);
            //自适应屏幕
            web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            //wView.loadUrl("file:///android_asset/index.html");
            web.loadUrl(file);

        } catch (Exception e) {
            Method.log(e);
        }
    }
}
