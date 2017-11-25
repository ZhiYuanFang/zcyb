package com.wzzc.index.home.d.main_view.main_view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.wzzc.index.home.d.main_view.RecordActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcyb365 on 2017/1/5.
 */
public class DzpAddressActivity extends BaseActivity {

    @ViewInject(R.id.spinner)
    private Spinner spinner;
    @ViewInject(R.id.spinner1)
    private Spinner spinner1;
    @ViewInject(R.id.spinner2)
    private Spinner spinner2;
    @ViewInject(R.id.spinner3)
    private Spinner spinner3;
    private String str,time;
    public List<String> data_list, data_list1, data_list2,data_list3;
    int id1, id2,id3,id11,id12,id13;
    public ArrayAdapter<String> arr_adapter, arr_adapter1, arr_adapter2,arr_adapter3;
    @ViewInject(R.id.lab_address)
    private EditText lab_address;
    @ViewInject(R.id.tv_name)
    private EditText lab_name;
    @ViewInject(R.id.lab_tel)
    private EditText lab_tel;
    @ViewInject(R.id.lab_mobile)
    private EditText lab_mobile;
    @ViewInject(R.id.btn_distribution)
    private Button btn_distribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("添加收货地址");
        address();

        btn_distribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetServerInfo();
            }
        });

    }

    public void GetServerInfo() {
        JSONObject para = new JSONObject();
        JSONObject sender = Default.GetSession();
        try {
            JSONObject filter = new JSONObject();
            filter.put("uid", sender.getString("uid"));
            filter.put("sid", sender.getString("sid"));
            para.put("session", filter);
            para.put("log_id", GetIntentData("id"));
            para.put("is_save", 1);
            para.put("province", id11);
            para.put("city", id12);
            para.put("district", id13);
            para.put("address", lab_address.getText());
            para.put("consignee", lab_name.getText());
            para.put("mobile", lab_mobile.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(DzpAddressActivity.this , "dazhuanpan/add_address", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                if (sender != null) {
                    try {
                        if (sender.getJSONObject("status").getInt("succeed")==0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(DzpAddressActivity.this);
                            builder.setMessage(sender.getJSONObject("status").getString("error_desc"));
                            builder.setTitle(Default.AppName);
                            builder.setPositiveButton("确定", null);
                            builder.create().show();
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DzpAddressActivity.this);
                            builder.setMessage("添加收货地址成功！");
                            builder.setTitle(Default.AppName);
                            builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                    AddActivity(RecordActivity.class);
                                    finish();
                                }
                            });
                            builder.create().show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

//    地址数据
    public void address() {
        //数据
        data_list = new ArrayList<String>();
        JSONObject para = new JSONObject();
        try {
            para.put("parent_id", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(DzpAddressActivity.this , "region", para, new AsynServer.BackObject() {
            @Override
            public void Back(final JSONObject sender)   {
                JSONObject json_status = JsonClass.jj(sender,"status");
                if (JsonClass.ij(json_status,"succeed") == 1) {
                    JSONObject data = JsonClass.jj(sender,"data");
                    JSONArray arr = JsonClass.jrrj(data,"regions");
                    for (int i = 0; i < arr.length(); i++) {
                        data_list.add(JsonClass.sj(JsonClass.jjrr(arr,i),"name"));
                    }
                    id1 = JsonClass.ij(JsonClass.jjrr(arr,0),"id");
                    //适配器
                    arr_adapter = new ArrayAdapter<String>(DzpAddressActivity.this, android.R.layout.simple_spinner_item, data_list);
                    //设置样式
                    arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //加载适配器
                    spinner.setAdapter(arr_adapter);
                } else {
                    MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //拿到被选择项的值
                str = (String) spinner.getSelectedItem();
                spinner.setTag(position);
                id = (int) spinner.getTag() + id1;
                id11= (int) id;
                //111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
                data_list1 = new ArrayList<String>();
                JSONObject para1 = new JSONObject();
                try {
                    para1.put("parent_id", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(DzpAddressActivity.this , "region", para1, new AsynServer.BackObject() {
                    @Override
                    public void Back(final JSONObject sender)  {
                        JSONObject json_status = JsonClass.jj(sender,"status");
                        if (JsonClass.ij(json_status,"succeed") == 1) {
                            JSONObject json_data = JsonClass.jj(sender,"data");
                            JSONArray arr = JsonClass.jrrj(json_data,"regions");
                            for (int i = 0; i < arr.length(); i++) {
                                data_list1.add(JsonClass.sj(JsonClass.jjrr(arr,i),"name"));
                                id2 = JsonClass.ij(JsonClass.jjrr(arr,i),"id");
                            }
                            //适配器
                            arr_adapter1 = new ArrayAdapter<String>(DzpAddressActivity.this, android.R.layout.simple_spinner_item, data_list1);
                            //设置样式
                            arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //加载适配器
                            spinner1.setAdapter(arr_adapter1);
                        } else {
                                MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                        }

                    }
                });

                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        //拿到被选择项的值
                        str = str + (String) spinner1.getSelectedItem();
                        spinner1.setTag(position);
                        id = (int) spinner1.getTag() + id2;
                        id12= (int) id;
                        ////2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222
                        data_list2 = new ArrayList<String>();
                        JSONObject para2 = new JSONObject();
                        try {
                            para2.put("parent_id", id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AsynServer.BackObject(DzpAddressActivity.this , "region", para2, new AsynServer.BackObject() {
                            @Override
                            public void Back(final JSONObject sender) {

                                JSONObject json_status = JsonClass.jj(sender,"status");
                                if (JsonClass.ij(json_status,"succeed") == 1) {
                                    JSONObject json_data = JsonClass.jj(sender,"data");
                                    JSONArray arr = JsonClass.jrrj(json_data,"regions");
                                    for (int i = 0; i < arr.length(); i++) {
                                        data_list2.add(JsonClass.sj(JsonClass.jjrr(arr,i),"name"));
                                        id3 = JsonClass.ij(JsonClass.jjrr(arr,i),"id");
                                    }
                                    //适配器
                                    arr_adapter2 = new ArrayAdapter<String>(DzpAddressActivity.this, android.R.layout.simple_spinner_item, data_list1);
                                    //设置样式
                                    arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //加载适配器
                                    spinner2.setAdapter(arr_adapter2);
                                } else {
                                    MessageBox.Show(JsonClass.sj(json_status,"error_desc"));
                                }
                            }
                        });

                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                //拿到被选择项的值
                                str = str + (String) spinner2.getSelectedItem();
                                spinner2.setTag(position);
                                id = (int) spinner2.getTag()+id3;
                                id13= (int) id;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

}
