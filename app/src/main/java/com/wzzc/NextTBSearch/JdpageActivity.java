package com.wzzc.NextTBSearch;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

public class JdpageActivity extends BaseActivity{
    public static final String num_iid_key = "num_iid_key";

    @ViewInject(R.id.main_webview)
    public WebBrowser main_webview;
    @ViewInject(R.id.imageView48)
    public ImageView mBackIv;
    @ViewInject(R.id.imageView49)
    public ImageView mReload;
    @ViewInject(R.id.close_page)
    public ImageView mClosePage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String num_iid = (String)GetIntentData(num_iid_key);

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_webview.goBack();
            }
        });

        mReload.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                main_webview.reload();
            }
        });

        mClosePage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        if(null == num_iid){
            finish();
        }

        JSONObject para = new JSONObject();
        try {
            para.put("num_iid", num_iid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(JdpageActivity.this, "JinDong/buy", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                JSONObject json_status = JsonClass.jj(sender, "status");
                if (JsonClass.ij(json_status, "succeed") == 1) {
                    JSONObject json_data = JsonClass.jj(sender, "data");
                    main_webview.LoadURL(JsonClass.sj(json_data, "url"));
                } else {
                    MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && main_webview.canGoBack()) {
            main_webview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}