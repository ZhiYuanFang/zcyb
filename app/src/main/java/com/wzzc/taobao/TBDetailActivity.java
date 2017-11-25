package com.wzzc.taobao;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.RelativeLayout;

import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.tou.MyWebView;
import com.wzzc.other_activity.web.tou.MyWebViewClient;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.FloatButton;
import com.wzzc.other_view.fragment.WarnFragment;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/11.
 * <p>
 * 产品详情
 */
@ContentView(R.layout.activity_tbdetail)
public class TBDetailActivity extends BaseActivity {
    public static final String Item = "item";
    public static final String Type = "type";
    public static final String GoodsName = "goods_name";
    @ViewInject(R.id.layout_set)
    RelativeLayout layout_set;
    @ViewInject(R.id.btn_back)
    RelativeLayout layout_back;
    @ViewInject(R.id.myWebView)
    MyWebView myWebView;
    @ViewInject(R.id.float_button)
    FloatButton float_button;
    WindowManager windowManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }

        float_button.setWindowManager(windowManager);

        float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFragment) {
                    onBackPressed();
                } else {
                    showWarnFragment(GetIntentData(GoodsName).toString());
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (GetIntentData(GoodsName) != null) {
                    if (TaoBao.isLogin) {
                       showWarnFragment(GetIntentData(GoodsName).toString());
                    }
                }
            }
        }, 3000);
        layout_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TaoBao.isLogin) {
                    MessageBox.Show(TBDetailActivity.this, "淘宝", "退出当前淘宝账号", new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                        @Override
                        public void Back(int index) {
                            switch (index) {
                                case 0:
                                    break;
                                case 1:
                                    TaoBao.logout(new LogoutCallback() {
                                        @Override
                                        public void onSuccess() {
                                            Default.showToast("成功退出");
                                            TaoBao.isLogin = false;
                                            final ProgressDialog pd = new ProgressDialog(TBDetailActivity.this);
                                            pd.setMessage("正在与淘宝协商授权");
                                            pd.show();
                                            TaoBao.login(new AlibcLoginCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    Default.showToast("登陆成功");
                                                    pd.dismiss();
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                    pd.dismiss();
                                                    Default.showToast(s);
                                                    BackActivity();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Default.showToast(s);
                                        }
                                    });
                                    break;
                                default:
                            }
                        }
                    });
                } else {
                    final ProgressDialog pd = new ProgressDialog(TBDetailActivity.this);
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

        if (GetIntentData(Item) != null && GetIntentData(Type) != null) {
            TaoBao.showItemFromWebView((Integer) GetIntentData(Type), myWebView, new MyWebViewClient(myWebView), new WebChromeClient(), GetIntentData(Item).toString(), new AlibcTradeCallback() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
        try {
            windowManager.removeView(float_button);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        float_button.setWindowManager(windowManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            windowManager.removeView(float_button);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            windowManager.removeView(float_button);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (hasFragment) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    float_button.setVisibility(View.VISIBLE);
                }
            },400);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void showWarnFragment(String proName) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(WarnFragment.ProInfo, "     当前页面仅购买 \"" + proName + "\" 才可获取返利!");
            WarnFragment warnFragment = new WarnFragment();
            warnFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out);
            fragmentTransaction.add(R.id.contain_fragment, warnFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            float_button.setVisibility(View.GONE);
            hasFragment = true;
        } catch (IllegalStateException e){
            e.printStackTrace();
        }

    }
}
