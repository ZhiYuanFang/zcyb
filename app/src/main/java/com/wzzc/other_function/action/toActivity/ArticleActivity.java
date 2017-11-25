package com.wzzc.other_function.action.toActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.wzzc.base.Default;
import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.FileInfo;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/12/5.
 */
public class ArticleActivity extends ExtendBaseActivity {
    private static final String TAG = "ThirdActivity";
    public static final String ARTICLE_ID = "id";
    @ViewInject(R.id.tv_name)
    private WebView name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("子成头条");
        if (!Default.isConnect(this)) {
            showNoNetWorkView();
        }
    }

    @Override
    protected String getFileKey() {
        return TAG + GetIntentData(ARTICLE_ID);
    }

    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                name.loadDataWithBaseURL(null,json_data.toString(),"text/html","utf-8",null);
            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        JSONObject para = new JSONObject();
        try {
            para.put("article_id", GetIntentData("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(ArticleActivity.this , "article",!hsf, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                FileInfo.SetUserString(getFileKey(),sender.toString(),ArticleActivity.this);
                try {
                    initialized(sender,serverCallBack());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
