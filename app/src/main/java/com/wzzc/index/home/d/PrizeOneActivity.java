package com.wzzc.index.home.d;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.d.main_view.RecordActivity;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.other_function.JsonClass;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/12/2.
 */
public class PrizeOneActivity extends BaseActivity {

    @ViewInject(R.id.main_webview)
    public WebBrowser main_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONObject para = new JSONObject();
        final JSONObject session = Default.GetSession();
        try {
            if (!"0".equals(session.getString("uid"))&&!"".equals(session.getString("uid"))) {
                try {
                    JSONObject filter = new JSONObject();
                    filter.put("uid",session.getString("uid"));
                    filter.put("sid", session.getString("sid"));
                    para.put("session", filter);
                    para.put("activityid", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(PrizeOneActivity.this , "dazhuanpan/index", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject s) {
                        JSONObject sender = JsonClass.jj(s,"data");
                        if (sender!=null){
                            try {
                                main_webview.LoadURL(sender.getString("url"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PrizeOneActivity.this);
                            builder.setMessage("您尚未登陆！");
                            builder.setTitle(Default.AppName);
                            builder.setPositiveButton("去登陆", new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                    AddActivity(LoginActivity.class);
                                    finish();
                                }
                            });
                            builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BackActivity();
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    }
                });
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PrizeOneActivity.this);
                builder.setMessage("您尚未登陆！");
                builder.setTitle(Default.AppName);
                builder.setPositiveButton("去登陆", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        AddActivity(LoginActivity.class);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BackActivity();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_record})
    public void btn_goto_onclick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == 0) {
            BackActivity();
        } else if (tag == 1) {
            Intent intent=new Intent();
            intent.putExtra("id",1);
            AddActivity(RecordActivity.class,0,intent);
        }
    }
}
