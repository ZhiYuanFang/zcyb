package com.wzzc.taobao;

import android.app.ProgressDialog;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.android.trade.page.AlibcShopPage;
import com.wzzc.base.Default;
import com.wzzc.other_function.AsynServer.Beans.ServerInfo;
import com.wzzc.other_function.MessageBox;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

/**
 * Created by by Administrator on 2017/6/5.
 */

public class TaoBao {

    private static final String App_key = "23856805";
    private static final String App_Secret = "d9ee78579764da5ef7235421cb2ec0c8";
    private static final String Taobao_url = "https://eco.taobao.com/router/rest";
    public static boolean isLogin = false;
    //http://gw.api.taobao.com/router/rest
    private static final String Session = "";//用户登录授权成功后，TOP颁发给应用的授权信息。当此API的标签上注明：“需要授权”，则此参数必传；“不需要授权”，则此参数不需要传；“可选授权”，则此参数为可选。
    private static final String MD5_Signature = "4286853be274b11ec6bd9a350a6af091";
    private static ProgressDialog pd;
    public static void showItemDetailPage(String itemId, AlibcTradeCallback alibcTradeCallback) {
        if (!Default.isConnect(Default.getActivity())) {
            Default.showToast("noNet!");
            return;
        }
        if (itemId == null || itemId.length() <= 0 || itemId.equals("null")) {
            MessageBox.Show("产品失效");
        } else {
            System.out.println("TaoBao num_id : " + itemId);
            //商品详情page
            AlibcBasePage detailPage = new AlibcDetailPage(itemId);

            //设置页面打开方式
            AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
            AlibcTrade.show(Default.getActivity(), detailPage, showParams, null, null, alibcTradeCallback);
        }
    }

    public static void showPage(String pageUrl, AlibcTradeCallback alibcTradeCallback) {
        AlibcPage alibcPage = new AlibcPage(pageUrl);
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        AlibcTrade.show(Default.getActivity(), alibcPage, showParams, null, null, alibcTradeCallback);
    }

    /**
     * @param openType 0:商品详情页 1:url优惠券 2:商铺页面 3:购物车
     */
    public static void showItemFromWebView(int openType, final WebView webView, final WebViewClient webViewClient, final WebChromeClient webChromeClient, String item,
                                           final AlibcTradeCallback alibcTradeCallback) {
        System.out.println("Taobao item : " + item);
        final AlibcBasePage detailPage;
        switch (openType) {
            case 0:
                detailPage = new AlibcDetailPage(item);
                break;
            case 1:
                detailPage = new AlibcPage(item);
                break;
            case 2:
                detailPage = new AlibcShopPage(item);
                break;
            case 3:
                detailPage = new AlibcMyCartsPage();
                break;
            default:
                detailPage = new AlibcPage(item);
        }
        final AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);
        if (!isLogin) {
            System.out.println("Taobao not login");
            if (pd == null) {
                pd = new ProgressDialog(Default.getActivity());
                pd.setMessage("正在调用淘宝授权登陆");
            }
            try {
                if (!pd.isShowing()){
                    pd.show();
                }
            } catch (WindowManager.BadTokenException e){
                e.printStackTrace();
            }
            login(new AlibcLoginCallback() {
                @Override
                public void onSuccess() {
                    pd.dismiss();
                    AlibcTrade.show(Default.getActivity(), webView, webViewClient, webChromeClient, detailPage, showParams, null, new HashMap(), alibcTradeCallback);
                    isLogin = true;
                }

                @Override
                public void onFailure(int i, String s) {
                    pd.dismiss();
                    isLogin = false;
                    Default.showToast(s);
                    Default.getActivity().onBackPressed();
                }
            });
        } else {
            System.out.println("Taobao has login");
            AlibcTrade.show(Default.getActivity(), webView, webViewClient, webChromeClient, detailPage, showParams, null, new HashMap(), alibcTradeCallback);
        }

    }

    /**
     * 我的订单页面
     *
     * @param status   默认跳转页面；填写：0：全部；1：待付款；2：待发货；3：待收货；4：待评价
     * @param allOrder false 进行订单分域（只展示通过当前app下单的订单），true 显示所有订单
     */
    public static void showOrderFromWebView(int status, final WebView webView, final WebViewClient webViewClient, final WebChromeClient webChromeClient, boolean allOrder,
                                            final AlibcTradeCallback alibcTradeCallback) {
        final AlibcBasePage detailPage = new AlibcMyOrdersPage(status, allOrder);
        final AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);

        if (!isLogin) {
            System.out.println("Taobao not login");
            if (pd == null) {
                pd = new ProgressDialog(Default.getActivity());
                pd.setMessage("正在调用淘宝授权登陆");
            }
            try {
                if (!pd.isShowing()){
                    pd.show();
                }
            } catch (WindowManager.BadTokenException e){
                e.printStackTrace();
            }
            login(new AlibcLoginCallback() {
                @Override
                public void onSuccess() {
                    pd.dismiss();
                    AlibcTrade.show(Default.getActivity(), webView, webViewClient, webChromeClient, detailPage, showParams, null, new HashMap(), alibcTradeCallback);
                    isLogin = true;
                }

                @Override
                public void onFailure(int i, String s) {
                    pd.dismiss();
                    isLogin = false;
                    Default.showToast(s);
                    Default.getActivity().onBackPressed();
                }
            });
        } else {
            System.out.println("Taobao has login");
            AlibcTrade.show(Default.getActivity(), webView, webViewClient, webChromeClient, detailPage, showParams, null, new HashMap(), alibcTradeCallback);
        }

    }

    public static void login(AlibcLoginCallback alibcLoginCallback) {
        System.out.println("Taobao login");
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(Default.getActivity(), alibcLoginCallback);
    }

    public static void logout() {
        System.out.println("Taobao logout");
        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.logout(Default.getActivity(), new LogoutCallback() {
            @Override
            public void onSuccess() {
                System.out.println("Taobao out ok !");
                Default.showToast("成功退出淘宝");
                isLogin = false;
            }

            @Override
            public void onFailure(int code, String msg) {
                System.out.println("Taobao out error --> " + msg);
                Default.showToast(msg);
            }
        });
    }

    public static void logout(LogoutCallback logoutCallback) {
        System.out.println("Taobao logout");
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.logout(Default.getActivity(), logoutCallback);
    }

    public static void destroy() {
        AlibcTradeSDK.destory();
    }

    public static boolean hasInstallation() {
        return Default.checkPackage(Default.getActivity(), "com.taobao.taobao");
    }
    //region ```
    public static void searchKeyList(final String key, final TaoBaoCallBack taoBaoCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] backValue = ServerInfo.Post("https://suggest.taobao.com/sug?code=utf-8&q=" + key + "", null, null);
                    String backString = new String(backValue, "UTF-8");
                    System.out.println("TaoBao : " + backString);
                    JSONObject json = (JSONObject) new JSONTokener(backString).nextValue();
                    if (taoBaoCallBack != null) {
                        taoBaoCallBack.call(json);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public interface TaoBaoCallBack {
        void call(JSONObject json);
    }
  //endregion
}
