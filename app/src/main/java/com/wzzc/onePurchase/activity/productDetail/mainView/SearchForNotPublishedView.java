package com.wzzc.onePurchase.activity.productDetail.mainView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/24.
 */

public class SearchForNotPublishedView extends BaseView {
    @ViewInject(R.id.tv_category)
    private TextView tv_info;
    @ViewInject(R.id.tv_searchMore)
    private TextView tv_searchMore;
    public SearchForNotPublishedView(Context context) {
        super(context);
        init();
    }

    public SearchForNotPublishedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

    }

    public void setInfo(String info , OnClickListener searchClickListener){
        this.tv_info.setText(info);
        this.tv_searchMore.setOnClickListener(searchClickListener);
    }
}
