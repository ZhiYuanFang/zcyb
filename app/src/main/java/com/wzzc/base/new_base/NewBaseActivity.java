package com.wzzc.base.new_base;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.AsynServer.Beans.ServerInfo;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.NoNetView;
import com.wzzc.other_view.progressDialog.CustomProgressDialog;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/24.
 *
 */

public abstract class NewBaseActivity extends BaseActivity implements View.OnClickListener {
    boolean hasCache;//是否有缓存
    protected boolean newServer = true;//首次访问一个服务器接口
    NoNetView noNetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noNetView = new NoNetView(this);
        View view = findViewById("basicLayout");
        if (view != null) {
            if (view instanceof RelativeLayout) {
                ((RelativeLayout) view).addView(noNetView);
                noNetView.setVisibility(View.GONE);
            }
        }
        initBackTouch();
        AddBack();
        final CustomProgressDialog dialog = CustomProgressDialog.createDialog(this,R.style.CustomProgressDialog);
        dialog.showProgress("加载布局");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
                dialog.dismiss();
            }
        }, Default.delayTime);

    }

    abstract protected void init();

    abstract protected String getFileKey();

    protected void initDataFromCache(String s) {
        final CustomProgressDialog dialog = CustomProgressDialog.createDialog(this,R.style.CustomProgressDialog);
        dialog.show();
        ServerInfo.stopConnection();
        newServer = true;
        System.out.println("NBA aa key : "+getFileKey() + " FileInfo.IsAtCache ? : " + FileInfo.IsAtCacheMap(getFileKey()));
        if (FileInfo.IsAtCacheMap(getFileKey())) {
            try {
                JSONObject sender = new JSONObject(FileInfo.GetStringFromCacheMap(getFileKey()));
                JSONObject json_status = JsonClass.jj(sender, "status");
                int succeed = JsonClass.ij(json_status, "succeed");
                if (succeed == 1) {
                    Object data = JsonClass.oj(sender, "data");
                    cacheCallBack().callBack(data, s);
                }
                hasCache = true;
            } catch (JSONException e) {
                e.printStackTrace();
                hasCache = false;
            }
        } else if (FileInfo.IsAtUserString(getFileKey(), this)) {
            try {
                JSONObject sender = new JSONObject(FileInfo.GetUserString(getFileKey(), this));
                JSONObject json_status = JsonClass.jj(sender, "status");
                int succeed = JsonClass.ij(json_status, "succeed");
                if (succeed == 1) {
                    Object data = JsonClass.oj(sender, "data");
                    cacheCallBack().callBack(data, s);
                }
                hasCache = true;
            } catch (JSONException e) {
                e.printStackTrace();
                hasCache = false;
            }
        } else {
            hasCache = false;
        }
        dialog.dismiss();
    }

    protected void initDataFromServer(ListViewScrollDelegate listViewScrollDelegate , String url, boolean showDialog, JSONObject para, ListView lv, final String s) {
        if (Default.isConnect(this)) {
            AsynServer.BackObject(this,listViewScrollDelegate, url, showDialog, lv, para, new AsynServer.BackObject() {
                @Override
                public void BackError(String error) {
                    super.BackError(error);
                    if (!FileInfo.IsAtUserString(getFileKey(), NewBaseActivity.this)) {
                        showNoNetView();
                    }
                }

                @Override
                public void Back(JSONObject sender) {
                    System.out.println("NBA newServer : " + newServer);
                    if (newServer) {
                        System.out.println("NBA newServerDataFromServer("+sender.toString()+"）");
                        newServerDataFromServer(sender, s);
                        newServer = false;
                    }
                    JSONObject json_status = JsonClass.jj(sender, "status");
                    int succeed = JsonClass.ij(json_status, "succeed");
                    if (succeed == 1) {
                        Object data = JsonClass.oj(sender, "data");
                        serverCallBack().callBack(data, s);
                    } else {
                        System.out.println("NBA C");
                        error(JsonClass.sj(json_status,"error_code"),JsonClass.sj(json_status, "error_desc"));
                    }
                }
            });
        } else {
            if (!FileInfo.IsAtUserString(getFileKey(), NewBaseActivity.this)) {
                showNoNetView();
            }
        }
    }

    protected void newServerDataFromServer(JSONObject sender, String s) {
//        System.out.println("NBA key : "+getFileKey() + " FileInfo.IsAtCache ? : " + FileInfo.IsAtCacheMap(getFileKey()));
//        if (!FileInfo.IsAtCacheMap(getFileKey())) {
//            FileInfo.SetUserString(getFileKey(), sender.toString(), this);
//        }
//        FileInfo.SetStringToCacheMap(getFileKey(),sender.toString());
        if (getFileKey() != null) {
            FileInfo.SetUserString(getFileKey(), sender.toString(), this);
        }
    }

    protected void error(String error_code ,String error_desc) {
        System.out.println("NBA error : " + error_desc);
        if (error_desc.length() > 0) {
            if (error_code.equals("100")) {
                MessageBox.Show(this,Default.AppName, error_desc, new String[]{"取消","前往登陆"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index){
                            case 1:
                                AddActivity(LoginActivity.class);
                                break;
                            default:
                        }
                    }
                });
            } else
            MessageBox.Show(error_desc);
        }
    }

    abstract protected CacheCallBack cacheCallBack();

    abstract protected ServerCallBack serverCallBack();

    protected interface CacheCallBack {
        void callBack(Object obj, String s);
    }

    protected interface ServerCallBack {
        void callBack(Object obj, String s);
    }

    public void showNoNetView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                noNetView.setVisibility(View.VISIBLE);
            }
        });
    }

    abstract protected void publish();

    @Override
    public void refresh() {
        noNetView.setVisibility(View.GONE);
        publish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AsynServer.wantShowDialog = false;
    }
}
