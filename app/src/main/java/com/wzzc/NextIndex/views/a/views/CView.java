package com.wzzc.NextIndex.views.a.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.wzzc.NextIndex.views.a.HomeDelegate;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;

/**
 * Created by by Administrator on 2017/6/26.
 * <p>
 * 广告
 */

public class CView extends BaseView {
    HomeDelegate homeDelegate;
    ClickDelegate clickDelegate;
    //region ```
    @ViewInject(R.id.eiv_c)
    ExtendImageView eiv_c;

    //endregion
    public CView(Context context) {
        super(context);
        init();
    }

    public CView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        eiv_c.setOnClickListener(click);
    }

    //region Action
    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] tags = (String[]) v.getTag();
            String da_link = tags[0];
            String data_type = tags[1];
            String num_iid = tags[2];

            if (!ItemClick.switchNormalListener(data_type, da_link)) {
                ItemClick.judgeSpecialListener(clickDelegate,v,data_type,da_link,num_iid);
            }
        }
    };
    //endregion

    public void setInfo(HomeDelegate homeDelegate, ClickDelegate clickDelegate, final JSONArray jrr) {
        this.homeDelegate = homeDelegate;
        this.clickDelegate = clickDelegate;

        eiv_c.setPath(JsonClass.sj(JsonClass.jjrr(jrr, 0), "ad_code"));
        eiv_c.setTag(new String[]{JsonClass.sj(JsonClass.jjrr(jrr, 0), "ad_link"),
                JsonClass.sj(JsonClass.jjrr(jrr, 0), "data_type"),
                JsonClass.sj(JsonClass.jjrr(jrr, 0), "num_iid")});

    }
}
