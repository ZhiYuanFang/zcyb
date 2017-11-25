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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextTBSearch.main_view.TBrebackListActivity;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.pay.PayFromWX;
import com.wzzc.other_function.pay.PayFromZFB;
import com.wzzc.other_function.pay.PayView;
import com.wzzc.other_view.BasicTitleView;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupAgentPreActivity extends BaseActivity implements View.OnClickListener{

    public static final String CATEGORY = "category";//分类

    public int mType = 0;
    protected BasicTitleView mTitleView;
    public String agreement;
    protected ArrayAdapter<String> adapter;

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
    @ViewInject(R.id.layout_none_info)
    private LinearLayout layout_none_info;
    @ViewInject(R.id.layout_none_code)
    private LinearLayout layout_none_code;
    @ViewInject(R.id.spinner4)
    private Spinner spinner;

    //endregion
    public static final String NONE = "none";
    private Timer timer;
    private int time = 60;
    private int is_upgrade;
    protected int uptype = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initBackTouch();
    }

    private void init() {
        // 类型
        mType = (int)GetIntentData(CATEGORY);
        // 标题
        mTitleView = (BasicTitleView) findViewById(R.id.basicTitleView);
        switch (mType){
            case 5:
                mTitleView.setTitle("淘金店主在线升级");
                break;
            case 6:
                mTitleView.setTitle("个人代理在线升级");
                break;
            default:
                break;
        }

        initPro();

//        tv_getCode.setOnClickListener(this);
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

    public void initPro(){
        JSONObject params = new JSONObject();
        int type = 0;
        switch (mType){
            case 5:
                type = 0;
                break;
            case 6:
                type = 1;
                break;
            default:
                break;
        }
        try{
            params.put("type", type);
        }catch (JSONException e){

        }
        AsynServer.BackObject(this, "user/signupAgentPre", params, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                JSONObject json_status = JsonClass.jj(sender,"status");
                if (JsonClass.ij(json_status,"succeed") == 1) {
                    JSONObject json_data = JsonClass.jj(sender,"data");
                    agreement = json_data.optString("agreement");
                    initSpinner(JsonClass.jrrj(json_data, "goods_list"));
                }
            }
        });
    }

    protected void initSpinner(JSONArray jsonArray){
        if(jsonArray == null || jsonArray.length() == 0)
            return;

        List listArray = new ArrayList<String>();
        for(int i = 0; i < jsonArray.length(); ++i){
            JSONObject jsonObj = jsonArray.optJSONObject(i);
            listArray.add(jsonObj.optString("goods_name"));
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if(0 != listArray.size())
            spinner.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getCode: {
                //region 获取验证码
                //endregion
                break;
            }
            case R.id.tv_submit: {
                //region 注册
                nextPage();
                break;
            }
            case R.id.tv_deal: {
                //region 协议
                final AlertDialog.Builder builder = new AlertDialog.Builder(SignupAgentPreActivity.this);
                builder.setTitle("用户协议");
                builder.setMessage(agreement);
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
                //endregion
                break;
            }
            default:
        }
    }

    protected void nextPage(){
        if(!radio_agree.isChecked()){
            Default.showToast("您还未同意本协议", Toast.LENGTH_SHORT);
            return;
        }
        if(et_name.getText().toString().length() == 0){
            Default.showToast("请输入您的姓名", Toast.LENGTH_SHORT);
            return;
        }
        if(et_promatName.getText().toString().length() == 0){
            Default.showToast("请输入推荐人账号", Toast.LENGTH_SHORT);
            return;
        }
        if(et_address.getText().toString().length() == 0){
            Default.showToast("请输入您的地址信息", Toast.LENGTH_SHORT);
            return;
        }

        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("goodsName", (String)spinner.getSelectedItem());
            jsonObj.put("is_upgrade", 1);
            jsonObj.put("userName", et_name.getText().toString());
            jsonObj.put("recommenderAccount", et_promatName.getText().toString() );
            jsonObj.put("detailAddress", et_address.getText().toString());
            switch (mType){
                case 5:
                    uptype = 0;
                    jsonObj.put("type", 0);
                    break;
                case 6:
                    uptype = 1;
                    jsonObj.put("type", 1);
                    break;
                default:
                    break;
            }
        }catch (JSONException e){

        }

        AsynServer.BackObject(this, "user/signupAgent", jsonObj, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                JSONObject json_status = JsonClass.jj(sender,"status");
                if (JsonClass.ij(json_status,"succeed") == 1) {
                    JSONObject json_data = JsonClass.jj(sender,"data");
                    String agentcy_cost = json_data.optString("agentcy_cost", "");

                    Intent intent = new Intent();
                    intent.putExtra(SignupagentpayActivity.Type_Key, uptype);
                    intent.putExtra(SignupagentpayActivity.Money_Key, agentcy_cost);
                    AddActivity(SignupagentpayActivity.class, 0, intent);
                }
            }
        });
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
    };
}