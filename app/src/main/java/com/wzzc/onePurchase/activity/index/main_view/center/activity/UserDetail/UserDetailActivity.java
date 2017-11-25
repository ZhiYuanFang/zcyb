package com.wzzc.onePurchase.activity.index.main_view.center.activity.UserDetail;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.UserDetail.main_view.UserDetailView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/30.
 *
 * 账户明细
 */

public class UserDetailActivity extends BaseActivity {

    public static final String RECID = "rec_id";
    public static final String CURRENT = "current";
    @ViewInject(R.id.basicLayout)
    OnePurchasePanelBasicLayoutView onePurchasePanelBasicLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init () {
        Bundle bundle = getIntent().getExtras();
        JSONObject sender = null;
        try {
            sender = new JSONObject(bundle.getString("info"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OnePurchasePanelBasicTitleView onePurchasePanelBasicTitleView = new OnePurchasePanelBasicTitleView(this);
        onePurchasePanelBasicTitleView.setTv_title(getString(R.string.user_detail));
        UserDetailView userDetailView = new UserDetailView(this);
        userDetailView.setInfo(sender);
        onePurchasePanelBasicLayoutView.setInfo(onePurchasePanelBasicTitleView,userDetailView);

    }
}
