package com.wzzc.NextIndex.views.e.other_activity.address.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.address.AddressDelegate;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/6/14.
 *
 */

public class AddressNoDataView extends BaseView{
    @ViewInject(R.id.tv_add_address)
    TextView tv_add_address;

    AddressDelegate ad;
    public AddressNoDataView(AddressDelegate ad , Context context) {
        super(context);
        this.ad = ad;
        init();
    }

    public AddressNoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init(){
        tv_add_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    ad.addAddress();
            }
        });
    }
}
