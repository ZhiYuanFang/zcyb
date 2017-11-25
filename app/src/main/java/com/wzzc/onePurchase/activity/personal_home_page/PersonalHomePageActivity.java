package com.wzzc.onePurchase.activity.personal_home_page;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.personal_home_page.main_view.PersonalHomePageView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/28.
 *
 * 个人主页
 */

public class PersonalHomePageActivity extends BaseActivity {
    @ViewInject(R.id.basicLayout)
    private OnePurchasePanelBasicLayoutView onePurchasePanelBasicLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnePurchasePanelBasicTitleView onePurchasePanelBasicTitleView = new OnePurchasePanelBasicTitleView(this);
        onePurchasePanelBasicTitleView.setTv_title(getString(R.string.personalHome));
        PersonalHomePageView personalHomePageView = new PersonalHomePageView(this);
        personalHomePageView.setInfo(null);
        onePurchasePanelBasicLayoutView.setInfo(onePurchasePanelBasicTitleView,personalHomePageView);
        onePurchasePanelBasicLayoutView.setTop_height(Default.dip2px(115,this));
    }
}
