package com.wzzc.new_index.userCenter.password;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/10/28.
 */
public class PasswordEditActivity extends BaseActivity {
    @ViewInject(R.id.old_password)
    private EditText old_password;
    @ViewInject(R.id.new_password)
    private EditText new_password;
    @ViewInject(R.id.new_passwordagin)
    private EditText new_passwordagin;
    @ViewInject(R.id.btn_passwordedit)
    private Button btn_passwordedit;
    private String oldpassword, newpassword, newpasswordagin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("修改密码");
        btn_passwordedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpassword = old_password.getText().toString();
                newpassword = new_password.getText().toString();
                newpasswordagin = new_passwordagin.getText().toString();
                if (oldpassword.trim().equals("") || newpassword.trim().equals("") || newpasswordagin.equals("")) {
                    Default.showToast("请输入密码" , Toast.LENGTH_LONG);
                    return;
                }
                if (newpassword.trim().equals(newpasswordagin.trim())) {
                    JSONObject para = new JSONObject();
                    JSONObject sender = Default.GetSession();
                    try {
                        JSONObject filter = new JSONObject();
                        filter.put("uid", sender.getString("uid"));
                        filter.put("sid", sender.getString("sid"));
                        para.put("session", filter);
                        para.put("old_password", oldpassword.trim());
                        para.put("new_password", newpassword.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(PasswordEditActivity.this , "user/password/edit", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            try {
                                JSONObject status=sender.getJSONObject("status");
                                if (status.getInt("succeed") == 0) {
//                                    AddActivity(LoginActivity.class);
                                    Default.showToast("密码错误" , Toast.LENGTH_LONG);
                                } else {
                                    if (sender != null) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordEditActivity.this);
                                        builder.setMessage("密码修改成功!请重新登陆");
                                        builder.setTitle(Default.AppName);
                                        builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                arg0.dismiss();
                                                AddActivity(LoginActivity.class);
                                            }
                                        });
                                        builder.create().show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordEditActivity.this);
                    builder.setMessage("两次输入密码不一致！请重新输入");
                    builder.setTitle(Default.AppName);
                    builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }
}
