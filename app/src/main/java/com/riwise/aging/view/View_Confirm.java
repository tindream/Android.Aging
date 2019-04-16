package com.riwise.aging.view;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.riwise.aging.R;
import com.riwise.aging.enums.LoadType;

public class View_Confirm extends View_Base {
    public void show(String msg) {
        super.show();
        alert.setCanceledOnTouchOutside(false);

        TextView textView = view_custom.findViewById(R.id.confirm_msg);
        textView.setText(msg);
        textView = view_custom.findViewById(R.id.confirm_btn);
        textView.setText(R.string.btn_confirm);
    }

    @Override
    protected boolean onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_btn:
                onListener(LoadType.confirm);
                break;
        }
        return super.onBtnClick(v);
    }

    public void init(Activity activity) {
        super.init(activity, R.layout.view_confirm, new int[]{R.id.confirm_btn});
    }
}
