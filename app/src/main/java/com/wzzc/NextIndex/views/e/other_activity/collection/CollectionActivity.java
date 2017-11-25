package com.wzzc.NextIndex.views.e.other_activity.collection;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextIndex.views.e.other_activity.collection.main_view.production.CollectionShopView;
import com.wzzc.NextIndex.views.e.other_activity.collection.main_view.bussinessShop.CollectionView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/10/24.
 */
public class CollectionActivity extends BaseActivity {

    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    @ViewInject(R.id.main_view_shop)
    private ExtendListView main_view_shop;
    @ViewInject(R.id.lab_noshopping)
    private LinearLayout lab_noshopping;
    @ViewInject(R.id.layout_like_production)
    LinearLayout layout_like_production;
    @ViewInject(R.id.layout_like_shop)
    LinearLayout layout_like_shop;
    @ViewInject(R.id.tv_like_production)
    TextView tv_like_production;
    @ViewInject(R.id.tv_like_shop)
    TextView tv_like_shop;
    @ViewInject(R.id.layout_production)
    LinearLayout layout_production;
    @ViewInject(R.id.layout_shop)
    LinearLayout layout_shop;
//    TextView footertextView;
    private boolean isloading = false;
    private int pageindex = 1;
    private int pagecount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("我的收藏");

//        footertextView = new TextView(this);
//        footertextView.setText("正在加载...");
//        footertextView.setTextSize(16);
//        footertextView.setGravity(Gravity.CENTER);
//        footertextView.setTextColor(Color.parseColor("#999999"));
//        main_view.addFooterView(footertextView);
//        AbsListView.LayoutParams para = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Default.dip2px(44, this));
//        footertextView.setLayoutParams(para);

    }

    @Override
    protected void viewFirstLoad() {
        CollectionAdapter adapter = new CollectionAdapter(this);
        main_view.setAdapter(adapter);
        GetServerInfo(adapter);
    }

    public void GetServerInfo(final CollectionAdapter adapter) {
        if (isloading) {
            return;
        }
        isloading = true;
        JSONObject para = new JSONObject();
        AsynServer.BackObject(CollectionActivity.this, "user/collect/list",false,main_view, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status = sender.getJSONObject("status");
                    if (status.getInt("succeed") == 0) {
                        AddActivity(LoginActivity.class);
                    } else {
                        JSONArray arr = sender.getJSONArray("data");
                        if (arr.length() == 0) {
                            lab_noshopping.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) main_view.getLayoutParams();
                        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
                        int height = wm.getDefaultDisplay().getHeight();
                        if (arr.length() < height / 108) {
                            params1.height = (Default.dip2px((arr.length() * 108), getBaseContext()));
                            main_view.setLayoutParams(params1);
                        } else {
                            params1.height = (Default.dip2px(RelativeLayout.LayoutParams.MATCH_PARENT, getBaseContext()));
                            main_view.setLayoutParams(params1);
                        }
                        adapter.addData(json);
//                        if (AsynServer.CheckPageIsEnd(sender)) {
//                            main_view.removeFooterView(footertextView);
//                        }
                        pageindex++;
                        isloading = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getServerInfoShop (final CollectionShopAdapter shopAdapter) {
        if (isloading) {
            return;
        }
        isloading = true;
        JSONObject para = new JSONObject();
        AsynServer.BackObject(CollectionActivity.this, "user/collect/store_list", false,main_view,para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status = sender.getJSONObject("status");
                    if (status.getInt("succeed") == 0) {
                        AddActivity(LoginActivity.class);
                    } else {
                        JSONArray arr = sender.getJSONArray("data");
                        if (arr.length() == 0) {
                            lab_noshopping.setVisibility(View.VISIBLE);

                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) main_view.getLayoutParams();
                        WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
                        int height = wm.getDefaultDisplay().getHeight();
                        if (arr.length() < height / 108) {
                            params1.height = (Default.dip2px((arr.length() * 108), getBaseContext()));
                            main_view.setLayoutParams(params1);
                        } else {
                            params1.height = (Default.dip2px(RelativeLayout.LayoutParams.MATCH_PARENT, getBaseContext()));
                            main_view.setLayoutParams(params1);
                        }
                        shopAdapter.addData(json);
//                        if (AsynServer.CheckPageIsEnd(sender)) {
//                            main_view.removeFooterView(footertextView);
//                        }
                        pageindex++;
                        isloading = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class CollectionAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public CollectionAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            int count = this.data.size();
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

            CollectionView view;
            if (convertView != null) {
                view = (CollectionView) convertView;
            } else {
                view = new CollectionView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

    class CollectionShopAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public CollectionShopAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            int count = this.data.size();
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

            CollectionShopView view;
            if (convertView != null) {
                view = (CollectionShopView) convertView;
            } else {
                view = new CollectionShopView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

    @OnClick({R.id.layout_like_production, R.id.layout_like_shop})
    public void click_like(View view) {
        Integer tag = Integer.parseInt(view.getTag().toString());
        pageindex = 1;
        switch (tag) {
            case 0:
                layout_like_production.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_hasOK));
                tv_like_production.setTextColor(ContextCompat.getColor(this, R.color.tv_White));
                layout_like_shop.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                tv_like_shop.setTextColor(ContextCompat.getColor(this, R.color.tv_Black));
                layout_production.setVisibility(View.VISIBLE);
                layout_shop.setVisibility(View.GONE);
                CollectionAdapter adapter = new CollectionAdapter(this);
                main_view.setAdapter(adapter);
                GetServerInfo(adapter);
                break;
            case 1:
                layout_like_production.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                tv_like_production.setTextColor(ContextCompat.getColor(this, R.color.tv_Black));
                layout_like_shop.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_hasOK));
                tv_like_shop.setTextColor(ContextCompat.getColor(this, R.color.tv_White));
                layout_production.setVisibility(View.GONE);
                layout_shop.setVisibility(View.VISIBLE);
                CollectionShopAdapter shopAdapter = new CollectionShopAdapter(this);
                main_view_shop.setAdapter(shopAdapter);
                getServerInfoShop(shopAdapter);
                break;
        }
    }
}
