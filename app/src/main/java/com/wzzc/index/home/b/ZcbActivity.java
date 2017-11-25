package com.wzzc.index.home.b;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.index.ShoppingCart.ShoppingCartActivity;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.Default;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.a.main_view.Pro2View;
import com.wzzc.index.home.b.main_view.TopZcbView;
import com.wzzc.other_view.production.list.main_view.BrowseProductionView;
import com.wzzc.other_view.production.list.main_view.Browse_ProductionView;
import com.wzzc.other_function.FileInfo;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/5/5.
 *
 */

public class ZcbActivity extends ExtendBaseActivity implements ZcbDelegate , ListViewScrollDelegate {
    private static final String TAG = "ZcbActivity";
    //region ```
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.ib_go_top)
    private ImageButton ib_go_top;
    @ViewInject(R.id.ib_go_cart)
    private ImageButton ib_go_cart;
    //endregion
    MyAdapter myAdapter;
    String type = BrowseProductionView.SORT_BY_NEW_PRODUCTIONS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        AddBack();
    }
    @Override
    protected void viewFirstLoad() {
        super.viewFirstLoad();
        AsynServer.listViewScrollDelegate = this;
    }
    protected void init () {
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        ib_go_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.scrollToListviewTop(listView);
            }
        });

        ib_go_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    Integer cart_type = ShoppingCartActivity.EXCHAGE_CART;
                    Intent intent = new Intent();
                    intent.putExtra(ShoppingCartActivity.TYPE, cart_type);
                    AddActivity(ShoppingCartActivity.class, 0, intent);
                } else {
                    MessageBox.Show(ZcbActivity.this, Default.AppName, "前往个人中心登陆！", new String[]{"取消", "登陆"}, new MessageBox.MessBtnBack() {
                        @Override
                        public void Back(int index) {
                            switch (index) {
                                case 0:
                                    break;
                                case 1:
                                    AddActivity(LoginActivity.class);
                                    break;
                                default:
                            }
                        }
                    });
                }

            }
        });

        super.init();
    }

    @Override
    protected String getFileKey() {
        return TAG;
    }

    ArrayList<JSONObject> adsData;
    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                final JSONObject sender = (JSONObject) json_data;
                adsData = new ArrayList<JSONObject>(){{
                    add(sender);
                }};
                myAdapter.clearData();
                myAdapter.addData(adsData);
                if (Default.isConnect(ZcbActivity.this)) {
                    getProductionInfo(BrowseProductionView.URL_ZCB,BrowseProductionView.SORT_BY_NEW_PRODUCTIONS,"");
                }
            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        AsynServer.BackObject(this, "exchange/index", !hsf,new JSONObject(), new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                FileInfo.SetUserString(getFileKey(), sender.toString(), ZcbActivity.this);
                if (!hsf) {
                    try {
                        initialized(sender, serverCallBack());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void getProductionInfo (String url , String sort_by , String keyWords) {
        if (url != null && sort_by != null) {
            JSONObject para = new JSONObject();
            try {
                JSONObject filter = new JSONObject();
                filter.put("keywords", keyWords);
                filter.put("sort_by", sort_by);
                para.put("filter", filter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            notKill = false;
            AsynServer.BackObject(this, url, true,listView, para, new AsynServer.BackObject() {
                @Override
                public void Back(JSONObject sender) {
                    if (!notKill) {
                        myAdapter.killDataFromSecond();
//                        AsynServer.page = 2;
                    }
                    notKill = true;
                    try {
                        JSONArray jrr_data = sender.getJSONArray("data");
                        ArrayList<JSONObject> data = new ArrayList<>();
                        for (int i = 0 ; i < jrr_data.length() ; i ++) {
                            data.add(jrr_data.getJSONObject(i));
                        }
                        myAdapter.addData(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.wantShowDialog = false;
                }
            });
        }
    }
    boolean notKill;

    @Override
    public void search(String keyValue, String typeValue) {
        type = typeValue;
        getProductionInfo(BrowseProductionView.URL_ZCB,typeValue,keyValue);
    }

    @Override
    public void dismissToastComponent() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.update_animator_small);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ib_go_top.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ib_go_top.startAnimation(animation);
    }

    @Override
    public void showToastComponent() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.update_animator_recory);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ib_go_top.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ib_go_top.startAnimation(animation);
    }

    @Override
    public void scrollChanged(int state) {
        switch (state){
            case 1 :{
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.update_animator_small);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ib_go_cart.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ib_go_cart.startAnimation(animation);
                break;}
            default:
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.update_animator_recory);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ib_go_cart.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ib_go_cart.startAnimation(animation);
        }
    }


    private class MyAdapter extends BaseAdapter {
        ArrayList<JSONObject> data ;
        ArrayList<View> parent;
        static final int TOP = 1000;
        static final int PRO_1 = 1001;
        static final int PRO_2 = 1002;

        private MyAdapter () {
            data = new ArrayList<>();
            parent = new ArrayList<>();
        }

        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void clearData () {
            data = new ArrayList<>();
        }

        private void killDataFromSecond () {
            data = new ArrayList<>();
            data.addAll(adsData);
        }
        @Override
        public int getCount() {
            int proCount = data.size() - 1 ;
            int otherNum = proCount%2;
            int proLineCount = proCount/2 + otherNum ;
            return proLineCount + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TOP;
            } else {
                if ((data.size() - position * 2 + 1) == 1) {
                    return PRO_1;
                } else return PRO_2;
            }
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
        public View getView(int position, View convertView, ViewGroup p) {
            switch (getItemViewType(position)){
                case TOP:
                    if (convertView == null || ! (convertView instanceof TopZcbView)) {
                        convertView = new TopZcbView(ZcbActivity.this);
                    }
                    ((TopZcbView)convertView).zd = ZcbActivity.this;
                    ((TopZcbView)convertView).setInfo(data.get(position),type);
                    break;
                case PRO_1:
                    if (convertView == null || ! (convertView instanceof Browse_ProductionView)) {
                        convertView = new Browse_ProductionView(ZcbActivity.this);
                    }
                    //region set
                    ((Browse_ProductionView)convertView).setTv_nowPriceColor(ContextCompat.getColor(ZcbActivity.this, R.color.gold));
                    ((Browse_ProductionView)convertView).setProductionNameColor(ContextCompat.getColor(ZcbActivity.this, R.color.tv_White));
                    ((Browse_ProductionView)convertView).setTv_nowPriceBackground(ContextCompat.getDrawable(ZcbActivity.this, R.drawable.bg_gold_arc));
                    ((Browse_ProductionView)convertView).setBackGroundColor(ContextCompat.getColor(ZcbActivity.this, R.color.bg_hasOK));
                    //endregion
                    ((Browse_ProductionView)convertView).setInfo(data.get(position),true);
                    break;
                case PRO_2:
                    if (convertView == null || ! (convertView instanceof Pro2View)) {
                        convertView = new Pro2View(ZcbActivity.this);
                    }
                    ((Pro2View)convertView).setInfo(data.get(position*2 - 1),data.get(position * 2),true);
                    break;
                default:
            }
            return convertView;
        }


    }
}
