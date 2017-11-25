package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendListView;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.invoice.main_view.InvoiceView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/11/25.
 *
 * 发货单列表
 */
public class InvoiceActivity extends BaseActivity {
    private CollectionAdapter adapter;
    //region 组件
    @ViewInject(R.id.main_view)
    private ExtendListView main_view;
    @ViewInject(R.id.lab_noshopping)
    private LinearLayout lab_noshopping;
    @ViewInject(R.id.et_input)
    private EditText et_input;
    @ViewInject(R.id.search)
    private SearchView search;
    //endregion

    public static final String ORDERSN = "order_sn";
    private boolean isloading = false;
    private int pageindex = 1;
    private int pagecount = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddBack();
        AddTitle("发货单列表");
        init();
        String order_sn = GetIntentData(ORDERSN) == null ? "" : (String) GetIntentData(ORDERSN);
        GetServerInfo(order_sn);
    }

    private void init () {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_input.getText().length() > 0) {
                    searchOrder(et_input.getText().toString());
                } else {
                    Default.showToast("请输入订单号", Toast.LENGTH_LONG);
                }
            }
        });

        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (et_input.getText().length() > 0) {
                        searchOrder(et_input.getText().toString());
                    } else {
                        Default.showToast("请输入订单号", Toast.LENGTH_LONG);
                    }
                }
                return false;
            }
        });
    }

    private void searchOrder (String order_sn) {
        GetServerInfo(order_sn);
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

    public void GetServerInfo(String order_sn) {
        adapter = new CollectionAdapter(this);
        main_view.setAdapter(adapter);
        if (isloading) {
            return;
        }
        isloading = true;
        JSONObject para = new JSONObject();
        try {
            para.put("order_sn",order_sn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(InvoiceActivity.this, "seller/delivery_list",false,main_view, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                try {
                    ArrayList<JSONObject> json = new ArrayList<JSONObject>();
                    JSONObject status=sender.getJSONObject("status");
                    if (status.getInt("succeed")==0){
                        error(status.getString("error_desc"));
                    }else {
                        JSONArray arr = sender.getJSONArray("data");
                        if (arr.length() == 0) {
                            lab_noshopping.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < arr.length(); i++) {
                                json.add(arr.getJSONObject(i));
                            }
                        }
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

            InvoiceView view;
            if (convertView != null) {
                view = (InvoiceView) convertView;
            } else {
                view = new InvoiceView(this.content);
            }
            int index = position;
            view.setInfo(this.data.get(index));
            return view;
        }
    }

}
