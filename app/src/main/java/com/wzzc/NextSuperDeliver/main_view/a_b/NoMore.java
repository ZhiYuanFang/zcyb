package com.wzzc.NextSuperDeliver.main_view.a_b;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/24.
 */
@ContentView(R.layout.no_more)
public class NoMore extends BaseView {
    @ViewInject(R.id.tv_nomore)
    TextView tv_noMore;
    public NoMore(Context context) {
        super(context);
    }

    public NoMore(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInfo (String noMore) {
        if (noMore == null) {
            return;
        }
        tv_noMore.setText(noMore);
    }
}
