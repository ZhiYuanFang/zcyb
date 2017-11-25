package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.replenishment;

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
 * Created by zcyb365 on 2016/11/15.
 */
public class ReplenishmentActivity extends BaseActivity {
    @ViewInject(R.id.edit_usid)
    private EditText edit_usid;
    @ViewInject(R.id.edit_money)
    private EditText edit_money;
    @ViewInject(R.id.btn_tj)
    private Button btn_tj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject para = new JSONObject();
                final JSONObject session = Default.GetSession();
                try {
                    JSONObject filter = new JSONObject();
                    filter.put("uid", session.getString("uid"));
                    filter.put("sid", session.getString("sid"));
                    para.put("session", filter);
                    para.put("name",edit_usid.getText());
                    para.put("money",edit_money.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(ReplenishmentActivity.this , "fpbhe", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        try {
                            JSONObject jon=sender.getJSONObject("status");
                            int i=jon.getInt("succeed");
                            if (i==0){
                                MessageBox.Show(jon.getString("error_desc"));
                            }else {
                                MessageBox.Show("提交成功，系统审核之后会把相应的子成币和工厂币存入消费者的账户！");
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
