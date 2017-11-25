package com.wzzc.base;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.NoNetView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/5/8.
 */

public abstract class ExtendBaseActivity extends BaseActivity {
    protected boolean hsf;
    protected String url;
    protected boolean firstInitFromFile;
    public static int delayTime = 0;
    protected boolean dataFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        init();
    }

    protected void init() {
        judgeFileHasDataAndInitializedData();
        judgeNetConnectedAndSetInfoFromService(null);
    }

    ;

    public void judgeFileHasDataAndInitializedData() {
        if (FileInfo.IsAtCacheMap(getFileKey())) {
            firstInitFromFile = true;
            hsf = true;
            JSONObject sender = getSenderFromFile(true);
            try {
                initialized(sender, serverCallBack());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (FileInfo.IsAtUserString(getFileKey(), this)) {
            firstInitFromFile = true;
            hsf = true;
            JSONObject sender = getSenderFromFile(false);
            try {
                initialized(sender, serverCallBack());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            hsf = false;
        }
    }

    public void judgeNetConnectedAndSetInfoFromService(String type) {
        if (Default.isConnect(this)) {
            setInfoFromService(type);
        } else {
            dataFromServer = true;
            if (!hsf)
                showNoNetWorkView();
        }
    }

    abstract protected String getFileKey();

    abstract protected ExtendBaseView.ServerCallBack serverCallBack();

    protected ServerCallBackType serverCallBackType() {
        return null;
    }

    /**
     * @param type 当null 时 ： 为默认方式
     */
    abstract protected void setInfoFromService(String type);

    //region Method

    public void showNoNetWorkView() {
        try {
            View view = findViewById(R.id.basicLayout);
            if (view instanceof LinearLayout) {
                ((LinearLayout) view).removeAllViews();
                ((LinearLayout) view).addView(new NoNetView(this));
            }
            if (view instanceof RelativeLayout) {
                ((RelativeLayout) view).removeAllViews();
                ((RelativeLayout) view).addView(new NoNetView(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Default.showToast("NoNet !", Toast.LENGTH_LONG);
        }
    }

    public JSONObject getSenderFromFile(boolean cacheMap) {
        JSONObject json = new JSONObject();
        try {
            if (cacheMap) {
                json = new JSONObject(FileInfo.GetStringFromCacheMap(getFileKey()));
            } else
                json = new JSONObject(FileInfo.GetUserString(getFileKey(), this));
        } catch (JSONException e) {
            System.out.println("FileInfo not has " + getFileKey());
        }
        return json;
    }

    ;

    public void initialized(final JSONObject sender, final ExtendBaseView.ServerCallBack scb) throws JSONException {
        JSONObject json_status = sender.getJSONObject("status");
        int succeed = json_status.getInt("succeed");
        if (succeed == 1) {
            if (scb != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            scb.callBack(sender.get("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, delayTime);
            }
        } else {
            MessageBox.Show(json_status.getString("error_desc"));
        }
    }


    public void initialized(final JSONObject sender, final String type, final ServerCallBackType scbt) throws JSONException {
        JSONObject json_status = sender.getJSONObject("status");
        int succeed = json_status.getInt("succeed");
        if (succeed == 1) {
            if (scbt != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            scbt.callBack(type, sender.get("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, delayTime);
            }
        } else {
            MessageBox.Show(json_status.getString("error_desc"));
        }
    }
    //endregion

    public interface ServerCallBack {
        void callBack(Object json_data);
    }

    public interface ServerCallBackType {
        void callBack(String type, Object json_data);
    }
}
