package com.wzzc.onePurchase.activity.index.main_view.center.activity.accountRecharge;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.accountRecharge.main_view.AccountRechargeView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/28.
 *
 * 账户充值
 */

public class AccountRechargeActivity extends BaseActivity {
    public static final String RECID = "rec_id";
    public static final String CURRENT = "current";
    @ViewInject(R.id.basicLayout)
    private OnePurchasePanelBasicLayoutView onePurchasePanelBasicLayoutView;AccountRechargeView accountRechargeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init () {

        Bundle bundle = getIntent().getExtras() ;
        JSONObject sender = null;
        try {
            sender = new JSONObject(String.valueOf(bundle.get("info")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OnePurchasePanelBasicTitleView onePurchasePanelBasicTitleView = new OnePurchasePanelBasicTitleView(this);
        onePurchasePanelBasicTitleView.setTv_title(getString(R.string.accountRecharge));
        accountRechargeView = new AccountRechargeView(this);
        accountRechargeView.setInfo(sender);
        onePurchasePanelBasicLayoutView.setInfo(onePurchasePanelBasicTitleView,accountRechargeView);
    }
}
