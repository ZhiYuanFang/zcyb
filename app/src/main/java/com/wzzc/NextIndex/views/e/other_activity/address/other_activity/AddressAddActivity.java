package com.wzzc.NextIndex.views.e.other_activity.address.other_activity;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.address.other_activity.main_view.AddressClearView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/6/15.
 *
 */

public class AddressAddActivity extends BaseActivity {
    @ViewInject(R.id.acv)
    AddressClearView acv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        acv.setInfo(null , AddressClearView.ADD);
    }
}
