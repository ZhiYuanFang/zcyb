package com.wzzc.index.home.zcybStores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zcyb365 on 2016/12/3.
 */
public class GiftExchangeActivity extends BaseActivity {
    private final Timer timer = new Timer();
    private TimerTask task;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.edit_money)
    private EditText edit_money;
    @ViewInject(R.id.lab_thisshop)
    private TextView lab_thisshop;
    @ViewInject(R.id.lab_otherstore)
    private TextView lab_otherstore;
    @ViewInject(R.id.lab_mostconvertible)
    private TextView lab_mostconvertible;
    @ViewInject(R.id.lab_payable)
    private TextView lab_payable;
    @ViewInject(R.id.btn_sure)
    private Button btn_sure;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("兑换商品");
        asd();
        JSONObject para = new JSONObject();
        try {
            para.put("supplier_id", GetIntentData("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(GiftExchangeActivity.this, "supplier_exchange_list", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject ss) {
                try {
                    JSONObject sender = ss.getJSONObject("data");
                    if (sender == null) {

                    } else {
                        lab_name.setText("店铺：" + sender.getString("supplier_name"));
                        lab_thisshop.setText(sender.getString("current_store_zcb"));
                        lab_otherstore.setText(sender.getString("other_store_zcb"));
                        lab_mostconvertible.setText(sender.getString("user_sum_use_money"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject para = new JSONObject();
                try {
                    para.put("supplier_id", GetIntentData("id"));
                    para.put("need_exchange", edit_money.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(GiftExchangeActivity.this, "supplier_exchange_do", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject s) {
                        JSONObject json_status = JsonClass.jj(s, "status");
                        if (JsonClass.ij(json_status, "succeed") == 0) {
                            MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                        } else {
                            JSONObject sender = JsonClass.jj(s, "data");
                            AlertDialog.Builder builder = new AlertDialog.Builder(GiftExchangeActivity.this);
                            try {
                                if ("faild".equals(sender.getString("result"))) {
                                    builder.setMessage(sender.getString("msg"));
                                } else {
                                    builder.setMessage("兑换成功");
                                }
//                            builder.setMessage(sender.getString("msg"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            builder.setTitle(Default.AppName);
                            builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
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
            }
        });
    }

    private void asd() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                lab_payable.setText(edit_money.getText());
                super.handleMessage(msg);
            }
        };

        task = new

                TimerTask() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                };
        timer.schedule(task, 100, 100);
    }
}
