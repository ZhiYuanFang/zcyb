package com.wzzc.other_view.production.list;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.list.main_view.BrowseProductionView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/9.
 */

public class BrowseProductionActivity extends BaseActivity {
    private String typeValue;
    private String url;
    private String keyWords;
    private Integer category_id;
    private String price_range;
    private Integer brand_id;
    private String intro;
    BrowseProductionView browseProductionView;
    @ViewInject(R.id.main_view)
    LinearLayout main_view;

    /**
     * 打包传输关键字
     */
    public static final String TYPEID = "type", KEYWORDS = "keyWords", CATEGORY_ID = "category_id", PRICE_RANGE = "price_range", BRAND_ID = "brand_id", INTRO = "intro", SORTBY = "sort_by";
    /**
     * 打包传输 TYPEID 内容
     */
    public static final String TYPEVALUE_ZCB_PRODUCTION = "zcb_production", TYPEVALUE_GCB_PRODUCTION = "gcb_production";

    /**
     * 打包传输 SORTBY 内容
     */
    public static final Integer SORT_INTEGRATED = 0, SORT_SALES = 1, SORT_NEW_PRODUCTION = 2, SORT_PRICE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        browseProductionView = new BrowseProductionView(this);
        main_view.addView(browseProductionView);
        typeValue = (String) GetIntentData(TYPEID);

        if (GetIntentData(CATEGORY_ID) != null) {
            category_id = Integer.valueOf(String.valueOf(GetIntentData(CATEGORY_ID)));
        }
        if (GetIntentData(PRICE_RANGE) != null) {
            price_range = String.valueOf(GetIntentData(PRICE_RANGE));
        }
        if (GetIntentData(BRAND_ID) != null) {
            brand_id = Integer.parseInt(String.valueOf(GetIntentData(BRAND_ID)));
        }
        if (GetIntentData(INTRO) != null) {
            intro = String.valueOf(GetIntentData(INTRO));
        }
        if (GetIntentData(KEYWORDS) != null) {
            keyWords = String.valueOf(GetIntentData(KEYWORDS)).trim();
        }

        Integer sby = (Integer) GetIntentData(SORTBY);
        browseProductionView.setInfo(typeValue , category_id , price_range , brand_id , intro , keyWords , sby);
    }
}
