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
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/26.
 */

public class BView extends BaseView {
    HomeDelegate homeDelegate;
    ClickDelegate clickDelegate;
    //region ```
    @ViewInject(R.id.eiv_0)
    ExtendImageView eiv_0;
    @ViewInject(R.id.eiv_1)
    ExtendImageView eiv_1;
    @ViewInject(R.id.eiv_2)
    ExtendImageView eiv_2;

    //endregion
    public BView(Context context) {
        super(context);
        init();
    }

    public BView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        eiv_0.setOnClickListener(click);
        eiv_1.setOnClickListener(click);
        eiv_2.setOnClickListener(click);

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

    public void setInfo(HomeDelegate homeDelegate, ClickDelegate clickDelegate, final JSONObject json) {
        this.homeDelegate = homeDelegate;
        this.clickDelegate = clickDelegate;
        //region ```
        JSONArray jrr_left = JsonClass.jrrj(json, "left");
        eiv_0.setPath(JsonClass.sj(JsonClass.jjrr(jrr_left, 0), "ad_code"));
        eiv_0.setTag(new String[]{JsonClass.sj(JsonClass.jjrr(jrr_left, 0), "ad_link"),
                JsonClass.sj(JsonClass.jjrr(jrr_left, 0), "data_type"),
                JsonClass.sj(JsonClass.jjrr(jrr_left, 0), "num_iid")});

        JSONArray jrr_right = JsonClass.jrrj(json, "right");
        eiv_1.setPath(JsonClass.sj(JsonClass.jjrr(jrr_right, 0), "ad_code"));
        eiv_1.setTag(new String[]{JsonClass.sj(JsonClass.jjrr(jrr_right, 0), "ad_link"),
                JsonClass.sj(JsonClass.jjrr(jrr_right, 0), "data_type"),
                JsonClass.sj(JsonClass.jjrr(jrr_right, 0), "num_iid")});

        eiv_2.setPath(JsonClass.sj(JsonClass.jjrr(jrr_right, 1), "ad_code"));
        eiv_2.setTag(new String[]{JsonClass.sj(JsonClass.jjrr(jrr_right, 1), "ad_link"),
                JsonClass.sj(JsonClass.jjrr(jrr_right, 1), "data_type"),
                JsonClass.sj(JsonClass.jjrr(jrr_right, 1), "num_iid")});
        //endregion
    }
}
