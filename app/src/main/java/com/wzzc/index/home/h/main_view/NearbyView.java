package com.wzzc.index.home.h.main_view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.index.home.h.main_view.main_view.ShopDetailsActivity;
import com.wzzc.index.home.h.main_view.item.NearOneView;
import com.wzzc.other_activity.web.WebActivity;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.other_function.CallPhone;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/11/15.
 */
public class NearbyView extends BaseView {

    private NearAdapter adapter;
    //region 组件
    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    @ViewInject(R.id.img_icon)
    private ExtendImageView img_view;
    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.lab_number)
    private TextView lab_number;
    @ViewInject(R.id.lab_distance)
    private TextView distance;
    @ViewInject(R.id.btn_shop)
    private LinearLayout btn_shop;
    @ViewInject(R.id.tv_call)
    private TextView tv_call;
    @ViewInject(R.id.tv_go)
    private TextView tv_go;
    @ViewInject(R.id.btn_take_photo)
    private LinearLayout btn_take_photo;
    @ViewInject(R.id.btn_address)
    private LinearLayout btn_address;
    //endregion
    int id;
    String phoneNumber;
    public NearbyView(Context context) {
        super(context);

        btn_take_photo.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        changeFocus(1,true);
                        break;
                    case MotionEvent.ACTION_UP:
                        changeFocus(1,false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        changeFocus(1,false);
                        break;
                }
                return false;
            }
        });

        btn_address.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        changeFocus(2,true);
                        break;
                    case MotionEvent.ACTION_UP:
                        changeFocus(2,false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        changeFocus(2,false);
                        break;
                }
                return false;
            }
        });
    }

    public void setInfo(JSONObject data) {
        adapter = new NearAdapter(GetBaseActivity());
        main_view.setAdapter(adapter);
        ArrayList<JSONObject> json = new ArrayList<JSONObject>();
        try {
            phoneNumber = data.getString("service_phone");
            img_view.radio = GetBaseActivity().getResources().getDimension(R.dimen.RoundRadio);
            img_view.setPath(data.getString("shop_logo"));
            lab_name.setText(data.getString("supplier_name"));
            lab_number.setText("共"+data.getString("goods_number")+"件宝贝");
            distance.setText(data.getString("shortest_path"));
            id=data.getInt("supplier_id");
            JSONArray arr = data.getJSONArray("goods_info");
            for (int i = 0; i < arr.length(); i++) {
                json.add(arr.getJSONObject(i));
        }
            adapter.addData(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


   private class NearAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;

        private NearAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }

        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            int count = this.data.size() / 4;
            if (this.data.size() % 4 == 1) {
                count++;
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NearOneView view;
            if (convertView != null) {
                view = (NearOneView) convertView;
            } else {
                view = new NearOneView(this.content);
            }
            JSONObject[] arr;
            int index = position * 4;
            if (index + 3 < this.data.size()) {
                arr = new JSONObject[]{this.data.get(index), this.data.get(index + 1),this.data.get(index+2),this.data.get(index+3)};
            } else {
                arr = new JSONObject[]{this.data.get(index)};
            }
            view.setInfo(arr);
            return view;
        }
    }


    @OnClick({R.id.btn_take_photo, R.id.btn_address, R.id.btn_shop})
    public void btn_bottom_click(LinearLayout view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag==0){
            CallPhone.call(phoneNumber);
        }else if (tag==1){
            Intent intent = new Intent();
            intent.putExtra("id","http://www.zcyb365.com/mobile/gps.php?id="+id);
            GetBaseActivity().AddActivity(WebActivity.class,0,intent);
        }else if (tag==2){
            if(id != 0){
                Intent intent=new Intent();
                intent.putExtra("id",id);
                GetBaseActivity().AddActivity(ShopDetailsActivity.class,0,intent);
            } else {
                Default.showToast("该商铺无法访问");
            }
        }
    }

    private void changeFocus (Integer index , boolean focus) {
        switch (index){
            case 1 :
                if (focus) {
                    tv_call.setTextColor(ContextCompat.getColor( getContext(), R.color.tv_Red));
                    tv_go.setTextColor(ContextCompat.getColor( getContext(), R.color.tv_Gray));
                } else {
                    tv_call.setTextColor(ContextCompat.getColor( getContext(), R.color.tv_Gray));
                    tv_go.setTextColor(ContextCompat.getColor( getContext(), R.color.tv_Gray));
                }
                break;
            case 2 :
                if (focus) {
                    tv_call.setTextColor(ContextCompat.getColor( getContext(), R.color.tv_Gray));
                    tv_go.setTextColor(ContextCompat.getColor( getContext(), R.color.tv_Red));
                } else {
                    tv_call.setTextColor(ContextCompat.getColor( getContext(), R.color.tv_Gray));
                    tv_go.setTextColor(ContextCompat.getColor( getContext(), R.color.tv_Gray));
                }
                break;
            default:
        }
    }
}
