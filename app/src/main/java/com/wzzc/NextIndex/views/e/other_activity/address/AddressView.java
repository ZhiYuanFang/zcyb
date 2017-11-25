package com.wzzc.NextIndex.views.e.other_activity.address;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.address.main_view.AddressItemView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/14.
 *
 */

public class AddressView extends BaseView {
    //region ```
    @ViewInject(R.id.list_address)
    ListView list_address;
    @ViewInject(R.id.layout_add_address)
    RelativeLayout layout_add_address;
    //endregion
    AddressDelegate ad;
    public AddressView(Context context) {
        super(context);
        init();
    }

    public AddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        layout_add_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.addAddress();
            }
        });
    }

    public void setInfo (AddressDelegate ad , JSONArray jrr) {
        this.ad = ad;
        MyAdapter myAdapter = new MyAdapter();
        list_address.setAdapter(myAdapter);
        ArrayList<JSONObject> data = new ArrayList<>();
        for (int i = 0 ; i < jrr.length() ; i ++) {
            try {
                data.add(jrr.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        myAdapter.addData(data);
        Default.fixListViewHeight(list_address);
    }

    class MyAdapter extends BaseAdapter {
        ArrayList<JSONObject> data;
        MyAdapter (){
            data = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
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
            if (convertView == null) {
                convertView = new AddressItemView(getContext());
            }
            ((AddressItemView)convertView).setInfo(ad,data.get(position));
            return convertView;
        }
    }
}
