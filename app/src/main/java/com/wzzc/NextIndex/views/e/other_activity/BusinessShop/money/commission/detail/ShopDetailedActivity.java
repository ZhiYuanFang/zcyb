package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.detail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.detail.main_view.ShopDetailedOneView;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.money.commission.detail.main_view.ShopDetailedTwoView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2017/1/3.
 */
public class ShopDetailedActivity extends BaseActivity {
    //region 组件
    //功能切换按钮
    @ViewInject(R.id.btn_onlineorder)
    private Button btn_onlineorder;
    @ViewInject(R.id.btn_lineorder)
    private Button btn_lineorder;
    @ViewInject(R.id.btn_onlineorder1)
    private Button btn_onlineorder1;
    @ViewInject(R.id.btn_lineorder1)
    private Button btn_lineorder1;
    //功能切换界面
    @ViewInject(R.id.show_onlineorder)
    private LinearLayout show_onlineorder;
    @ViewInject(R.id.show_lineorder)
    private LinearLayout show_lineorder;
    @ViewInject(R.id.show_onlineorder1)
    private LinearLayout show_onlineorder1;
    @ViewInject(R.id.show_lineorder1)
    private LinearLayout show_lineorder1;
    //线上订单佣金
    private CollectionAdapter adapter;
    @ViewInject(R.id.main_view)
    private ExtendListView main_view1;
    @ViewInject(R.id.lab_noshopping)
    private LinearLayout lab_noshopping;
    //线上订单返利
    private CollectionAdapter adapter1;
    @ViewInject(R.id.main_view1)
    private ExtendListView main_view2;
    @ViewInject(R.id.lab_noshopping1)
    private LinearLayout lab_noshopping1;

