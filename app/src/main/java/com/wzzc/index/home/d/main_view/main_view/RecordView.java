package com.wzzc.index.home.d.main_view.main_view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/12/7.
 */
public class RecordView extends BaseView {
    @ViewInject(R.id.lab_time)
    private TextView time;
    @ViewInject(R.id.tv_name)
    private TextView name;
    @ViewInject(R.id.lab_address)
    private TextView lab_address;
    int id;

    public RecordView(Context context) {
        super(context);
        lab_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("未设置收货地址".equals(lab_address.getText())){
                    Intent intent=new Intent();
                    intent.putExtra("id",id);
                    GetBaseActivity().AddActivity(DzpAddressActivity.class,0,intent);
                }
            }
        });

    }
    public void setInfo(JSONObject[] data) {
        try {
            time.setText(data[0].getString("date_add"));
            name.setText(data[0].getString("award"));
            id=data[0].getInt("id");
            if (data[0].getInt("is_award")!=0){
                if (data[0].getInt("log_id")==0){
                    lab_address.setText("未设置收货地址");
                }else {
                    lab_address.setText("已设置收货地址");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
