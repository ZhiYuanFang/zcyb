package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.data.main_view.tradingarea;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcyb365 on 2017/1/5.
 */
public class ShopeDateActivity extends BaseActivity {
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;

    @ViewInject(R.id.lab_number1)
    private TextView lab_number1;
    @ViewInject(R.id.lab_money)
    private TextView lab_money;

    @ViewInject(R.id.btn_see)
    private Button btn_see;
//    @ViewInject(R.id.myView)
//    private LineCharView myView;

    @ViewInject(R.id.lab_time)
    private TextView lab_time;
    @ViewInject(R.id.lab_time1)
    private TextView lab_time1;
    CommonChartView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("店铺数据报表");
        myView = (CommonChartView) findViewById(R.id.myView);
        lab_name.setText("" + GetIntentData("name"));
        lab_number.setText("账号：" + GetIntentData("number"));

        btn_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject para = new JSONObject();
                JSONObject sender = Default.GetSession();
                try {
                    JSONObject filter = new JSONObject();
                    filter.put("uid", sender.getString("uid"));
                    filter.put("sid", sender.getString("sid"));
                    para.put("session", filter);
                    para.put("user_id", GetIntentData("uid"));
                    para.put("startTime", lab_time.getText());
                    para.put("endTime", lab_time1.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(ShopeDateActivity.this , "seller/dataCharts", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(final JSONObject sender) {
                        try {
                            lab_number1.setText(sender.getJSONObject("data").getString("order_count_sum"));
                            lab_money.setText(sender.getJSONObject("data").getString("order_goods_sum"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        List<String> x_coords = new ArrayList<String>();
        x_coords.add("1");
        x_coords.add("2");
        x_coords.add("3");
        x_coords.add("4");
        x_coords.add("5");
        x_coords.add("6");
        x_coords.add("7");
        x_coords.add("8");
        x_coords.add("9");
        x_coords.add("10");
//        List<String> x_coord_values = new ArrayList<String>();
        List<List<Float>> x_coord_values = new ArrayList<>();
//        x_coord_values.add(String.valueOf(1));
//        x_coord_values.add(String.valueOf(500));
//        x_coord_values.add(String.valueOf(600));
//        x_coord_values.add(String.valueOf(50));
//        x_coord_values.add(String.valueOf(10000));
//        x_coord_values.add(String.valueOf(2000));
        x_coord_values.add(new ArrayList<Float>(){{add(1f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(50f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(300f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(400f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(500f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(600f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(1000f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(1500f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(2000f);}});
        x_coord_values.add(new ArrayList<Float>(){{add(10000f);}});
//        myView.setBgColor();
        myView.setValue(x_coords, x_coord_values);

    }

    @OnClick({R.id.lab_time, R.id.lab_time1})
    public void lab_birthday_click(final TextView view) {
        Dialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                String data = arg1 + "-" + (arg2 + 1) + "-" + arg3;
                view.setText(data);
            }
        }, 2015, 0, 1);
        dateDialog.setTitle("请选择日期");
        dateDialog.show();
    }
}
