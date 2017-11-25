package com.wzzc.NextIndex.views.e.other_activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.ShoppingCart.ConfirmOrder.a.AConfirmOrderActivity;
import com.wzzc.index.ShoppingCart.ConfirmOrder.b.BConfirmOrderActivity;
import com.wzzc.NextIndex.views.e.other_activity.address.main_view.AddressNoDataView;
import com.wzzc.NextIndex.views.e.other_activity.address.other_activity.AddressAddActivity;
import com.wzzc.NextIndex.views.e.other_activity.address.other_activity.AddressEditActivity;
import com.wzzc.NextIndex.views.e.other_activity.address.other_activity.AddressLookActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.FileInfo;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/14.
 */

public class AddressActivity extends ExtendBaseActivity implements AddressDelegate {
    public static final String TAG = "AddressActivity";
    public static final String SELECT = "select";/*是否为选择跳转到订单*/
    public static final String ORDERTYPE = "orderType";/*点击跳转到订单的类型 0 ：工厂币  1：子成币*/
    //region ```
    @ViewInject(R.id.addressView)
    AddressView addressView;
    @ViewInject(R.id.layout_contain)
    RelativeLayout layout_contain;
    //endregion
    AddressNoDataView andv;
    private boolean canSelect;
    private int orderType;
    @Override
    protected void init() {
        initBackTouch();
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(SELECT)) {
            canSelect = bundle.getBoolean(SELECT);
        }
        if (bundle.containsKey(ORDERTYPE)) {
            orderType = bundle.getInt(ORDERTYPE);
        }
        judgeFileHasDataAndInitializedData();
        andv = new AddressNoDataView(this,this);
        layout_contain.addView(andv);
    }

    @Override
    protected String getFileKey() {
        return TAG;
    }

    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                JSONArray data = (JSONArray) json_data;
                if (data.length() > 0) {
                    addressView.setVisibility(View.VISIBLE);
                    andv.setVisibility(View.GONE);
                    addressView.setInfo(AddressActivity.this, data);
                } else {
                    addressView.setVisibility(View.GONE);
                    andv.setVisibility(View.VISIBLE);
                }

            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        AsynServer.BackObject(this, "address/list", new JSONObject(), new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                FileInfo.SetUserString(getFileKey(), sender.toString(), AddressActivity.this);
                try {
                    initialized(sender, serverCallBack());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void deleteAddress(final String addressID) {
        JSONObject para = new JSONObject();
        try {
            para.put("address_id", addressID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "address/delete", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        Default.showToast("成功删除", Toast.LENGTH_LONG);
                        String str = FileInfo.GetUserString(getFileKey(), Default.getActivity());
                        JSONObject json_from_catch = new JSONObject(str);
                        JSONArray jrr_from_catch = json_from_catch.getJSONArray("data");
                        JSONArray jrr_new = new JSONArray();
                        for (int i = 0; i < jrr_from_catch.length(); i++) {
                            JSONObject json = jrr_from_catch.getJSONObject(i);
                            String address_id = json.getString("id");
                            if (!address_id.equals(addressID)) {
                                jrr_new.put(json);
                            }
                        }
                        JSONObject json_status_from_catch = json_from_catch.getJSONObject("status");
                        JSONObject json_new = new JSONObject();
                        json_new.put("data", jrr_new);
                        json_new.put("status", json_status_from_catch);
                        FileInfo.SetUserString(getFileKey(), json_new.toString(), Default.getActivity());
                        initialized(json_new, serverCallBack());
                    } else {
                        Default.showToast(json_status.getString("error_desc"), Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setDefaultAddress(final String addressID) {
        JSONObject para = new JSONObject();
        try {
            para.put("address_id", addressID);
            para.put("cancel", 0);//0设为默认,1取消默认
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "address/setDefault", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        Default.showToast("设置成功", Toast.LENGTH_LONG);
                        String str = FileInfo.GetUserString(getFileKey(), Default.getActivity());
                        JSONObject json_from_catch = new JSONObject(str);
                        JSONArray jrr_from_catch = json_from_catch.getJSONArray("data");
                        JSONArray jrr_new = new JSONArray();
                        for (int i = 0; i < jrr_from_catch.length(); i++) {
                            JSONObject json = jrr_from_catch.getJSONObject(i);
                            String address_id = json.getString("id");
                            if (!address_id.equals(addressID)) {
                                json.put("default_address", 0);
                            } else {
                                json.put("default_address", 1);
                            }
                            jrr_new.put(json);
                        }
                        JSONObject json_status_from_catch = json_from_catch.getJSONObject("status");
                        JSONObject json_new = new JSONObject();
                        json_new.put("data", jrr_new);
                        json_new.put("status", json_status_from_catch);
                        FileInfo.SetUserString(getFileKey(), json_new.toString(), Default.getActivity());
                        initialized(json_new, serverCallBack());
                    } else {
                        Default.showToast(json_status.getString("error_desc"), Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void editAddress(String addressID) {
        Intent intent = new Intent();
        intent.putExtra(AddressEditActivity.ADDRESS_ID, addressID);
        AddActivity(AddressEditActivity.class, 0, intent);
    }

    @Override
    public void lookAddress(String addressID) {
        System.out.println("``` lookAddress : " + addressID + " canSelect : " + canSelect);
        if (canSelect) {
            Intent intent = new Intent();
            intent.putExtra(AConfirmOrderActivity.ADDRESS, addressID);
            switch (orderType){
                case 0:
                    intent.setClass(this, AConfirmOrderActivity.class);
                    break;
                case 1:
                    intent.setClass(this, BConfirmOrderActivity.class);
                    break;
                default:
            }

            Default.toClass(this,intent);
        } else {
            Intent intent = new Intent();
            intent.putExtra(AddressLookActivity.ADDRESS_ID, addressID);
            AddActivity(AddressLookActivity.class, 0, intent);
        }
    }

    @Override
    public void addAddress() {
        AddActivity(AddressAddActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeNetConnectedAndSetInfoFromService(null);
    }
}
