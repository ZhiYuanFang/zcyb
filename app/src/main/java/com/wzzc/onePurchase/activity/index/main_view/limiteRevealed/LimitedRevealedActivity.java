package com.wzzc.onePurchase.activity.index.main_view.limiteRevealed;


import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.limiteRevealed.mianView.LimitedRevealedMainView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/22.
 *
 * 限时揭晓
 */

public class LimitedRevealedActivity extends BaseActivity {
    @ViewInject(R.id.basicLayout)
    OnePurchasePanelBasicLayoutView basicLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnePurchasePanelBasicTitleView titleView = new OnePurchasePanelBasicTitleView(this);
        titleView.setTv_title(getString(R.string.slowTimeAnnoucement));
        LimitedRevealedMainView mainView = new LimitedRevealedMainView(this);
        mainView.setInfo();
        basicLayoutView.setInfo(titleView,mainView);
    }
}
