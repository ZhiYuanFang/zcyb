package com.wzzc.NextIndex.views.e;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.zcybStores.ConvertActivity;
import com.wzzc.new_index.userCenter.password.RetrieveActivity;
import com.wzzc.new_index.userCenter.regest.ordinary.RegisterActivity;
import com.wzzc.new_index.userCenter.regest.personalAgent.RegisterPersonalAgentActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.jpush.MyJpush;
import com.wzzc.other_view.BasicTitleView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2016/10/8.
 * <p>
 * 登陆界面
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    LoginDelegate loginDelegate;
    //region 组件
    @ViewInject(R.id.btn_register)
    private TextView btn_register;
    @ViewInject(R.id.user_name)
    private EditText user_name;
    @ViewInject(R.id.user_password)
    private EditText user_password;
    @ViewInject(R.id.btn_nopassword)
    private TextView btn_nopassword;
    @ViewInject(R.id.btn_login)
    private Button btn_login;
    private String username, userpassword;
    @ViewInject(R.id.ib_lookPassword)
    private ImageButton ib_lookPassword;
    @ViewInject(R.id.btn_register_personal_agents)
    private Button btn_register_personal_agents;
    @ViewInject(R.id.basicTitleView)
    private BasicTitleView basicTitleView;
    //endregion
    public static final String LoginDelegate = "ld";//登陆代理
    public static final String USERNAME = "username", PASSWORD = "password";
    public static final String DPId = "dpid";

    String dpid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(USERNAME) && bundle.containsKey(PASSWORD)) {
            user_name.setText(bundle.getString(USERNAME));
            user_password.setText(bundle.getString(PASSWORD));
        }
        if (bundle.containsKey(DPId)) {
            dpid = bundle.getString(DPId);
        }
        if (bundle.containsKey(LoginDelegate)) {
            loginDelegate = bundle.getParcelable(LoginDelegate);
        }
    }

    private void init () {
        btn_register.setOnClickListener(this);
        btn_register_personal_agents.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_nopassword.setOnClickListener(this);
        ib_lookPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                //region 注册
                Intent intent = new Intent();
                intent.putExtra(RegisterActivity.DPID,dpid);
                AddActivity(RegisterActivity.class, 1, intent);
                //endregion
                break;
            case R.id.btn_register_personal_agents:
                AddActivity(RegisterPersonalAgentActivity.class);
                break;
            case R.id.btn_login: {
                //region 登陆
                username = user_name.getText().toString();
                userpassword = user_password.getText().toString();

                if (username != null && userpassword != null && !"".equals(username.trim()) && !"".equals(userpassword.trim())) {
                    JSONObject para = new JSONObject();
                    try {
                        para.put("name", username);
                        para.put("password", userpassword);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(LoginActivity.this, "user/signin", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject s) {
                            JSONObject json_status = JsonClass.jj(s,"status");
                            if (JsonClass.ij(json_status,"succeed") == 0) {
                                MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                            } else {
                                MyJpush.setAlias(username, new MyJpush.AliasOK() {
                                    @Override
                                    public void aliasOK() {
                                        if (GetIntentData("dpid") == null) {
                                            onBackPressed();
                                        } else {
                                            Intent intent1 = new Intent();
                                            intent1.putExtra("dpid", Integer.valueOf(GetIntentData("dpid").toString()));
                                            AddActivity(ConvertActivity.class, 0, intent1);
                                            finish();
                                        }
                                    }
                                });
                                JSONObject sender = JsonClass.jj(s, "data");
                                JSONObject session = JsonClass.jj(sender,"session");
                                User.setUserID(JsonClass.sj(session,"uid"));
                                User.setSession(JsonClass.sj(session,"sid"));
                                JSONObject obj1 = JsonClass.jj(sender,"user");
                                User.setHeadUrl(JsonClass.sj(obj1,"avatar"));
                                User.setRankName(JsonClass.sj(obj1,"rank_name"));
                                JSONObject json_activity = JsonClass.jj(obj1,"is_activate");
                                int activity_status = JsonClass.ij(json_activity,"status");
                                User.setIs_activate(activity_status == 1);
                                User.setActivity_msg(JsonClass.sj(json_activity,"msg"));
                                User.setActivity_btn_cancel(JsonClass.sj(json_activity,"btn3"));
                                User.setActivity_btn_goActivity(JsonClass.sj(json_activity,"btn1"));
                                User.setActivity_btn_justBuy(JsonClass.sj(json_activity,"btn2"));
                                User.setActivity_check_text(JsonClass.sj(json_activity,"checkbox"));
                                User.saveUser(LoginActivity.this);
                        }
                        }
                    });
                } else {
                    MessageBox.Show("账号或密码不能为空");
                }
                //endregion
                break;
            }
            case R.id.btn_nopassword:
                //region 忘记密码
                AddActivity(RetrieveActivity.class);
                //endregion
                break;
            case R.id.ib_lookPassword:
                //region 查看密码
                if (user_password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    user_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ib_lookPassword.setBackgroundResource(R.drawable.look_password);
                } else {
                    user_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ib_lookPassword.setBackgroundResource(R.drawable.lookpassword1);
                }
                //endregion
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (loginDelegate != null) {
            loginDelegate.loginOK();
        } else {
            Default.showToast("no Delegate!");
        }
    }
}
