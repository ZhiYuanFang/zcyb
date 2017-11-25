package com.wzzc.NextIndex.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.CallPhone;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/5.
 */
@ContentView(R.layout.b_view)
public class B extends BaseView{
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    public B(Context context) {
        super(context);
        init();
    }

    public B(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        tv_phone.setText(CallPhone.KEFU);
        tv_phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tv_phone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CallPhone.call(CallPhone.KEFU);
            }
        });
    }
}
