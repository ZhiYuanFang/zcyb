package com.wzzc.base;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
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

public abstract class ExtendBaseView extends BaseView {
    protected boolean hsf;//file是否存在
    protected boolean dataFromServer;//访问服务器后设置数据
    protected boolean firstInitFromFile;
    public static int delayTime = 0;

    public ExtendBaseView(Context context) {
        super(context);
        init();
    }

    public ExtendBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    abstract protected void init();

    protected void setOthersInfo(JSONObject jsonInComing) throws JSONException {
    }

    ;

    abstract protected String getFileKey();

    abstract protected ServerCallBack serverCallBack();
    protected ExtendBaseActivity.ServerCallBackType serverCallBackType() {
        return null;
    }
    /**
     * @param type 当null 时 ： 为默认方式
     */
    abstract protected void setInfoFromService(String type);

    protected void firstInitDataFromFileInfo(final JSONObject sender) {
        try {
            initialized(sender, serverCallBack());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //region Method
    public void setInfo(JSONObject json) {
        try {
            setOthersInfo(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                judgeFileHasDataAndInitializedData();
                judgeNetConnectedAndSetInfoFromService(null);
            }
        },delayTime);
    }

    public void judgeFileHasDataAndInitializedData() {
        if (FileInfo.IsAtCacheMap(getFileKey())) {
            firstInitFromFile = true;
            hsf = true;
            JSONObject sender = getSenderFromFile(true);
            firstInitDataFromFileInfo(sender);
        }
        if (FileInfo.IsAtUserString(getFileKey(), getContext())) {
            firstInitFromFile = true;
            hsf = true;
            JSONObject sender = getSenderFromFile(false);
            firstInitDataFromFileInfo(sender);
        }
    }

    public void judgeNetConnectedAndSetInfoFromService(String type) {
        if (Default.isConnect(getContext())) {
            setInfoFromService(type);
        } else {
            dataFromServer = true;
            if (!hsf)
                showNoNetWorkView();
        }
    }

    public void showNoNetWorkView() {

        try {
            View view = findViewById(R.id.basicLayout);
            if (view instanceof LinearLayout) {
                ((LinearLayout) view).removeAllViews();
                ((LinearLayout) view).addView(new NoNetView(getContext()));
            }
            if (view instanceof RelativeLayout) {
                ((RelativeLayout) view).removeAllViews();
                ((RelativeLayout) view).addView(new NoNetView(getContext()));
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
                json = new JSONObject(FileInfo.GetUserString(getFileKey(), getContext()));
        } catch (JSONException e) {
            System.out.println("FileInfo not has " + getFileKey());
        }
        return json;
    }

    public void initialized(final JSONObject sender, final ServerCallBack scb) throws JSONException {
        hsf = FileInfo.IsAtUserString(getFileKey(), getContext());
        JSONObject json_status = sender.getJSONObject("status");
        int succeed = json_status.getInt("succeed");
        if (succeed == 1) {
            if (scb != null) {
                try {
                    scb.callBack(sender.get("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            error(json_status.getString("error_desc"));
//            FileInfo.SetUserString("userid","",getContext());
        }
    }
    public void initialized (final JSONObject sender , final String type , final ExtendBaseActivity.ServerCallBackType scbt) throws JSONException {
        JSONObject json_status = sender.getJSONObject("status");
        int succeed = json_status.getInt("succeed");
        if (succeed == 1) {
            if (scbt != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            scbt.callBack(type , sender.get("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },delayTime);
            }
        } else {
            MessageBox.Show(json_status.getString("error_desc"));
        }
    }
    //endregion

    public void error(String error_desc) {
        MessageBox.Show(error_desc);
    }

    public interface ServerCallBack {
        void callBack(Object json_data);
    }
}
