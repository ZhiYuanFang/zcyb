package com.wzzc.onePurchase.activity.index.main_view.center.activity.PersonalSettings;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.PersonalSettings.main_view.PersonalSettingsView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelBasicTitleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/28.
 *
 * 个人设置
 */

public class PersonalSettingsActivity extends BaseActivity {

    public static final String MABELPHONE = "mabelPhone",RECID = "rec_id",SIGNATURE = "signature",NAME = "name";
    @ViewInject(R.id.basicLayout)
    private OnePurchasePanelBasicLayoutView onePurchasePanelBasicLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras() ;
        JSONObject sender = null;
        try {
            sender = new JSONObject(String.valueOf(bundle.get("info")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OnePurchasePanelBasicTitleView onePurchasePanelBasicTitleView = new OnePurchasePanelBasicTitleView(this);
        onePurchasePanelBasicTitleView.setTv_title(getString(R.string.personalSettings));
        PersonalSettingsView personalSettingsView = new PersonalSettingsView(this);
        personalSettingsView.setInfo(sender);
        onePurchasePanelBasicLayoutView.setInfo(onePurchasePanelBasicTitleView,personalSettingsView);
    }
}
