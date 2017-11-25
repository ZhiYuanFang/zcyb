package com.wzzc.onePurchase.activity.index.main_view.center.activity.beginnerGuide;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.beginnerGuide.main_view.BeginnerGuideView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/3/28.
 */

public class BeginnerGuideActivity extends BaseActivity {
    public static final String RECID = "rec_id";
    @ViewInject(R.id.basicLayout)
    private OnePurchasePanelBasicLayoutView onePurchasePanelBasicLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init () {
        Bundle bundle = getIntent().getExtras();
        JSONObject json = null;
        try {
            json = new JSONObject(String.valueOf(bundle.get("info")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OnePurchasePanelBasicTitleView onePurchasePanelBasicTitleView = new OnePurchasePanelBasicTitleView(this);
        onePurchasePanelBasicTitleView.setTv_title(getString(R.string.beginnerGuide));
        BeginnerGuideView beginnerGuideView = new BeginnerGuideView(this);
        beginnerGuideView.setInfo(json);
        onePurchasePanelBasicLayoutView.setInfo(onePurchasePanelBasicTitleView,beginnerGuideView);
    }
}