    //线下门店支付佣金
    private CollectionAdapter1 adapter2;
    @ViewInject(R.id.main_view2)
    private ExtendListView main_view3;
    @ViewInject(R.id.lab_noshopping2)
    private LinearLayout lab_noshopping2;
    //    线下门店返利
    private CollectionAdapter1 adapter3;
    @ViewInject(R.id.main_view3)
    private ExtendListView main_view4;
    @ViewInject(R.id.lab_noshopping3)
    private LinearLayout lab_noshopping3;
    //endregion
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("商家佣金明细");
        btn_onlineorder.setBackgroundColor(Color.RED);
        btn_onlineorder.setTextColor(Color.WHITE);
        show_onlineorder.setVisibility(View.VISIBLE);
    }

    @Override
    protected void viewFirstLoad() {
        adapter = new CollectionAdapter(this);
        main_view1.setAdapter(adapter);

        adapter1 = new CollectionAdapter(this);
        main_view2.setAdapter(adapter1);

        adapter2 = new CollectionAdapter1(this);
        main_view3.setAdapter(adapter2);

        adapter3 = new CollectionAdapter1(this);
        main_view4.setAdapter(adapter3);

        GetServerInfo();



    }

    public void GetServerInfo() {
        JSONObject para = new JSONObject();
        try {
            para.put("type",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(ShopDetailedActivity.this , "seller/commision_rebate", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        error(status.getString("error_desc"));
                    }else {
                        GetServerInfo1();
                        JSONArray arr = sender.getJSONObject("data").getJSONArray("list");
                        if (arr.length() == 0) {
                            lab_noshopping.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        adapter.addData(json);
                        Default.fixListViewHeight(main_view1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void GetServerInfo1() {
        JSONObject para = new JSONObject();
        try {
            para.put("type",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(ShopDetailedActivity.this , "seller/commision_rebate", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        error(status.getString("error_desc"));
                    }else {
                        GetServerInfo2();
                        JSONArray arr = sender.getJSONObject("data").getJSONArray("list");
                        if (arr.length() == 0) {
                            lab_noshopping1.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        adapter1.addData(json);
                        Default.fixListViewHeight(main_view2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void GetServerInfo2() {
        JSONObject para = new JSONObject();
        try {
            para.put("type",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(ShopDetailedActivity.this , "seller/store_commision_rebate", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        error(status.getString("error_desc"));
                    }else {
                        GetServerInfo3();
                        JSONArray arr = sender.getJSONObject("data").getJSONArray("list");
                        if (arr.length() == 0) {
                            lab_noshopping2.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        adapter2.addData(json);
                        Default.fixListViewHeight(main_view3);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void GetServerInfo3() {
        JSONObject para = new JSONObject();
        try {
            para.put("type",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(ShopDetailedActivity.this , "seller/store_commision_rebate", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        error(status.getString("error_desc"));
                    }else {
                        JSONArray arr = sender.getJSONObject("data").getJSONArray("list");
                        if (arr.length() == 0) {
                            lab_noshopping3.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
                        adapter1.addData(json);
                        Default.fixListViewHeight(main_view4);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void error (String msg) {
        String[] strs = new String[1];
        strs[0] = "确定";
        MessageBox.Show(getString(R.string.app_name),msg,strs, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                finish();
            }
        });
    }

    //线上
    private class CollectionAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        private CollectionAdapter(Context context) {
            this.content = context;
            this.data = new ArrayList<>();
        }


        public void addData(ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.data.size();
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

            ShopDetailedOneView view;
            if (convertView != null) {
                view = (ShopDetailedOneView) convertView;
            } else {
                view = new ShopDetailedOneView
                        (this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

    //线下
    class CollectionAdapter1 extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public CollectionAdapter1(Context context) {
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

            ShopDetailedTwoView view;
            if (convertView != null) {
                view = (ShopDetailedTwoView) convertView;
            } else {
                view = new ShopDetailedTwoView
                        (this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }

    //功能切换
    @OnClick({R.id.btn_onlineorder, R.id.btn_lineorder, R.id.btn_onlineorder1, R.id.btn_lineorder1})
    public void lab_setting_click(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        if (tag == 0) {
            btn_onlineorder.setBackgroundColor(Color.RED);
            btn_onlineorder.setTextColor(Color.WHITE);
            show_onlineorder.setVisibility(View.VISIBLE);

            btn_lineorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_lineorder.setTextColor(Color.BLACK);
            show_lineorder.setVisibility(View.GONE);

            btn_onlineorder1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_onlineorder1.setTextColor(Color.BLACK);
            show_onlineorder1.setVisibility(View.GONE);

            btn_lineorder1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_lineorder1.setTextColor(Color.BLACK);
            show_lineorder1.setVisibility(View.GONE);
        } else if (tag == 1) {
            btn_lineorder.setBackgroundColor(Color.RED);
            btn_lineorder.setTextColor(Color.WHITE);
            show_lineorder.setVisibility(View.VISIBLE);

            btn_onlineorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_onlineorder.setTextColor(Color.BLACK);
            show_onlineorder.setVisibility(View.GONE);

            btn_onlineorder1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_onlineorder1.setTextColor(Color.BLACK);
            show_onlineorder1.setVisibility(View.GONE);

            btn_lineorder1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_lineorder1.setTextColor(Color.BLACK);
            show_lineorder1.setVisibility(View.GONE);

        } else if (tag == 2) {
            btn_onlineorder1.setBackgroundColor(Color.RED);
            btn_onlineorder1.setTextColor(Color.WHITE);
            show_onlineorder1.setVisibility(View.VISIBLE);

            btn_lineorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_lineorder.setTextColor(Color.BLACK);
            show_lineorder.setVisibility(View.GONE);

            btn_onlineorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_onlineorder.setTextColor(Color.BLACK);
            show_onlineorder.setVisibility(View.GONE);

            btn_lineorder1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_lineorder1.setTextColor(Color.BLACK);
            show_lineorder1.setVisibility(View.GONE);

        } else if (tag == 3) {
            btn_lineorder1.setBackgroundColor(Color.RED);
            btn_lineorder1.setTextColor(Color.WHITE);
            show_lineorder1.setVisibility(View.VISIBLE);

            btn_lineorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_lineorder.setTextColor(Color.BLACK);
            show_lineorder.setVisibility(View.GONE);

            btn_onlineorder1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_onlineorder1.setTextColor(Color.BLACK);
            show_onlineorder1.setVisibility(View.GONE);

            btn_onlineorder.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_collection));
            btn_onlineorder.setTextColor(Color.BLACK);
            show_onlineorder.setVisibility(View.GONE);

        }

    }

}
