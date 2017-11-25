package com.wzzc.other_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/6.
 */

public class NoNetView extends BaseView {
    @ViewInject(R.id.bt_reload)
    Button bt_reload;
    public NoNetView(Context context) {
        super(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        bt_reload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Default.isConnect(getContext())) {
                    GetBaseActivity().refresh();
                }
            }
        });

        bt_reload.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        bt_reload.setTextColor(ContextCompat.getColor(getContext() , R.color.tv_Red));
                        break;
                    case MotionEvent.ACTION_UP:
                        bt_reload.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                        break;
                }
                return false;
            }
        });
    }
}
