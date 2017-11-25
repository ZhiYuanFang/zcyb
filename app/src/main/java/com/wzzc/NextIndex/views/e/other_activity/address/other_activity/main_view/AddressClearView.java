package com.wzzc.NextIndex.views.e.other_activity.address.other_activity.main_view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_view.fragment.address.AddressSelectDelegate;
import com.wzzc.other_view.fragment.address.AddressSelectFragmet;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/15.
 *
 */

public class AddressClearView extends BaseView implements AddressSelectDelegate{
    public static final int EDIT = 1 , LOOK = 2 , ADD = 3;
    //region ```
    @ViewInject(R.id.et_name)
    EditText et_name;
    @ViewInject(R.id.et_phone)
    EditText et_phone;
    @ViewInject(R.id.et_code)
    EditText et_code;
    @ViewInject(R.id.tv_address)
    TextView tv_address;
    @ViewInject(R.id.edit_address)
    RelativeLayout edit_address;
    @ViewInject(R.id.et_address_info)
    EditText et_address_info;
    @ViewInject(R.id.edit_deliver_time)
    RelativeLayout edit_deliver_time;
    @ViewInject(R.id.tv_deliver_time)
    TextView tv_deliver_time;
    @ViewInject(R.id.tv_submit)
    TextView tv_submit;
    //endregion
    int type;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String address_id;
    String[] addressIDs;
    String[] addressNames;
    int default_address;
    public AddressClearView(Context context) {
        super(context);
        init();
    }

    public AddressClearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        fragmentManager = Default.getActivity().getSupportFragmentManager();

        tv_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case EDIT:
                        submitEditInfo();
                        break;
                    case ADD :
                        submitAddInfo();
                        break;
                    default:
                }
            }
        });
        
        edit_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                AddressSelectFragmet asf = new AddressSelectFragmet();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AddressSelectFragmet.ASD , AddressClearView.this);
                bundle.putInt(AddressSelectFragmet.TableType,1);
                bundle.putStringArray(AddressSelectFragmet.AddressIDS,addressIDs);
                bundle.putStringArray(AddressSelectFragmet.AddressNameS,addressNames);
                asf.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.push_bottom_in,R.anim.push_bottom_out,R.anim.push_bottom_in,R.anim.push_bottom_out);
                fragmentTransaction.replace(R.id.contaim_fragment,asf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        edit_deliver_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeliverMenu(v);
            }
        });
    }

    public void setInfo (JSONObject json , int type) {
        this.type = type;
        addressIDs = new String[3];
        addressNames = new String[3];
        if (json != null) {
            address_id = sj(json,"id");
            et_name.setText(sj(json,"consignee"));
            et_phone.setText(sj(json,"mobile"));
            et_code.setText(sj(json,"zipcode"));
            et_address_info.setText(sj(json,"address"));
            addressIDs[0] = sj(json,"province");
            addressIDs[1] = sj(json,"city");
            addressIDs[2] = sj(json,"district");
            addressNames[0] = sj(json,"province_name");
            addressNames[1] = sj(json,"city_name");
            addressNames[2] = sj(json,"district_name");

            StringBuilder sder = new StringBuilder();
            sder.append(sj(json,"province_name")).append("/").append(sj(json,"city_name")).append("/").append(sj(json,"district_name"));
            tv_address.setText(sder);
            tv_deliver_time.setText(sj(json,"best_time"));
            try {
                default_address = json.getInt("default_address");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        switch (type){
            case EDIT :
                setAllCanEdit();
                tv_submit.setText("确认修改");
                break;
            case LOOK:
                setAllNotEdit();
                tv_submit.setVisibility(GONE);
                break;
            case ADD :
                setAllCanEdit();
                tv_submit.setText("确认添加");
                break;
            default:
        }
    }
    //region server
    public void submitEditInfo () {
        JSONObject para = new JSONObject();
        try {
            para.put("address_id",address_id);
            JSONObject address = new JSONObject();
            address.put("name",et_name.getText());
            address.put("country",1);
            address.put("province",addressIDs[0]);
            address.put("city",addressIDs[1]);
            address.put("district",addressIDs[2]);
            address.put("mobile",et_phone.getText());
            address.put("address",et_address_info.getText());
            address.put("best_time",tv_deliver_time.getText());
            address.put("default_address",default_address);
            para.put("address",address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(getContext(), "address/update", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        Default.showToast("修改成功");
                        GetBaseActivity().onBackPressed();
                    } else {
                        Default.showToast(json_status.getString("error_desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void submitAddInfo () {
        JSONObject para = new JSONObject();
        try {JSONObject address = new JSONObject();
            address.put("name",et_name.getText());
            address.put("country",1);
            address.put("province",addressIDs[0]);
            address.put("city",addressIDs[1]);
            address.put("district",addressIDs[2]);
            address.put("mobile",et_phone.getText());
            address.put("address",et_address_info.getText());
            address.put("best_time",tv_deliver_time.getText());
            address.put("default_address",default_address);
            para.put("address",address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(getContext(), "address/add", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    JSONObject json_status = sender.getJSONObject("status");
                    int succeed = json_status.getInt("succeed");
                    if (succeed == 1) {
                        Default.showToast("添加成功");
                        GetBaseActivity().onBackPressed();
                    } else {
                        Default.showToast(json_status.getString("error_desc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //endregion

    //region Helper
    private void setAllCanEdit () {
        et_name.setFocusable(true);
        et_phone.setFocusable(true);
        et_code.setFocusable(true);
        et_address_info.setFocusable(true);
        et_name.setFocusableInTouchMode(true);
        et_phone.setFocusableInTouchMode(true);
        et_code.setFocusableInTouchMode(true);
        et_address_info.setFocusableInTouchMode(true);
        edit_address.setClickable(true);
        edit_deliver_time.setClickable(true);
    }

    private void setAllNotEdit () {
        et_name.setFocusable(false);
        et_phone.setFocusable(false);
        et_code.setFocusable(false);
        et_address_info.setFocusable(false);

        et_name.setFocusableInTouchMode(false);
        et_phone.setFocusableInTouchMode(false);
        et_code.setFocusableInTouchMode(false);
        et_address_info.setFocusableInTouchMode(false);
        edit_address.setClickable(false);
        edit_deliver_time.setClickable(false);
    }
    //endregion

    private String sj (JSONObject json , String str_id) {
        try {
            return json.getString(str_id);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    //region menu
    private void showDeliverMenu (View view) {
        PopupMenu popupMenu;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            popupMenu = new PopupMenu(getContext(),view, Gravity.CENTER);
        } else {
            popupMenu = new PopupMenu(getContext(),view);
        }
        popupMenu.inflate(R.menu.deliver_time_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String deliver_time = item.getTitle().toString();
                tv_deliver_time.setText(deliver_time);
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void selectFinish(String[] address, String[] addressID) {
        addressIDs = addressID;
        StringBuilder sder = new StringBuilder();
        for (int i = 0 ; i < address.length ; i ++) {
            sder.append(address[i]);
            if (i < address.length - 1) {
                sder.append("/");
            }
        }
        tv_address.setText(sder);
    }

    @Override
    public void dismissAddressFragment() {
        fragmentManager.popBackStack();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    //endregion
}
