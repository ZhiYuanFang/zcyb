package com.wzzc.other_function.jpush;

import android.os.Bundle;
import android.widget.TextView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/4/25.
 * todo
 * JPush 推送界面
 */

public class JPushActivity extends BaseActivity {

    //region 组件
    @ViewInject(R.id.tv_jpush)
    private TextView tv_jpush;
    //endregion
    public static final String MSG = "msg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_jpush.setText(GetIntentData(MSG).toString());
    }
}
