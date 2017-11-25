package com.wzzc.other_view.fragment.address;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_view.WheelView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/15.
 */

public class AddressSelectFragmet extends Fragment implements View.OnClickListener{
    public static final String ASD = "asd";
    public static final String TableType = "table_type";
    public static final String AddressIDS = "addressIDS";
    public static final String AddressNameS = "addressNameS";
    AddressSelectDelegate asd;
    WheelView wv1,wv2,wv3;
    TextView tv_ok;
    RelativeLayout fraLayout;
    int type;//区域数据有两个,0新的区域表用于入住商或定位,1旧的区域表,用于收货地址的区域选择

    private JSONArray jrr_province ,jrr_city , jrr_district;
    private String province , city , district;
    private String provinceID , cityID , districtID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_select,null);
        wv1 = (WheelView) view.findViewById(R.id.wv1);
        wv2 = (WheelView) view.findViewById(R.id.wv2);
        wv3 = (WheelView) view.findViewById(R.id.wv3);
        tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        fraLayout = (RelativeLayout) view.findViewById(R.id.fraLayout);
        tv_ok.setOnClickListener(this);
        fraLayout.setOnClickListener(this);
        wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                System.out.println("``` wv1 index : " + selectedIndex + " item : " + item);
                province = item;
                try {
                    provinceID = jrr_province.getJSONObject(selectedIndex - 1).getString("region_id");
                    jrr_city = jrr_province.getJSONObject(selectedIndex - 1).getJSONArray("sub");
                    setWv(wv2,jrr_city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                System.out.println("``` wv2 index : " + selectedIndex + " item : " + item);

                city = item;
                try {
                    cityID = jrr_city.getJSONObject(selectedIndex - 1).getString("region_id");
                    jrr_district = jrr_city.getJSONObject(selectedIndex - 1).getJSONArray("sub");
                    setWv(wv3,jrr_district);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        wv3.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
            @Override
            public void onSelected(int selectedIndex, String item) {
                System.out.println("``` wv3 index : " + selectedIndex + " item : " + item);

                district = item;
                try {
                    districtID = jrr_district.getJSONObject(selectedIndex - 1).getString("region_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        asd = bundle.getParcelable(ASD);
        type = bundle.getInt(TableType);
        String[] addressIDs = bundle.getStringArray(AddressIDS);
        provinceID = addressIDs[0];
        cityID = addressIDs[1];
        districtID = addressIDs[2];
        String[] addressNames = bundle.getStringArray(AddressNameS);
        province = addressNames[0];
        city = addressNames[1];
        district = addressNames[2];
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getInfoFromService();
            }
        },1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ok:
                asd.selectFinish(new String[]{province,city,district},new String[]{provinceID , cityID , districtID});
                asd.dismissAddressFragment();
                break;
            case R.id.fraLayout:
                asd.dismissAddressFragment();
                break;
            default:
        }
    }

    /**
     * {
     "region_id": "2",
     "region_name": "北京",
     "region_type": "1",
     "sub_type": "2",
     "sub": [
     {
     "region_id": "52",
     "region_name": "北京",
     "region_type": "2",
     "sub_type": "3",
     "sub": [
     {
     "region_id": "500",
     "region_name": "东城区",
     "region_type": "3",
     "sub_type": "4"
     }]}]}

     "status": {
     "succeed": 1
     }
     */

    void getInfoFromService () {
        jrr_province = new JSONArray();
        JSONObject para = new JSONObject();
        try {
            para.put("table_type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(getActivity(), "region_all", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        jrr_province = sender.getJSONArray("data");
                        setWv(wv1,jrr_province);
                    }else {
                        Default.showToast(json_status.getString("error_desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setWv (WheelView wv , JSONArray jrr) {
        ArrayList<String> items = new ArrayList<>();
        int selectItem = 0;
        for (int i = 0 ; i < jrr.length() ; i ++) {
            try {
                items.add(jrr.getJSONObject(i).getString("region_name"));
                if (jrr.getJSONObject(i).getString("region_id").equals(provinceID) ||
                        jrr.getJSONObject(i).getString("region_id").equals(cityID) ||
                        jrr.getJSONObject(i).getString("region_id").equals(districtID)) {
                    selectItem = i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        wv.setItems(items);
        wv.setSeletion(selectItem);
        wv.startScrollerTask();
    }
}
