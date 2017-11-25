package com.wzzc.index.home.h;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.h.main_view.NearViewPagerView;
import com.wzzc.index.home.h.main_view.NearbyView;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.new_index.SuperDeliver.SuperDeliverDelegate;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.BasicTitleView;
import com.wzzc.other_view.RegionView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/6.
 */

public class NewNearbyView extends BaseView implements NearbyDelegate, ListViewScrollDelegate {
    //region ```
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.basicTitleView)
    BasicTitleView basicTitleView;
    //endregion
    MyAdapter myAdapter;
    SuperDeliverDelegate superDeliverDelegate;
    LocationDelegate locationDelegate;
    String country_id, province_id, city_id, district_id, type;
    String keyWords;

    public NewNearbyView(Context context) {
        super(context);
        init();
    }

    public NewNearbyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        myAdapter = new MyAdapter(getContext());
        listView.setAdapter(myAdapter);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listView.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
            }
        });
        //region 定位
        basicTitleView.setTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RegionView regionView = new RegionView(getContext());
                regionView.setInfo();
                MessageBox.ShowFlexibleDialog("区域选择", regionView, new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index) {
                            case 0:
                                break;
                            case 1:
                                String[] regions = regionView.getRegionLocationId();
                                if (regions != null) {
                                    country_id = regions[0];
                                    province_id = regions[1];
                                    city_id = regions[2];
                                    district_id = regions[3];
                                    if (superDeliverDelegate != null) {
                                        superDeliverDelegate.showPlay(keyWords, country_id, province_id, city_id, district_id, type);
                                    }
                                    if (locationDelegate != null) {
                                        locationDelegate.showNear(keyWords, country_id, province_id, city_id, district_id, type);
                                    }
                                }
                                basicTitleView.setTitle(regionView.getRegionName());
                                break;
                            default:
                        }
                    }
                });
            }
        });
        //endregion
    }

    public NomalAdapter getAdapter() {
        return myAdapter;
    }

    public ListView getListView() {
        return listView;
    }

    public void setAddress(String address) {
        basicTitleView.setTitle(address);
    }


    public void setInfo(SuperDeliverDelegate superDeliverDelegate, JSONObject json) {
        this.superDeliverDelegate = superDeliverDelegate;
        callBack(json);
    }

    public void setInfo(LocationDelegate locationDelegate, JSONObject json) {
        this.locationDelegate = locationDelegate;
        callBack(json);
    }

    public void callBack(Object json_data) {
        ArrayList<Object> arrayList = new ArrayList<>();
        JSONObject data = (JSONObject) json_data;
        setAddress(JsonClass.sj(JsonClass.jj(data,"shop_info"),"district_name"));
        if (myAdapter.getCount() <= 0) {
            JSONArray jrr_cats = JsonClass.jrrj(data, "cats");
            String pagerArr = jrr_cats.toString();
            arrayList.add(pagerArr);
        }

        JSONArray jrr_shop = JsonClass.jrrj(data, "shops");
        for (int i = 0; i < jrr_shop.length(); i++) {
            arrayList.add(JsonClass.jjrr(jrr_shop, i).toString());
        }
        myAdapter.addData(arrayList);
    }

    @Override
    public void clickItem(String str_id, int selectPage) {
        type = str_id;
        this.selectPage = selectPage;
        if (superDeliverDelegate != null) {
            superDeliverDelegate.showPlay(keyWords, country_id, province_id, city_id, district_id, type);
        }
        if (locationDelegate != null) {
            locationDelegate.showNear(keyWords, country_id, province_id, city_id, district_id, type);
        }
    }

    @Override
    public void changeSearch(String str, boolean search) {
        keyWords = str;
        if (search) {
            if (superDeliverDelegate != null) {
                superDeliverDelegate.showPlay(keyWords, country_id, province_id, city_id, district_id, type);
            }
            if (locationDelegate != null) {
                locationDelegate.showNear(keyWords, country_id, province_id, city_id, district_id, type);
            }
        }
    }

    int selectPage;

    @Override
    public void dismissToastComponent() {

    }

    @Override
    public void showToastComponent() {

    }

    @Override
    public void scrollChanged(int state) {

    }

    private class MyAdapter extends NomalAdapter {
        private static final int PagerView = 1001;
        private static final int ShopView = 1002;

        public MyAdapter(Context c) {
            super(c);
        }

        @Override
        public void removeDataFromFirst() {
            super.removeDataFromFirst();
            clearData();
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                    return PagerView;
                default:
                    return ShopView;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case PagerView:
                    if (!(convertView instanceof NearViewPagerView)) {
                        convertView = new NearViewPagerView(c);
                        ((NearViewPagerView) convertView).nd = NewNearbyView.this;
                    }
                    try {
                        ((NearViewPagerView) convertView).setInfo(new JSONArray(String.valueOf(data.get(position))), type, selectPage, keyWords);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ShopView:
                    if (!(convertView instanceof NearbyView)) {
                        convertView = new NearbyView(c);
                    }
                    try {
                        ((NearbyView) convertView).setInfo(new JSONObject(String.valueOf(data.get(position))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
            }
            return convertView;
        }
    }

}
