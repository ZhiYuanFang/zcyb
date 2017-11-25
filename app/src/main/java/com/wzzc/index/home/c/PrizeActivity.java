package com.wzzc.index.home.c;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.d.main_view.RecordActivity;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/12/2.
 */
public class PrizeActivity extends BaseActivity {

    @ViewInject(R.id.main_webview)
    public WebBrowser main_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONObject para = new JSONObject();
        try {
            para.put("activityid", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(PrizeActivity.this, "dazhuanpan/index", para, new AsynServer.BackObject() {
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

    @OnClick({R.id.btn_back, R.id.btn_record})
    public void btn_goto_onclick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == 0) {
            BackActivity();
        } else if (tag == 1) {
            Intent intent = new Intent();
            intent.putExtra("id", 2);
            AddActivity(RecordActivity.class, 0, intent);
        }
    }
}
