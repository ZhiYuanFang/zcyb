package com.wzzc.NextSuperDeliver.main_view.a_b;

import android.content.Context;
import android.util.AttributeSet;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/8/24.
 * 品牌 banner
 */
@ContentView(R.layout.layout_banner)
public class BannerView extends BaseView {
    @ViewInject(R.id.eiv_branner)
    ExtendImageView eiv_branner;
    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInfo (JSONObject jsonBanner) {
//        eiv_branner.setScaleType(ImageView.ScaleType.CENTER_CROP);
        eiv_branner.setPath(JsonClass.sj(jsonBanner,"banner"));
    }
}
