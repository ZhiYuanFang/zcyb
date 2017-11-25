package com.wzzc.NextIndex.views.e.other_activity.address.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.address.AddressDelegate;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/14.
 *
 */

public class AddressItemView extends BaseView {
    //region ```
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.tv_address)
    TextView tv_address;
    @ViewInject(R.id.tv_address_info)
    TextView tv_address_info;
    @ViewInject(R.id.tv_edit_address)
    TextView tv_edit_address;
    @ViewInject(R.id.tv_delete_address)
    TextView tv_delete_address;
    @ViewInject(R.id.radio_default)
    RadioButton radio_default;
    //endregion
    AddressDelegate ad;
    String addressID;

    public AddressItemView(Context context) {
        super(context);
    }

    public AddressItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void init() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.lookAddress(addressID);
            }
        });

        radio_default.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ad.setDefaultAddress(addressID);
            }
        });

        tv_edit_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.editAddress(addressID);
            }
        });

        tv_delete_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.deleteAddress(addressID);
            }
        });
    }

    /**
     * {
     * "id": "1651",
     * "consignee": "text",
     * "address": "aga",
     * "tel": "13259726321",
     * "mobile": "13259726321",
     * "country": "1",
     * "country_name": "中国",
     * "province": "4",
     * "province_name": "福建",
     * "city": "55",
     * "city_name": "南平",
     * "district": "539",
     * "district_name": "邵武市",
     * "default_address": 1
     * }
     */
    public void setInfo(AddressDelegate ad, JSONObject json) {
        this.ad = ad;
        try {
            addressID = json.getString("id");
            tv_name.setText(json.getString("consignee"));
            tv_address_info.setText(json.getString("address"));
            tv_phone.setText(json.getString("mobile"));
            radio_default.setChecked(json.getInt("default_address") == 1);
            StringBuilder sder = new StringBuilder();
            sder.append(json.getString("province_name")).append(json.getString("city_name")).append(json.getString("district_name"));
            tv_address.setText(sder);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        init();
    }
}
