package com.wzzc.onePurchase.activity.specialProduction;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.specialProduction.main_view.SpecialProductionView;
import com.wzzc.onePurchase.activity.specialProduction.main_view.SpecialTitleView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/30.
 *
 * 特别产品界面（首页/所有商品/最新揭晓/晒单/限时）
 */

public class SpecialProductionActivity extends BaseActivity implements SpecialSearchDelegate{

    public static final String CURRENTITEM = "currentItem";
    public static final String PUBLISHED = "published", SHOWORDER = "showOrder";

    @ViewInject(R.id.basicLayout)
    OnePurchasePanelBasicLayoutView onePurchasePanelBasicLayoutView;
    SpecialProductionView specialProductionView;
    String currentItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        currentItem = bundle.getString(CURRENTITEM);

        SpecialTitleView specialTitleView = new SpecialTitleView(this);
        specialTitleView.setInfo(this);
        specialProductionView = new SpecialProductionView(this);
        specialProductionView.setInfo(currentItem,"");
        onePurchasePanelBasicLayoutView.setInfo(specialTitleView,specialProductionView);

    }

    @Override
    public void search(String keyWords) {
        specialProductionView.setInfo(currentItem,keyWords);
    }
}
