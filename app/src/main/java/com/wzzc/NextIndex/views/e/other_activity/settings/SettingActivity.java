package com.wzzc.NextIndex.views.e.other_activity.settings;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/10/10.
 */
public class SettingActivity extends BaseActivity {
    //region 组件
    @ViewInject(R.id.btn_modify)
    private Button btn_modify;
    @ViewInject(R.id.lab_birthday)
    private TextView lab_birthday;
    @ViewInject(R.id.secret)
    private RadioButton secret;
    @ViewInject(R.id.male)
    private RadioButton male;
    @ViewInject(R.id.female)
    private RadioButton female;
    @ViewInject(R.id.lab_mobile)
    private EditText lab_mobile;
    @ViewInject(R.id.card_number)
    private EditText card_number;
    @ViewInject(R.id.card_address)
    private EditText card_address;
    @ViewInject(R.id.card_name)
    private EditText card_name;
    @ViewInject(R.id.lab_code)
    private EditText lab_code;
    @ViewInject(R.id.layout_code)
    private LinearLayout layout_code;
    @ViewInject(R.id.sendCode)
    private TextView sendCode;
    @ViewInject(R.id.btn_clear_data)
    Button btn_clear_data;
    //endregion
    private String sex, birthday, phone, cardNumber, cardFrom, cardWanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddTitle("设置");

        getUserInfo();

        layout_code.setVisibility(View.GONE);

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_code.getVisibility() == View.VISIBLE && lab_code.getText().length() == 0) {
                    MessageBox.Show("请输入验证码");
                } else
                    submitUserInfo();
            }
        });

        secret.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sex = isChecked ? "0" : sex;
            }
        });

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sex = isChecked ? "1" : sex;
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sex = isChecked ? "2" : sex;
            }
        });

        lab_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(phone)) {
                    layout_code.setVisibility(View.VISIBLE);
                }
            }
        });

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode.setClickable(false);
                sendCode();
            }
        });
        btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Default.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(Default.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
                    //判断是否需要 向用户解释，为什么要申请该权限
                    ActivityCompat.shouldShowRequestPermissionRationale(Default.getActivity(), Manifest.permission.READ_CONTACTS);
                } else {
                    FileInfo.ClearUserString(SettingActivity.this);//删除缓存数据
                    ImageHelper.clearImage(SettingActivity.this);//删除缓存图片
                    Default.showToast("清空缓存");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int index = 0; index < permissions.length; index++) {
            switch (permissions[index]) {
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    if (requestCode == 1002) {
                        if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                            /*用户已经受权*/
                            FileInfo.ClearUserString(SettingActivity.this);//删除缓存数据
                            ImageHelper.clearImage(SettingActivity.this);//删除缓存图片
                            Default.showToast("清空缓存");
                        } else {
                            /*用户应该拒绝了权限*/
                            Default.showToast("拒绝");
                        }
                    }
                    break;
                default:
            }
        }
    }

    private void sendCode() {
        JSONObject para = new JSONObject();
        try {
            para.put("session", Default.GetSession());
            para.put("mobile", lab_mobile.getText());
            para.put("sms_type", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "sms_send", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        Default.showToast("验证码发送成功", Toast.LENGTH_LONG);
                        Message msg = new Message();
                        msg.arg1 = 60;
                        handler.handleMessage(msg);
                    } else {
                        MessageBox.Show(json_status.getString("error_desc"));
                        sendCode.setClickable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void submitUserInfo() {
        JSONObject para = new JSONObject();
        try {
            para.put("session", Default.GetSession());
            para.put("birthday", lab_birthday.getText());
            para.put("sex", sex);
            para.put("mobile", lab_mobile.getText());
            para.put("sms_code", lab_code.getText());
            para.put("card_number", card_number.getText());
            para.put("card_address", card_address.getText());
            para.put("card_name", card_name.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "user/setup", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 0) {
                        MessageBox.Show(json_status.getString("error_desc"));
                    } else {
                        User.setCard_id(card_number.getText().toString());
                        User.saveUser(SettingActivity.this);
                        MessageBox.Show("修改成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getUserInfo() {
        JSONObject para = new JSONObject();
        try {
            para.put("session", Default.GetSession());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "user/setupInfo", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject s) {
                JSONObject json_status = JsonClass.jj(s, "status");
                if (JsonClass.ij(json_status, "succeed") == 0) {
                    MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                } else {
                    JSONObject sender = JsonClass.jj(s, "data");
                    sex = JsonClass.sj(sender, "sex");
                    birthday = JsonClass.sj(sender, "birthday");
                    JSONArray other = JsonClass.jrrj(sender, "other");
                    JSONObject json_phone = JsonClass.jjrr(other, 0);
                    JSONObject json_cardNumber = JsonClass.jjrr(other, 1);
                    JSONObject json_cardFrom = JsonClass.jjrr(other, 2);
                    JSONObject json_cardWanner = JsonClass.jjrr(other, 3);
                    phone = getContent(json_phone);
                    cardNumber = getContent(json_cardNumber);
                    cardFrom = getContent(json_cardFrom);
                    cardWanner = getContent(json_cardWanner);
                    setInfo();
                }
            }
        });
    }

    private void setInfo() {
        lab_birthday.setText(birthday);
        secret.setChecked(sex.equals("0"));
        male.setChecked(sex.equals("1"));
        female.setChecked(sex.equals("2"));
        lab_mobile.setText(phone);
        card_number.setText(cardNumber);
        card_address.setText(cardFrom);
        card_name.setText(cardWanner);
    }

    private String getContent(JSONObject json) {
        try {
            return json.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @OnClick(R.id.lab_birthday)
    public void lab_birthday_click(final TextView view) {

        if (birthday == null) {
            birthday = "1980-1-1";
        }
        String[] strs = birthday.split("-");

        DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                String data = arg1 + "-" + (arg2 + 1) + "-" + arg3;
                view.setText(data);
            }
        }, Integer.valueOf(strs[0]), Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
        dateDialog.show();
    }

    //region handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int time = msg.arg1;
            if (time > 0) {
                sendCode.setText(time + "秒");
                Message message = new Message();
                message.arg1 = --time;
                sendMessageDelayed(message, 1000);
            } else {
                sendCode.setText("发送验证码");
                sendCode.setClickable(true);
            }

        }
    };
    //endregion

}
