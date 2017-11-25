package com.wzzc.onePurchase.activity.index.main_view.center.activity.PersonalSettings.main_view;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.PersonalSettings.PersonalSettingsActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/28.
 *
 * 个人设置
 */

public class PersonalSettingsView extends BaseView {

    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.et_signature)
    private EditText et_signature;
    @ViewInject(R.id.bt_submit)
    private TextView bt_submit;

    private String rec_id,phone,name,signature;

    public PersonalSettingsView(Context context) {
        super(context);
        init();
    }
    private void init () {
        et_phone.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        et_phone.setFocusable(false);

        //region Editor
        et_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    phone = String.valueOf(et_phone.getText());
                }
                return false;
            }
        });

        et_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    name = String.valueOf(et_name.getText());
                }
                return false;
            }
        });

        et_signature.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signature = String.valueOf(et_signature.getText());
                }
                return false;
            }
        });

        //endregion
        
        bt_submit.setOnClickListener(submitListener());
    }

    public void setInfo (JSONObject sender){
        if (sender != null) {
            try {
                name = sender.getString(PersonalSettingsActivity.NAME);
                rec_id = sender.getString(PersonalSettingsActivity.RECID);
                phone = sender.getString(PersonalSettingsActivity.MABELPHONE);
                signature = sender.getString(PersonalSettingsActivity.SIGNATURE);
            } catch (JSONException e) {
                e.printStackTrace();
                nullInit();
            }
        } else {
            nullInit();
        }


        initialized();
    }

    private void nullInit () {
        name = "";
        rec_id = "";
        phone = "";
        signature = "";
    }

    private void initialized () {
        et_phone.setText(phone);
        et_name.setText(name);
        et_signature.setText(signature);
    }
    
    //region Action
    protected OnClickListener submitListener () {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/3/28 访问服务器 提交保存
                Default.showToast(getContext().getString(R.string.notDevelop), Toast.LENGTH_SHORT);
            }
        };
        
        return listener;
    }
    //endregion
}
