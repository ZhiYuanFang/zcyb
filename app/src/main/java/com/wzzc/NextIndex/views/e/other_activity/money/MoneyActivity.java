package com.wzzc.NextIndex.views.e.other_activity.money;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.money.main_view.AccountView;
import com.wzzc.NextIndex.views.e.other_activity.money.main_view.CapitalView;
import com.wzzc.NextIndex.views.e.other_activity.money.main_view.RechargeView;
import com.wzzc.NextIndex.views.e.other_activity.money.main_view.WithdrawalsView;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/4/20.
 * <p>
 * 资金管理
 */

public class MoneyActivity extends BaseActivity implements View.OnClickListener {
    public static final String /*资金明细*/CAPITAL = "capital",/*账户明细*/
            ACCOUNT = "account",/*充值*/
            RECHARGE = "recharge",/*提现*/
            WITHDRAWALS = "withDrawals";
    public static final String SHOWTYPE = "showType";
    //region 组件
    @ViewInject(R.id.btn_money_detail)
    private TextView btn_money_detail;
    @ViewInject(R.id.btn_user_detail)
    private TextView btn_user_detail;
    @ViewInject(R.id.btn_money_add)
    private TextView btn_money_add;
    @ViewInject(R.id.btn_money_get)
    private TextView btn_money_get;
    @ViewInject(R.id.contain_view)
    private RelativeLayout contain_view;
    @ViewInject(R.id.lab_money)
    private TextView lab_money;
    //endregion
    ArrayList<TextView> arrListButton;
    //region view
    private CapitalView capitalView;
    private AccountView accountView;
    private RechargeView rechargeView;
    private WithdrawalsView withdrawalsView;
    //endregion
    //余额
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        lab_money.setText("￥" + User.getSurplus());

        switch (GetIntentData(SHOWTYPE).toString()){
            case CAPITAL:
                btn_money_detail.callOnClick();
                break;
            case ACCOUNT:
                btn_user_detail.callOnClick();
                break;
            case RECHARGE:
                btn_money_add.callOnClick();
                break;
            case WITHDRAWALS:
                btn_money_get.callOnClick();
                break;
            default:
        }
    }

    private void init() {
        capitalView = new CapitalView(this);
        accountView = new AccountView(this);
        rechargeView = new RechargeView(this);
        withdrawalsView = new WithdrawalsView(this);

        arrListButton = new ArrayList<TextView>() {{
            add(btn_money_detail);
            add(btn_user_detail);
            add(btn_money_add);
            add(btn_money_get);
        }};

        for (TextView view : arrListButton) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        changeViewColor((TextView) v);
        switch (v.getId()) {
            case R.id.btn_money_detail: {
                //资金明细
                AddTitle("资金明细");
                capitalView.setInfo();
                showView(capitalView);
                break;
            }
            case R.id.btn_user_detail: {
                //账户明细
                AddTitle("账户明细");
                accountView.setInfo();
                showView(accountView);
                break;
            }
            case R.id.btn_money_add: {
                //充值
                AddTitle("账户充值");
                rechargeView.setInfo();
                showView(rechargeView);
                break;
            }
            case R.id.btn_money_get: {
                //提现
                AddTitle("账户提现");
                withdrawalsView.setInfo(Double.valueOf(User.getSurplus()));
                showView(withdrawalsView);
                break;
            }
            default:
                Default.showToast(getString(R.string.notDevelop), Toast.LENGTH_LONG);
        }
    }

    //region Method

    public void toView(String viewString) {
        switch (viewString) {
            case CAPITAL:
                btn_money_detail.callOnClick();
                break;
            case ACCOUNT:
                btn_user_detail.callOnClick();
                break;
            case RECHARGE:
                btn_money_add.callOnClick();
                break;
            case WITHDRAWALS:
                btn_money_get.callOnClick();
                break;
            default:
        }
    }
    //endregion

    //region Helper
    private void showView(View view) {
        contain_view.removeAllViews();
        contain_view.addView(view);
    }

    private void changeViewColor(TextView focusView) {
        for (TextView view : arrListButton) {
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_White));
            view.setTextColor(ContextCompat.getColor(this, R.color.tv_Black));
        }
        focusView.setTextColor(ContextCompat.getColor(this, R.color.tv_Red));
    }
    //endregion
}
