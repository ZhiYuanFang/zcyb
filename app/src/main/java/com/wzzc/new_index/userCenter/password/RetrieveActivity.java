package com.wzzc.new_index.userCenter.password;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zcyb365 on 2016/12/2.
 */
public class RetrieveActivity extends BaseActivity {
    @ViewInject(R.id.edit_phone)
    private EditText edit_phone;
    @ViewInject(R.id.edit_message)
    private EditText edit_message;
    @ViewInject(R.id.btn_obtain)
    private Button btn_obtain;
    @ViewInject(R.id.edit_password)
    private EditText edit_password;
    @ViewInject(R.id.btn_register)
    private Button register;
    @ViewInject(R.id.lab_nodisplay)
    private ImageView lab_nodisplay;
    private int time = 60;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("找回密码");
        initBackTouch();
        btn_obtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
                Matcher m = p.matcher(edit_phone.getText());
                if (m.matches()) {
                    JSONObject sender = Default.GetSession();
                    JSONObject filter = new JSONObject();
                    JSONObject para = new JSONObject();
                    try {
                        filter.put("uid", sender.getString("uid"));
                        filter.put("sid", sender.getString("sid"));
                        para.put("session", filter);
                        para.put("mobile", edit_phone.getText());
                        para.put("sms_type", 2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(RetrieveActivity.this , "sms_send", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            JSONObject json_status = JsonClass.jj(sender,"status");
                            if (JsonClass.ij(json_status,"succeed") == 1) {
                                RunTimer();
                            } else {
                                MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                            }
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RetrieveActivity.this);
                    builder.setMessage("手机号码格式不正确！");
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject para = new JSONObject();
                if (edit_password.getText().length() >= 6 && edit_password.getText().length() <= 20) {
                    try {
                        para.put("mobile", edit_phone.getText().toString());
                        para.put("new_password", edit_password.getText().toString());
                        para.put("sms_code", edit_message.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(RetrieveActivity.this , "user/get_password", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject s) {
                            JSONObject json_status = JsonClass.jj(s,"status");
                            if (JsonClass.ij(json_status,"succeed") == 0) {
                                MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                            } else {
                                MessageBox.createNewDialog();
                                MessageBox.Show(RetrieveActivity.this, Default.AppName, "密码修改成功", new String[]{"前往登陆"}, new MessageBox.MessBtnBack() {
                                    @Override
                                    public void Back(int index) {
                                        Intent intent = new Intent(RetrieveActivity.this,LoginActivity.class);
                                        Default.toClass(RetrieveActivity.this,intent);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RetrieveActivity.this);
                    builder.setMessage("密码长度不正确！");
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

    public void RunTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                time--;
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);

            }
        };
        timer.schedule(task, 100, 1000);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if (time > 0) {
                        btn_obtain.setEnabled(false);
                        btn_obtain.setText(time + "秒后重新发送");
                        btn_obtain.setTextSize(14);
                    } else {
                        timer.cancel();
                        btn_obtain.setText("获取验证码");
                        btn_obtain.setEnabled(true);
                        btn_obtain.setTextSize(14);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @OnClick({R.id.lab_nodisplay})
    public void btn_bottom_click(ImageView view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == 0) {
            lab_nodisplay.setImageResource(R.drawable.input_eye_on);
            edit_password.setInputType(InputType.TYPE_CLASS_TEXT);
            lab_nodisplay.setTag(1);
        } else if (tag == 1) {
            lab_nodisplay.setImageResource(R.drawable.input_eye_off);
            edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            lab_nodisplay.setTag(0);
        }
    }


}
