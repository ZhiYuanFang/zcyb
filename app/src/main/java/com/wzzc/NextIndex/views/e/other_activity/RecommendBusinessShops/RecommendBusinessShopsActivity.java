package com.wzzc.NextIndex.views.e.other_activity.RecommendBusinessShops;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.RegionView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/4/5.
 *
 * 推荐商铺
 */

public class RecommendBusinessShopsActivity extends BaseActivity{

    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    @ViewInject(R.id.regionView)
    private RegionView regionView;
    @ViewInject(R.id.tv_msg)
    private TextView tv_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] regions = regionView.getRegionId();
                if (regions != null) {
                    JSONObject para = new JSONObject();
                    try {
                        para.put("session", Default.GetSession());
                        para.put("country",regions[0]);
                        para.put("province",regions[1]);
                        para.put("city",regions[2]);
                        para.put("district",regions[3]);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(RecommendBusinessShopsActivity.this, "seller/apply_account", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(JSONObject sender) {
                            try {
                                JSONObject status = sender.getJSONObject("status");
                                int succeed = status.getInt("succeed");
                                if (succeed == 1){
                                    Default.showToast("推荐店铺成功", Toast.LENGTH_LONG);
                                    JSONObject data = sender.getJSONObject("data");
                                    String msg = data.getString("msg");
                                    tv_msg.setText("\u3000\u3000" + msg);
                                } else {
                                    tv_msg.setText("\u3000\u3000" + status.getString("error_desc"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}
