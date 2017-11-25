package com.wzzc.other_view;

import android.content.Context;
import android.util.AttributeSet;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/5/2.
 */
@ContentView(R.layout.view_nodata)
public class NoDataView extends BaseView {
    public NoDataView(Context context) {
        super(context);
    }

    public NoDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


}
