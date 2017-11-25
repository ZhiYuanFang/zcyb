package com.wzzc.new_index.userCenter.regest.ordinary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.zcybStores.ConvertActivity;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.new_index.userCenter.regest.ordinary.main_view.AgreementActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zcyb365 on 2016/10/8.
 * 注册
 */
public class RegisterActivity extends BaseActivity {

    public static String DPID = "dpid";
    public static String EWCode = "ewcode";
    //region ```
    @ViewInject(R.id.edit_phone)
    private EditText edit_phone;
    @ViewInject(R.id.edit_message)
    private EditText edit_message;
    @ViewInject(R.id.btn_obtain)
    private Button btn_obtain;
    @ViewInject(R.id.edit_password)
    private EditText edit_password;
    @ViewInject(R.id.checkBox)
    private CheckBox checkBox;
    @ViewInject(R.id.btn_register)
    private Button register;
    @ViewInject(R.id.btn_login)
    private Button btn_login;
    @ViewInject(R.id.lab_nodisplay)
    private ImageView lab_nodisplay;
    @ViewInject(R.id.lab_agreement)
    private TextView lab_agreement;
    @ViewInject(R.id.lab_dpid)
    private LinearLayout lab_dpid;
    @ViewInject(R.id.tv_dpid_line)
    private TextView tv_dpid_line;
    @ViewInject(R.id.edit_dpid)
    private EditText edit_dpid;
    @ViewInject(R.id.edit_code)
    EditText edit_code;
    //endregion
    private int time = 60;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("用户注册");
        initBackTouch();
        if (GetIntentData(EWCode) != null) {
            edit_code.setText(GetIntentData(EWCode).toString());
            edit_code.setFocusable(false);
        }

        if (GetIntentData(DPID) != null) {
            edit_dpid.setText(GetIntentData(DPID).toString());
        }

        btn_obtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
                Matcher m = p.matcher(edit_phone.getText());
                if (m.matches()) {
                    JSONObject para = new JSONObject();
                    try {
                        para.put("mobile", edit_phone.getText());
                        para.put("sms_type", 1);//短信类型1、注册,2、发短信找回密码,3、会员中心修改手机号码
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(RegisterActivity.this, "sms_send", para, new AsynServer.BackObject() {

                        @Override
                        public void Back(JSONObject sender) {
                            try {
                                JSONObject json_statue = sender.getJSONObject("status");
                                Integer integer = json_statue.getInt("succeed");
                                switch (integer) {
                                    case 0:
                                        String err_desc = json_statue.getString("error_desc");
                                        MessageBox.Show(err_desc);
                                        break;
                                    case 1:
                                        RunTimer();
                                        break;
                                    default:
                                        Default.showToast("短信发送失败", Toast.LENGTH_LONG);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackActivity();
            }
        });

        lab_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity(AgreementActivity.class);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject para = new JSONObject();
                if (edit_password.getText().length() >= 6 && edit_password.getText().length() <= 20) {
                    if (checkBox.isChecked()) {
                        try {
                            para.put("invitation_code",edit_code.getText().toString());
                            para.put("mobile_phone", edit_phone.getText().toString());
                            para.put("password", edit_password.getText().toString());
                            para.put("sms_code", edit_message.getText().toString());
                            para.put("dpid",edit_dpid.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AsynServer.BackObject(RegisterActivity.this, "user/signup", para, new AsynServer.BackObject() {
                            @Override
                            public void Back(JSONObject sender) {
                                if (sender == null) {
                                    return;
                                }
                                try {
                                    JSONObject json_status = sender.getJSONObject("status");
                                    int succeed = json_status.getInt("succeed");
                                    if (succeed == 0) {
                                        MessageBox.Show(json_status.getString("error_desc"));
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    MessageBox.Show(e.getMessage());
                                    return;
                                }
                                JSONObject para = new JSONObject();
                                try {
                                    para.put("name", edit_phone.getText().toString());
                                    para.put("password", edit_password.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                AsynServer.BackObject(RegisterActivity.this, "user/signin", para, new AsynServer.BackObject() {
                                    @Override
                                    public void Back(JSONObject s) {
                                        JSONObject json_status = JsonClass.jj(s, "status");
                                        if (JsonClass.ij(json_status, "succeed") == 0) {
                                            MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                                        } else {
                                            JSONObject sender = JsonClass.jj(s, "data");
                                            JSONObject session = JsonClass.jj(sender,"session");
                                            User.setUserID(JsonClass.sj(session,"uid"));
                                            User.setSession(JsonClass.sj(session, "sid"));
                                            JSONObject json_activity = JsonClass.jj(sender,"is_activate");
                                            int activity_status = JsonClass.ij(json_activity,"status");
                                            User.setIs_activate(activity_status == 1);
                                            User.setActivity_msg(JsonClass.sj(json_activity,"msg"));
                                            User.setActivity_btn_cancel(JsonClass.sj(json_activity,"btn3"));
                                            User.setActivity_btn_goActivity(JsonClass.sj(json_activity,"btn1"));
                                            User.setActivity_btn_justBuy(JsonClass.sj(json_activity,"btn2"));
                                            User.setActivity_check_text(JsonClass.sj(json_activity,"checkbox"));
                                            JSONObject obj1 = JsonClass.jj(sender,"user") ;
                                            User.setRankName(JsonClass.sj(obj1,"rank_name"));
                                            if (GetIntentData(DPID) == null) {
                                                Intent intent1 = new Intent();
                                                intent1.putExtra(LoginActivity.USERNAME, edit_phone.getText().toString());
                                                intent1.putExtra(LoginActivity.PASSWORD, edit_password.getText().toString());
                                                BackActivity(intent1);
                                                finish();
                                            } else {
                                                Intent intent1 = new Intent();
                                                intent1.putExtra(ConvertActivity.DPId, edit_dpid.toString());
                                                AddActivity(ConvertActivity.class, 0, intent1);
                                                finish();
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
