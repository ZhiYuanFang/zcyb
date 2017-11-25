package com.wzzc.new_index.userCenter.regest.personalAgent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.pay.PayFromWX;
import com.wzzc.other_function.pay.PayFromZFB;
import com.wzzc.other_function.pay.PayView;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toutou on 2017/4/1.
 * <p>
 * 个人代理注册
 */

public class RegisterPersonalAgentActivity extends BaseActivity implements View.OnClickListener {

    //region 组件
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.et_passWord)
    private EditText et_passWord;
    @ViewInject(R.id.et_repeat_passWord)
    private EditText et_repeat_passWord;
    @ViewInject(R.id.et_promatName)
    private EditText et_promatName;
    @ViewInject(R.id.et_address)
    private EditText et_address;
    @ViewInject(R.id.et_code)
    private EditText et_code;
    @ViewInject(R.id.tv_getCode)
    private TextView tv_getCode;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    @ViewInject(R.id.radio_agree)
    private RadioButton radio_agree;
    @ViewInject(R.id.tv_deal)
    private TextView tv_deal;
    @ViewInject(R.id.eiv_personalAgent)
    private ExtendImageView eiv_personalAgent;
    @ViewInject(R.id.layout_none_info)
    private LinearLayout layout_none_info;
    @ViewInject(R.id.layout_none_code)
    private LinearLayout layout_none_code;
    //endregion
    public static final String NONE = "none";
    private Timer timer;
    private int time = 60;
    private int is_upgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initBackTouch();
    }

    private void init() {
        AsynServer.BackObject(this, "get_resources", false, null, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                JSONObject json_status = JsonClass.jj(sender,"status");
                if (JsonClass.ij(json_status,"succeed") == 1) {
                    JSONObject json_data = JsonClass.jj(sender,"data");
                    JSONObject json_signupAgent = JsonClass.jj(json_data,"signupAgent");
                    eiv_personalAgent.setPath(JsonClass.sj(json_signupAgent,"bg_img"));
                }
            }
        });

        tv_getCode.setOnClickListener(this);
        tv_submit.setOnClickListener(this);

        tv_deal.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(NONE)) {
            if (bundle.getBoolean(NONE)) {
                is_upgrade = 1;
                et_phone.setText("12345678910");
                et_passWord.setText("123456");
                et_repeat_passWord.setText("123456");
                et_code.setText("1234");
                layout_none_code.setVisibility(View.GONE);
                layout_none_info.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getCode: {
                //region 获取验证码
                Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
                Matcher m = p.matcher(et_phone.getText());
                if (m.matches()) {
                    JSONObject sender = Default.GetSession();
                    JSONObject filter = new JSONObject();
                    JSONObject para = new JSONObject();
                    try {
                        filter.put("uid", sender.getString("uid"));
                        filter.put("sid", sender.getString("sid"));
                        para.put("session", filter);
                        para.put("mobile", et_phone.getText());
                        para.put("sms_type", 1);//短信类型1、注册,2、发短信找回密码,3、会员中心修改手机号码
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(RegisterPersonalAgentActivity.this, "sms_send", para, new AsynServer.BackObject() {

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
                    MessageBox.Show("手机号码格式不正确！");
                }
                //endregion
                break;
            }
            case R.id.tv_submit: {
                //region 注册
                if (et_name.getText(). length() == 0 ) {
                    Default.showToast("请输入姓名", Toast.LENGTH_SHORT);
                } else if (et_phone.getText(). length() == 0 ) {
                    Default.showToast("请输入手机号", Toast.LENGTH_SHORT);
                } else if (et_passWord.getText(). length() == 0 ) {
                    Default.showToast("请输入密码", Toast.LENGTH_SHORT);
                } else if (!String.valueOf(et_passWord.getText()).equals(String.valueOf(et_repeat_passWord.getText()))) {
                    Default.showToast("两次密码输入不正确，请重新核对", Toast.LENGTH_SHORT);
                } else if (et_promatName.getText() . length() == 0 ) {
                    Default.showToast("请输入推介人账号", Toast.LENGTH_SHORT);
                } else if (et_address.getText(). length() == 0 ) {
                    Default.showToast("请输入收货地址", Toast.LENGTH_SHORT);
                } else if (et_code.getText() . length() == 0 ) {
                    Default.showToast("请输入验证码", Toast.LENGTH_SHORT);
                } else if (et_passWord.getText().length() >= 6 && et_passWord.getText().length() <= 20) {
                    if (radio_agree.isChecked()) {
                        try {
                            JSONObject para = new JSONObject();
                            para.put("session", Default.GetSession());
                            para.put("is_upgrade", is_upgrade);//如果是注册用户升级到个人代理,填1,否则填0
                            para.put("mobile_phone", String.valueOf(et_phone.getText()));
                            para.put("password", String.valueOf(et_passWord.getText()));
                            para.put("userName", String.valueOf(et_name.getText()));
                            para.put("recommenderAccount", String.valueOf(et_promatName.getText()));
                            para.put("detailAddress", String.valueOf(et_address.getText()));
                            para.put("sms_code", String.valueOf(et_code.getText()));
                            AsynServer.BackObject(RegisterPersonalAgentActivity.this, "user/signupAgent", para, new AsynServer.BackObject() {
                                @Override
                                public void Back(JSONObject sender) {
                                    try {
                                        JSONObject json_status = sender.getJSONObject("status");
                                        Integer succeed = json_status.getInt("succeed");
                                        if (succeed == 0) {
                                            //注册失败
                                            MessageBox.Show(json_status.getString("error_desc"));
                                        } else {
                                            //注册成功
                                            JSONObject json_data = sender.getJSONObject("data");
                                            final Double money = json_data.getDouble("agentcy_cost");
                                            MessageBox.Show(getString(R.string.app_name), "激活个人代理需缴纳" + money + "元,请选择是否激活.", new String[]{"稍后激活","立即激活"},  new MessageBox.MessBtnBack() {
                                                @Override
                                                public void Back(int index) {
                                                    switch (index){
                                                        case 0:
                                                            BackActivity();
                                                            break;
                                                        case 1:
                                                            final PayView payView = new PayView(RegisterPersonalAgentActivity.this);
                                                            payView.setInfo(false,null);
                                                            MessageBox.Show("个人代理激活",payView ,false, new String[]{"取消", "确认支付" + money + "元"}, new MessageBox.MessBtnBack() {
                                                                @Override
                                                                public void Back(int index) {
                                                                    switch (index){
                                                                        case 0:
                                                                            BackActivity();
                                                                            break;
                                                                        case 1:
                                                                            JSONObject para = new JSONObject();
                                                                            try {
                                                                                para.put("session", Default.GetSession());
                                                                                para.put("payment_id", payView.getPayment_id());
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            switch (payView.getPayment_id()){
                                                                                case 0:
                                                                                    Default.showToast("请选择支付方式",Toast.LENGTH_LONG);
                                                                                    break;
                                                                                case 1:
                                                                                    PayFromZFB.pay(RegisterPersonalAgentActivity.this, "user/signupAgentPayment", para, new MessageBox.MessBtnBack() {
                                                                                        @Override
                                                                                        public void Back(int index) {
                                                                                            Default.toClass(RegisterPersonalAgentActivity.this, new Intent(RegisterPersonalAgentActivity.this, LoginActivity.class));
                                                                                        }
                                                                                    }, new MessageBox.MessBtnBack() {
                                                                                        @Override
                                                                                        public void Back(int index) {
                                                                                            //失败
                                                                                        }
                                                                                    });
                                                                                    break;
                                                                                case 3:
                                                                                    PayFromWX.pay(RegisterPersonalAgentActivity.this,"user/signupAgentPayment", para, new MessageBox.MessBtnBack() {
                                                                                        @Override
                                                                                        public void Back(int index) {
                                                                                            Default.toClass(RegisterPersonalAgentActivity.this, new Intent(RegisterPersonalAgentActivity.this, LoginActivity.class));
                                                                                        }
                                                                                    }, new MessageBox.MessBtnBack() {
                                                                                        @Override
                                                                                        public void Back(int index) {
                                                                                            //失败
                                                                                        }
                                                                                    });
                                                                                    break;
                                                                            }
                                                                            break;
                                                                        default:
                                                                    }
                                                                }
                                                            });
                                                            break;
                                                        default:
                                                    }
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (JSONException json) {
                            json.printStackTrace();
                        }
                    } else {
                        Default.showToast("您还未同意本协议", Toast.LENGTH_SHORT);
                    }
                } else {
                    Default.showToast("密码长度错误", Toast.LENGTH_SHORT);
                }
                //endregion
                break;
            }
            case R.id.tv_deal: {
                //region 协议
                Default.readParse("http://www.zcyb365.com/zcybapp/images_txt/signupAgent_protocol.json", new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        String text = "暂时无法获取协议数据";
                        try {
                            JSONObject json = new JSONObject(String.valueOf(msg.obj));
                            text = json.getString("text");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPersonalAgentActivity.this);
                        builder.setTitle("用户协议");
                        builder.setMessage(text);
                        builder.setPositiveButton("同意", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                radio_agree.setChecked(true);
                            }
                        });
                        builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                radio_agree.setChecked(false);
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                builder.create().show();
                            }
                        });

                        return false;
                    }
                });
                //endregion
                break;
            }

            default:
        }
    }

    //region JSON

    //endregion

    //region sms
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

    //region handler
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if (time > 0) {
                        tv_getCode.setEnabled(false);
                        tv_getCode.setText(time + "秒后重新发送");
                        tv_getCode.setTextSize(14);
                    } else {
                        timer.cancel();
                        tv_getCode.setText("获取验证码");
                        tv_getCode.setEnabled(true);
                        tv_getCode.setTextSize(14);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };
    //endregion
    //endregion
}
