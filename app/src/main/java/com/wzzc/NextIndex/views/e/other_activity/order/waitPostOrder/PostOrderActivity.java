package com.wzzc.NextIndex.views.e.other_activity.order.waitPostOrder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextIndex.views.e.other_activity.order.main_view.businiess.main.main_view.OrderView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/10/24.
 */
public class PostOrderActivity extends BaseActivity {
    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    private boolean isloading = false;
    PostOrderAdapter adapter;
    @ViewInject(R.id.lab_noshopping)
    private LinearLayout lab_noshopping;
    private int pageindex = 1;
    private int pagecount = 10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("待付款");

    }

    @Override
    protected void viewFirstLoad() {
        adapter = new PostOrderAdapter(this);
        main_view.setAdapter(adapter);
        GetServerInfo();
    }

    public void GetServerInfo() {
        if (isloading) {
            return;
        }
        isloading = true;

        JSONObject para = new JSONObject();
        JSONObject sender = Default.GetSession();
        try {
            JSONObject filter = new JSONObject();
            filter.put("uid", sender.getString("uid"));
            filter.put("sid", sender.getString("sid"));
            para.put("session", filter);
            para.put("type", "await_pay");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(PostOrderActivity.this , "order/list", false,main_view,para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        AddActivity(LoginActivity.class);
                    }else {
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

                            params1.height = (Default.dip2px(RelativeLayout.LayoutParams.MATCH_PARENT, getBaseContext()));
                            main_view.setLayoutParams(params1);

                        adapter.addData(json);
                        pageindex++;
                        isloading = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    class PostOrderAdapter extends BaseAdapter {
        private ArrayList<JSONObject> data;
        private Context content;


        public PostOrderAdapter(Context context) {
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

            OrderView view;
            if (convertView != null) {
                view = (OrderView) convertView;
            } else {
                view = new OrderView(this.content);
            }
            JSONObject[] arr1;
            int index = position;
            arr1 = new JSONObject[]{this.data.get(index)};
            view.setInfo(arr1);
            return view;
        }
    }
}
