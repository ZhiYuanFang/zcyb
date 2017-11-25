package com.wzzc.NextIndex.views.e.other_activity.address.other_activity;

import android.os.Bundle;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.address.other_activity.main_view.AddressClearView;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/15.
 *
 */

public class AddressLookActivity extends BaseActivity {
    public static final String ADDRESS_ID = "address_id";
    @ViewInject(R.id.acv)
    AddressClearView acv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        setInfoFromService();
    }

    void setInfoFromService () {
        JSONObject para = new JSONObject();
        try {
            para.put("address_id",GetIntentData(ADDRESS_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "address/info", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        JSONObject data = sender.getJSONObject("data");
                        acv.setInfo(data,AddressClearView.LOOK);
                    } else {
                        Default.showToast(json_status.getString("error_desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
