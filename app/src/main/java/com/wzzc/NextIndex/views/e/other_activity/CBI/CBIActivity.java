package com.wzzc.NextIndex.views.e.other_activity.CBI;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.NextIndex.views.e.other_activity.settings.SettingActivity;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/1.
 */

public class CBIActivity extends BaseActivity {
    //region ```
    @ViewInject(R.id.tv_cbi)
    TextView tv_cbi;
    @ViewInject(R.id.et_number)
    EditText et_number;
    @ViewInject(R.id.et_notes)
    EditText et_notes;
    @ViewInject(R.id.tv_reset)
    TextView tv_reset;
    @ViewInject(R.id.tv_submit)
    TextView tv_submit;
    @ViewInject(R.id.tv_category)
    TextView tv_info;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        init();
    }

    protected void init() {
        tv_cbi.setText(User.getCbi());
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_number.setText("");
                et_notes.setText("");
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (User.getUser_rank()) {
                    case "0":
                    case "1":
                    case "15":
                        MessageBox.Show(CBIActivity.this, Default.AppName, "注册会员和个人代理提现手续费是" + User.getCbi_withdrawal_rate() + "%", new String[]{"取消", "立即提现"}, new MessageBox.MessBtnBack() {
                            @Override
                            public void Back(int index) {
                                switch (index) {
                                    case 0:
                                        break;
                                    case 1:
                                        if (User.getCard_id().length() > 0) {
                                            submit();
                                        } else {
                                            MessageBox.dismiss();
                                            MessageBox.createNewDialog();
                                            MessageBox.Show(CBIActivity.this, Default.AppName, "前往填写银行卡", new String[]{"取消", "立即前往"}, new MessageBox.MessBtnBack() {
                                                @Override
                                                public void Back(int index) {
                                                    switch (index){
                                                        case 0:
                                                            break;
                                                        case 1:
                                                            AddActivity(SettingActivity.class);
                                                            break;
                                                        default:
                                                    }
                                                }
                                            });
                                        }
                                        break;
                                    default:
                                }
                            }
                        });
                        break;
                    case "6":
                        submit();
                        break;
                    default:
                        MessageBox.Show("当前等级暂未开放C米提现功能");
                        break;
                }
            }
        });
    }

    private void submit () {
        //region submit
        if (et_number.getText().toString().trim().length() > 0) {
            JSONObject para = new JSONObject();
            try {
                para.put("amount", et_number.getText());
                para.put("user_note", et_notes.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AsynServer.BackObject(CBIActivity.this, "account/apply_cbi", para, new AsynServer.BackObject() {
                @Override
                public void Back(JSONObject sender) {
                    try {
                        JSONObject json_status = sender.getJSONObject("status");
                        int succeed = json_status.getInt("succeed");
                        if (succeed == 1) {
                            MessageBox.Show(Default.AppName, "提现申请成功，等待工作人员审核。", new String[]{"确定"}, new MessageBox.MessBtnBack() {
                                @Override
                                public void Back(int index) {
                                    BackActivity();
                                }
                            });
                        } else {
                            MessageBox.Show("提现失败", json_status.getString("error_desc"), new String[]{"确定"}, new MessageBox.MessBtnBack() {
                                @Override
                                public void Back(int index) {

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Default.showToast("请输入提现金额", Toast.LENGTH_LONG);
        }
        //endregion
    }
}
