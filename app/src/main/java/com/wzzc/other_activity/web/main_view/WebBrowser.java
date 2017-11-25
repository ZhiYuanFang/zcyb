package com.wzzc.other_activity.web.main_view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendAnimation;
import com.wzzc.other_view.extendView.scrollView.UIScrollViewDelegate;

import java.util.HashMap;

/**
 * Created by storecode on 15-11-9.
 *
 */
public class WebBrowser extends WebView implements UIScrollViewDelegate {
    private WebBrowserListener listener;
    public boolean isload = false;
    private Handler gethtmlhandler;

    public WebBrowser(Context context) {
        super(context);
        addEvent();
    }

    public WebBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);
        addEvent();
    }

    public WebBrowser(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addEvent();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
            goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isInEditMode()) {
            return;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void addEvent() {
        if (isInEditMode()) {
            return;
        }
        if (isload) {
            return;
        }
        isload = true;
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings websettings = getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setAppCacheMaxSize(1024 * 1024 * 8);
        websettings.setAppCachePath(getContext().getCacheDir().getAbsolutePath());
        websettings.setAllowFileAccess(true);
        websettings.setAppCacheEnabled(true);
        websettings.setDomStorageEnabled(true);
        websettings.setDatabaseEnabled(true);
        websettings.setGeolocationEnabled(true);
        websettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            websettings.setDatabasePath("/data/data/" + getContext().getPackageName() + "/databases/");
        }
        if (Build.VERSION.SDK_INT >= 16) {
            websettings.setAllowUniversalAccessFromFileURLs(true);
        }
        String UserAgent = websettings.getUserAgentString();
        websettings.setUserAgentString(UserAgent + " " + Default.APPWebClient);

        setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(WebBrowser.this.getContext());
                builder.setTitle(Default.AppName).setMessage(message).setPositiveButton("确定", null);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();
                return true;
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                callback.invoke(origin, true, false);
            }
        });

        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = getTitle();
                if (title != null) {
                    if (!"".equals(title.trim())) {
                        WebBrowser.this.OnPageFinishAction();
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return WebBrowser.this.shouldStartLoadWithRequest(url);
            }
        });

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    private boolean shouldStartLoadWithRequest(String url) {
        if (url.startsWith(Default.APPWebHTML)) {
            String para = url.substring(Default.APPWebHTML.length() + 3).trim();
            if (gethtmlhandler != null) {
                Message mess = new Message();
                mess.obj = para;
                gethtmlhandler.sendMessage(mess);
                gethtmlhandler = null;
            }
            return true;
        }
        if (listener == null) {
            return false;
        }
        return listener.NewUrl(this, url);
    }

    private void OnPageFinishAction() {
        if (listener == null) {
            return;
        }
        listener.LoadEnd(this);
    }

    public void LoadURL(String url) {
        System.out.println("LoadURL : " + url);
        loadUrl(url);
        if (listener == null) {
            return;
        }
        listener.LoadStart(this);
    }

    public String GetTitle() {
        String title = getTitle();
        if (title != null && title.length() > 7) {
            title = title.substring(0, 7);
        }
        return title;
    }

    public void RunJavaScript(String script) {
        loadUrl("javascript:" + script);
    }

    public String GetNowUrl() {
        return getUrl();
    }

    public void setTarget(WebBrowserListener listener) {
        this.listener = listener;
    }

    public int getVerticalScrollOffset() {
        return computeVerticalScrollOffset();
    }

    public int getVerticalScrollRange() {
        return computeVerticalScrollRange();
    }

    public int getVerticalScrollExtent() {
        return computeVerticalScrollExtent();
    }

    public static String GetActionType(String url) {
        if (url.startsWith(Default.APPWebClient)) {
            String para = url.substring(Default.APPWebClient.length() + 3).trim();
            int index = para.indexOf("?");
            if (index <= 0) {
                return para;
            }
            return para.substring(0, index);
        }
        return "";
    }

    public static HashMap<String, String> GetActionPara(String url) {
        HashMap<String, String> actionpara = new HashMap<>();
        if (url.startsWith(Default.APPWebClient)) {
            String para = url.substring(Default.APPWebClient.length() + 3).trim();
            int index = para.indexOf("?");
            if (index <= 0) {
                return actionpara;
            }
            if (index + 1 >= para.length()) {
                return actionpara;
            }
            para = para.substring(index + 1);
            String[] arr = para.split("&");
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].trim().length() <= 0) {
                    continue;
                }
                String text = arr[i].trim();
                int index1 = text.indexOf("=");
                if (index1 > 0) {
                    actionpara.put(text.substring(0, index1), Default.URLDecoded(text.substring(index1 + 1)));
                } else {
                    actionpara.put(text, null);
                }
            }
            return actionpara;
        }
        return actionpara;
    }

    public void RunFunciton(String para) {
        this.RunFunciton("BaseCallBack", para);
    }

    public void RunFunciton(String funname, String para) {
        if (funname == null) {
            funname = "BaseCallBack";
        }
        para = para == null ? "" : "\"" + para + "\"";
        String str = "function client_page_target_function() {\n" +
                "            if (typeof window." + funname + " != \"undefined\") {\n" +
                "                window." + funname + "(" + para + ");\n" +
                "            }\n" +
                "        }\n" +
                "        client_page_target_function();";
        this.RunJavaScript(str);
    }

    public void GetHTMLValue(String para, Handler handler) {
        gethtmlhandler = handler;
        String script = "function gethtmlvalue(para) {\n" +
                "            var arr = para.split(\",\");\n" +
                "            var data = \"null\";\n" +
                "            var control = document.querySelector(arr[0] + \"[\" + arr[1] + \"=\" + arr[2] + \"]\");\n" +
                "            if (control != null && control.hasAttribute(arr[3])) {\n" +
                "                data = control.getAttribute(arr[3]);\n" +
                "            }\n" +
                "            location.href = \"storecodehtml://\" + data;\n" +
                "        }\n" +
                "        gethtmlvalue(\"" + para + "\")";
        RunJavaScript(script);
    }

    public void GetMetaContent(String name, Handler handler) {
        GetHTMLValue("meta,name," + name + ",content", handler);
    }

    @Override
    public void setScrollBarEnabled(boolean enabled) {
        setHorizontalScrollBarEnabled(enabled);
        setVerticalScrollBarEnabled(enabled);
    }

    @Override
    public void TouchUp() {

    }

    @Override
    public boolean isTop() {
        return getVerticalOffset() == 0;
    }

    @Override
    public boolean isBottom(int height) {
        return getVerticalOffset() + height >= getVerticalSize();
    }

    @Override
    public int getVerticalOffset() {
        return computeVerticalScrollOffset();
    }

    @Override
    public int getVerticalSize() {
        return computeVerticalScrollRange();
    }

    public void scrollToY(int Y, boolean animated) {
        if (animated) {
            clearAnimation();
            ExtendAnimation animation = new ExtendAnimation(computeVerticalScrollOffset(), Y);
            animation.setDuration(300);
            animation.setChangeListene(new ExtendAnimation.ExtendAnimationnListener() {
                @Override
                public void onChange(int value) {
                    WebBrowser.this.scrollTo(0, value);
                }

                @Override
                public void onEnd() {

                }
            });
            startAnimation(animation);
        } else {
            WebBrowser.this.scrollTo(0, Y);
        }
    }

}
