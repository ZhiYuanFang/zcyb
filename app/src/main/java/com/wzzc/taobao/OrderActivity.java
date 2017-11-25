package com.wzzc.taobao;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.tou.MyWebView;
import com.wzzc.other_activity.web.tou.MyWebViewClient;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.fragment.WarnFragment;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/13.
 *
 */

public class OrderActivity extends BaseActivity {
    public static final String Status = "status";//默认跳转页面；填写：0：全部；1：待付款；2：待发货；3：待收货；4：待评价
    //region ```
    @ViewInject(R.id.layout_set)
    RelativeLayout layout_set;
    @ViewInject(R.id.btn_back)
    RelativeLayout layout_back;
    @ViewInject(R.id.orderWebView)
    MyWebView orderWebView;
    @ViewInject(R.id.tv_type)
    TextView tv_type;
    //endregion
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && orderWebView.canGoBack()) {
            orderWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TaoBao.isLogin) {
                    showWarnFragment("当前页面仅显示在子成商城购买的订单");
                }
            }
        }, 1000);
        layout_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TaoBao.isLogin) {
                    MessageBox.Show(OrderActivity.this, "淘宝", "退出当前淘宝账号", new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                        @Override
                        public void Back(int index) {
                            switch (index) {
                                case 0:
                                    break;
                                case 1:
                                    TaoBao.logout();
                                    break;
                                default:
                            }
                        }
                    });
                } else {
                    final ProgressDialog pd = new ProgressDialog(OrderActivity.this);
                    pd.setMessage("正与淘宝商议授权");
                    pd.show();
                    TaoBao.login(new AlibcLoginCallback() {
                        @Override
                        public void onSuccess() {
                            pd.dismiss();
                            Default.showToast("登陆成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            pd.dismiss();
                            Default.showToast(s);
                        }
                    });
                }

            }
        });
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int status = (int) GetIntentData(Status);
        switch (status) {
            case 0:
                tv_type.setText("全部");
                break;
            case 1:
                tv_type.setText("待付款");
                break;
            case 2:
                tv_type.setText("待发货");
                break;
            case 3:
                tv_type.setText("待收货");
                break;
            case 4:
                tv_type.setText("待评价");
                break;
            default:
        }
        TaoBao.showOrderFromWebView(status, orderWebView, new MyWebViewClient(orderWebView), new WebChromeClient(), false, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(TradeResult tradeResult) {

            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println("error code : " + i + " error msg : " + s);
                Default.showToast(s);
            }
        });

    }

    private void showWarnFragment(String pro) {
        Bundle bundle = new Bundle();
        bundle.putString(WarnFragment.ProInfo, pro);
        WarnFragment warnFragment = new WarnFragment();
        warnFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_bottom_out, R.anim.push_bottom_in, R.anim.push_bottom_out);
        fragmentTransaction.replace(R.id.contain_fragment, warnFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        hasFragment = true;
    }
}
