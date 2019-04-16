package com.riwise.aging.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.riwise.aging.R;
import com.riwise.aging.enums.LoadType;

public class View_Ask extends View_Base {
    public void show(String msg) {
        super.show();
        alert.setCanceledOnTouchOutside(false);

        TextView textView = view_custom.findViewById(R.id.ask_msg);
        textView.setText(msg);
        textView = view_custom.findViewById(R.id.ask_confirm);
        textView.setText(R.string.btn_confirm);
        textView = view_custom.findViewById(R.id.ask_cancel);
        textView.setText(R.string.btn_cancel);
    }

    @Override
    protected boolean onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.ask_cancel:
                onListener(LoadType.cancel);
                break;
            case R.id.ask_confirm:
                onListener(LoadType.confirm);
                break;
        }
        return super.onBtnClick(v);
    }

    public void init(Activity activity) {
        super.init(activity, R.layout.view_ask, new int[]{R.id.ask_cancel, R.id.ask_confirm});
    }
}
