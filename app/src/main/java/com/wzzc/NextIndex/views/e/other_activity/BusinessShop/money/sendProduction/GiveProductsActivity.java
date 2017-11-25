package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.sendProduction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/12/10.
 */
public class GiveProductsActivity extends BaseActivity {
    @ViewInject(R.id.edit_usid)
    private EditText edit_usid;
    @ViewInject(R.id.edit_money)
    private EditText edit_money;
    @ViewInject(R.id.edit_money1)
    private EditText edit_money1;
    @ViewInject(R.id.btn_tj)
    private Button btn_tj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("赠送产品");

        btn_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject para = new JSONObject();
                JSONObject sender = Default.GetSession();
                try {
                    JSONObject filter = new JSONObject();
                    filter.put("sid", sender.getString("sid"));
                    para.put("session", filter);
                    para.put("name",edit_usid.getText());
                    para.put("money",edit_money.getText());
                    para.put("Surplus",edit_money1.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(GiveProductsActivity.this , "user/zscp", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GiveProductsActivity.this);
                        try {
                            if (sender.getJSONObject("status").getInt("succeed")==1){
                                MessageBox.Show(sender.getJSONObject("data").getString("message"));
                            }else {
                                MessageBox.Show(sender.getJSONObject("status").getString("error_desc"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}
