package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.withdrawals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.withdrawals.main_view.ShopWithdrawalsView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2017/1/3.
 *
 * 商家佣金提现
 */
public class ShopWithdrawalsActivity extends BaseActivity {
    //region 组件
    @ViewInject(R.id.btn_shopmoney)
    private Button btn_shopmoney;
    @ViewInject(R.id.btn_shoppassword)
    private Button btn_shoppassword;
    @ViewInject(R.id.btn_tx)
    private Button btn_tx;
    @ViewInject(R.id.show_shopmoney)
    private LinearLayout show_shopmoney;
    @ViewInject(R.id.show_shoppassword)
    private LinearLayout show_shoppassword;
    @ViewInject(R.id.show_tx)
    private LinearLayout show_tx;
    //提现
    @ViewInject(R.id.text_money)
    private TextView text_money;
    @ViewInject(R.id.input_money)
    private EditText input_money;
    @ViewInject(R.id.input_password)
    private EditText input_password;
    @ViewInject(R.id.btn_txsure)
    private Button btn_txsure;
    //提现密码
    @ViewInject(R.id.old_password)
    private EditText old_password;
    @ViewInject(R.id.new_password)
    private EditText new_password;
    @ViewInject(R.id.btn_passwordsure)
    private Button btn_passwordsure;
    //提现日志
    private CollectionAdapter adapter;
    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    @ViewInject(R.id.lab_noshopping)
    private LinearLayout lab_noshopping;
    //endregion

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("商家佣金提现");
        btn();

        adapter = new CollectionAdapter(this);
        main_view.setAdapter(adapter);
        btn_shopmoney.setBackgroundColor(Color.RED);
        btn_shopmoney.setTextColor(Color.WHITE);
        show_shopmoney.setVisibility(View.VISIBLE);
        show_shoppassword.setVisibility(View.GONE);
        show_tx.setVisibility(View.GONE);
        JSONObject para1 = new JSONObject();
        try {
            para1.put("is_submit", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(ShopWithdrawalsActivity.this , "seller/withdraw", para1, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject status = sender.getJSONObject("status");
                    int succeed = status.getInt("succeed");
                    if (succeed == 0) {
                           error(status.getString("error_desc"));
                    } else {
                        text_money.setText("￥" + sender.getJSONObject("data").getString("amount") + "元");
                        GetServerInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void error (String msg) {
        String[] strs = new String[1];
        strs[0] = "确定";
        MessageBox.Show(getString(R.string.app_name),msg,strs, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                finish();
            }
        });
    }

    public void GetServerInfo() {
        JSONObject para = new JSONObject();
        AsynServer.BackObject(ShopWithdrawalsActivity.this , "seller/withdraw_log", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        error(status.getString("error_desc"));
                    }else {
                        JSONArray arr = sender.getJSONObject("data").getJSONArray("list");
                        if (arr.length() == 0) {
                            lab_noshopping.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        adapter.addData(json);
                        Default.fixListViewHeight(main_view);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    class CollectionAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public CollectionAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            int count = this.data.size();
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ShopWithdrawalsView view;
            if (convertView != null) {
                view = (ShopWithdrawalsView) convertView;
            } else {
                view = new ShopWithdrawalsView
                        (this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

    //提交按钮
    private void btn(){
        btn_txsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject para = new JSONObject();
                JSONObject sender = Default.GetSession();
                try {
                    JSONObject filter = new JSONObject();
                    filter.put("uid", sender.getString("uid"));
                    filter.put("sid", sender.getString("sid"));
                    para.put("session", filter);
                    para.put("withdraw_money", input_money.getText());
                    para.put("withdraw_password", input_password.getText());
                    para.put("is_submit", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(ShopWithdrawalsActivity.this , "seller/withdraw", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        try {
                            if (sender.getJSONObject("status").getInt("succeed")==1){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShopWithdrawalsActivity.this);
                                builder.setMessage("提现成功！");
                                builder.setTitle(Default.AppName);
                                builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                        input_money.setText("");
                                        input_password.setText("");
                                    }
                                });
                                builder.create().show();
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShopWithdrawalsActivity.this);
                                builder.setMessage(sender.getJSONObject("status").getString("error_desc"));
                                builder.setTitle(Default.AppName);
                                builder.setPositiveButton("确定", null);
                                builder.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        btn_passwordsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("确认提现", " 确认提现 ");
                JSONObject para = new JSONObject();
                JSONObject sender = Default.GetSession();
                try {
                    JSONObject filter = new JSONObject();
                    filter.put("uid", sender.getString("uid"));
                    filter.put("sid", sender.getString("sid"));
                    para.put("session", filter);
                    para.put("origin_password", old_password.getText());
                    para.put("password", new_password.getText());
                    para.put("set_type", 2);
                    para.put("is_submit", 1);
                    Log.d("确认提现", " 确认提现111111 ");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(ShopWithdrawalsActivity.this , "seller/set_password", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        try {
                            Log.d("确认提现", " 确认提现1222222 ");
                            if (sender.getJSONObject("status").getInt("succeed")==1){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShopWithdrawalsActivity.this);
                                builder.setMessage("密码修改成功！");
                                builder.setTitle(Default.AppName);
                                builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                        old_password.setText("");
                                        new_password.setText("");
                                    }
                                });
                                builder.create().show();
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShopWithdrawalsActivity.this);
                                builder.setMessage(sender.getJSONObject("status").getString("error_desc"));
                                builder.setTitle(Default.AppName);
                                builder.setPositiveButton("确定", null);
                                builder.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    //功能切换
    @OnClick({R.id.btn_shopmoney,R.id.btn_shoppassword,R.id.btn_tx})
    public void lab_setting_click(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag==0){
            btn_shopmoney.setBackgroundColor(Color.RED);
            btn_shopmoney.setTextColor(Color.WHITE);
            btn_shoppassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_shoppassword.setTextColor(Color.BLACK);
            btn_tx.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_tx.setTextColor(Color.BLACK);
            show_shopmoney.setVisibility(View.VISIBLE);
            show_shoppassword.setVisibility(View.GONE);
            show_tx.setVisibility(View.GONE);
        }else if (tag==1){
            btn_shoppassword.setBackgroundColor(Color.RED);
            btn_shoppassword.setTextColor(Color.WHITE);
            btn_shopmoney.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_shopmoney.setTextColor(Color.BLACK);
            btn_tx.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_tx.setTextColor(Color.BLACK);
            show_shopmoney.setVisibility(View.GONE);
            show_shoppassword.setVisibility(View.VISIBLE);
            show_tx.setVisibility(View.GONE);
        }else if (tag==2){
            btn_tx.setBackgroundColor(Color.RED);
            btn_tx.setTextColor(Color.WHITE);
            btn_shopmoney.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_shopmoney.setTextColor(Color.BLACK);
            btn_shoppassword.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_shoppassword.setTextColor(Color.BLACK);
            show_shopmoney.setVisibility(View.GONE);
            show_shoppassword.setVisibility(View.GONE);
            show_tx.setVisibility(View.VISIBLE);
        }
    }

}
