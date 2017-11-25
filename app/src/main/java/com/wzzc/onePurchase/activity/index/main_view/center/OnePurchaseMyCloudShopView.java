package com.wzzc.onePurchase.activity.index.main_view.center;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.PersonalSettings.PersonalSettingsActivity;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.UserDetail.UserDetailActivity;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.accountRecharge.AccountRechargeActivity;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.beginnerGuide.BeginnerGuideActivity;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.myShowOrder.OnePurchaseMyShowOrderActivity;
import com.wzzc.onePurchase.view.AnotherItemLayoutView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/29.
 *
 * 我的云购
 */

public class OnePurchaseMyCloudShopView extends BaseView  implements View.OnClickListener{

    //region 组件
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_signature)
    private TextView tv_signature;
    @ViewInject(R.id.tv_another)
    private TextView tv_another;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    @ViewInject(R.id.user_icon)
    private ExtendImageView user_icon;
    @ViewInject(R.id.item_myCurrent)
    private AnotherItemLayoutView item_myCurrent;
    @ViewInject(R.id.item_myRecord)
    private AnotherItemLayoutView item_myRecord;
    @ViewInject(R.id.item_MyGetted)
    private AnotherItemLayoutView item_MyGetted;
    @ViewInject(R.id.item_myShowOrder)
    private AnotherItemLayoutView item_myShowOrder;
    @ViewInject(R.id.item_myDetail)
    private AnotherItemLayoutView item_myDetail;
    @ViewInject(R.id.item_myManager)
    private AnotherItemLayoutView item_myManager;
    @ViewInject(R.id.item_helpCenter)
    private AnotherItemLayoutView item_helpCenter;
    @ViewInject(R.id.item_userSetting)
    private AnotherItemLayoutView item_userSetting;
    //endregion

    private String rec_id;
    private String name,mabelPhone,signature,iconPath,current;
    public OnePurchaseMyCloudShopView(Context context) {
        super(context);
        init();
    }
    private void init () {

        item_myCurrent.setOnClickListener(this);
        item_myRecord.setOnClickListener(this);
        item_MyGetted.setOnClickListener(this);
        item_myShowOrder.setOnClickListener(this);
        item_myDetail.setOnClickListener(this);
        item_myManager.setOnClickListener(this);
        item_helpCenter.setOnClickListener(this);
        item_userSetting.setOnClickListener(this);

        tv_another.setOnClickListener(anotherClick());

        getUserInfoFromServer();
    }

    public void setInfo () {
        getUserInfoFromServer();
    }

    private void getUserInfoFromServer () {
        rec_id = "I23902";
        name = "自测qq";
        signature = "亲人就哦i啊u发了块肉价钱哦啊去啊恶气";
        iconPath = "http://www.zcyb365.com/mobile/images/br2.jpg";
        current = "5454154.00";
        mabelPhone = "13258726514";
        initialized();
    }

    private void initialized () {
        tv_name.setText(name);
        tv_signature.setText(signature);
        user_icon.setPath(iconPath);
        item_myCurrent.setOtherInfo(current);
    }

    //region Action
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_myCurrent:{
                // 我的账户余额
                JSONObject json = new JSONObject();
                try {
                    json.put(AccountRechargeActivity.RECID,rec_id);
                    json.put(AccountRechargeActivity.CURRENT,current);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("info", String.valueOf(json));
                GetBaseActivity().AddActivity(AccountRechargeActivity.class,0,intent);
                break;}
            case R.id.item_myRecord:{
                // TODO: 2017/3/29  我的云购记录
                break;}
            case R.id.item_MyGetted:{
                // TODO: 2017/3/29 获得的商品
                break;}
            case R.id.item_myShowOrder:{
                // 我的晒单

                JSONObject json = new JSONObject();
                try {
                    json.put(OnePurchaseMyShowOrderActivity.RECID,rec_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("info", String.valueOf(json));
                GetBaseActivity().AddActivity(OnePurchaseMyShowOrderActivity.class,0,intent);
                break;}
            case R.id.item_myDetail:{
                // 账户明细
                JSONObject json = new JSONObject();
                try {
                    json.put(UserDetailActivity.RECID,rec_id);
                    json.put(UserDetailActivity.CURRENT,current);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("info", String.valueOf(json));
                GetBaseActivity().AddActivity(UserDetailActivity.class,0,intent);
                break;}
            case R.id.item_myManager:{
                // TODO: 2017/3/29  邀请管理

                break;}
            case R.id.item_helpCenter:{
                // 帮助中心
                JSONObject json = new JSONObject();
                try {
                    json.put(BeginnerGuideActivity.RECID,rec_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("info", String.valueOf(json));
                GetBaseActivity().AddActivity(BeginnerGuideActivity.class,0,intent);
                break;}
            case R.id.item_userSetting:{
                // 个人设置
                JSONObject json = new JSONObject();
                try {
                    json.put(PersonalSettingsActivity.RECID,rec_id);
                    json.put(PersonalSettingsActivity.MABELPHONE,mabelPhone);
                    json.put(PersonalSettingsActivity.NAME,name);
                    json.put(PersonalSettingsActivity.SIGNATURE,signature);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("info", String.valueOf(json));
                GetBaseActivity().AddActivity(PersonalSettingsActivity.class,0,intent);

                break;}
            default:
        }
    }

    protected OnClickListener anotherClick () {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.showToast(getContext().getString(R.string.notDevelop), Toast.LENGTH_SHORT);
            }
        };
    }
    //endregion
}
