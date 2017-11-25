package com.wzzc.NextIndex.views.e.other_activity.redpackage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/2/25.
 */

public class UseRedActivity extends BaseActivity {
    @ViewInject(R.id.bt_addred)
    Button bt_addred;
    @ViewInject(R.id.et_serial)
    EditText et_serial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("我的红包");

        bt_addred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.showToast("Developing..." , Toast.LENGTH_LONG);
            }
        });
    }
}
