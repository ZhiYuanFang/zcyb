package com.wzzc.index.home.h.main_view.main_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.base.ExtendBaseActivity;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.h.main_view.main_view.main_view.ProfileFragment;
import com.wzzc.index.home.h.main_view.main_view.main_view.a.ShopDetailsView;
import com.wzzc.index.home.h.main_view.main_view.main_view.b.ShopDetailPromationView;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.SlideView.SlideDelegate;
import com.wzzc.other_view.SlideView.SlideView;
import com.wzzc.index.home.h.main_view.main_view.main_view.c.NearbyShopView;
import com.wzzc.onePurchase.view.AnotherItemLayoutView;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.CallPhone;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.NoDataView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2016/11/18.
 * <p>
 * 商铺详情
 */
public class ShopDetailsActivity extends ExtendBaseActivity implements SlideDelegate {
    private static final String TAG = "ShopDetailsActivity";
    public static final String SELLER_ID = "id";
    //region 组件
    @ViewInject(R.id.scroll)
    ScrollView scroll;
    @ViewInject(R.id.layout_home)
    private RelativeLayout layout_home;
    @ViewInject(R.id.user_names)
    private TextView user_names;
    @ViewInject(R.id.tv_distance)
    private TextView tv_distance;
    @ViewInject(R.id.ratingBar)
    private RatingBar ratingBar;
    @ViewInject(R.id.lv_promote)
    private ListView lv_promote;
    @ViewInject(R.id.lv_other)
    private ListView lv_other;
    @ViewInject(R.id.sdpv)
    private ShopDetailPromationView sdpv;
    @ViewInject(R.id.tv_num)
    private TextView tv_num;
    @ViewInject(R.id.tv_local)
    private TextView tv_local;
    @ViewInject(R.id.slideView)
    private SlideView slideView;
    @ViewInject(R.id.ibn_call)
    private ImageButton ibn_call;
    @ViewInject(R.id.tv_shop_other)
    private TextView tv_shop_other;
    @ViewInject(R.id.layout_promate)
    LinearLayout layout_promate;
    @ViewInject(R.id.ailv_promote)
    private AnotherItemLayoutView ailv_promote;
    @ViewInject(R.id.ailv_profile)
    private AnotherItemLayoutView ailv_profile;
    @ViewInject(R.id.ailv_nearby)
    private AnotherItemLayoutView ailv_nearby;
    @ViewInject(R.id.nearby_shop)
    private ListView nearby_shop;
    @ViewInject(R.id.rv_player)
    private RelativeLayout rv_player;
    @ViewInject(R.id.rv_promote)
    private RelativeLayout rv_promote;
    @ViewInject(R.id.rv_profile)
    private RelativeLayout rv_profile;
    @ViewInject(R.id.rv_nearBy)
    private RelativeLayout rv_nearBy;
    @ViewInject(R.id.iv_collec)
    private ImageView iv_collec;
    @ViewInject(R.id.lv_bottom)
    private RelativeLayout lv_bottom;
    @ViewInject(R.id.tv_collect)
    TextView tv_collect;
    @ViewInject(R.id.btn_share)
    ImageView btn_share;
    //endregion
    boolean hasCollection;
    AlphaAnimation alphaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void init() {
        initBackTouch();
        AddBack();
        sellerID = String.valueOf(GetIntentData(SELLER_ID));
        ailv_promote.setNotUseTouchListener(true);
        ailv_nearby.setNotUseTouchListener(true);
        ailv_profile.setNotUseTouchListener(true);
        iv_collec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasCollection = !hasCollection;
                Drawable drawable = hasCollection ? ContextCompat.getDrawable(ShopDetailsActivity.this, R.drawable.c_has) : ContextCompat.getDrawable(ShopDetailsActivity.this, R.drawable.c_no);
                iv_collec.setBackground(drawable);
                collect();
                int col = Integer.parseInt(tv_collect.getText().toString());
                col += hasCollection ? 1 : -1;
                tv_collect.setText(String.valueOf(col));
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Default.showToast(getString(R.string.notDevelop), Toast.LENGTH_LONG);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (oldScrollY < rv_player.getLayoutParams().height) {
                        lv_bottom.setAlpha(0);
                        if (alphaAnimation != null) {
                            alphaAnimation.reset();
                            lv_bottom.clearAnimation();
                        }
                        alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(500);
                        lv_bottom.startAnimation(alphaAnimation);
                    } else {
                        lv_bottom.setAlpha(1);
                        if (alphaAnimation != null) {
                            alphaAnimation.reset();
                            lv_bottom.clearAnimation();
                        }
                        alphaAnimation = new AlphaAnimation(0, 1);
                        alphaAnimation.setDuration(500);
                        lv_bottom.startAnimation(alphaAnimation);
                    }
                }
            });
        }
        super.init();
    }

    @Override
    protected String getFileKey() {
        return TAG + sellerID;
    }

    @Override
    protected ExtendBaseView.ServerCallBack serverCallBack() {
        return new ExtendBaseView.ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                final JSONObject sender = (JSONObject) json_data;

                final JSONObject seller_info = JsonClass.jj(sender, "seller_info");
                //region info
                hasCollection = JsonClass.boolj(seller_info, "is_guanzhu");
                Drawable drawable = hasCollection ? ContextCompat.getDrawable(ShopDetailsActivity.this, R.drawable.c_has) : ContextCompat.getDrawable(ShopDetailsActivity.this, R.drawable.c_no);
                iv_collec.setBackground(drawable);
                user_names.setText(JsonClass.sj(seller_info, "shopname"));
                tv_distance.setText(JsonClass.sj(seller_info, "shortest_path"));
                ratingBar.setSelected(false);
                Object obj_step = JsonClass.oj(seller_info, "pingfen");
                Float step = Float.valueOf(String.valueOf(obj_step));
                ratingBar.setRating(step);
                tv_num.setText(step + "分");
                tv_local.setText(JsonClass.sj(seller_info, "address"));
                //endregion

                //region 查看更多
                int num = JsonClass.ij(seller_info, "goods_list_more_count");
                if (num > 0) {
                    tv_shop_other.setVisibility(View.VISIBLE);
                    tv_shop_other.setText("查看其它" + num + "个");
                } else {
                    tv_shop_other.setVisibility(View.GONE);
                }
                tv_shop_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_shop_other.setVisibility(View.GONE);
                        lv_other.setVisibility(View.VISIBLE);
                        try {
                            CreateShopeDetailsView(seller_info.getJSONArray("goods_list_more"), ShopDetailsView.DetailB);
                            Default.fixListViewHeight(lv_other);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //endregion

                //region 商家介绍
                CreateShopeDetailsView(JsonClass.jrrj(seller_info, "goods_list"), ShopDetailsView.DetailA);
                Default.fixListViewHeight(lv_promote);
                //endregion

                //region 电话
                final String callPhone = JsonClass.sj(seller_info, "service_phone");
                ibn_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callPhone == null || callPhone.equals("")) {
                            MessageBox.Show("该商家未提供电话");
                        } else {
                            CallPhone.call(callPhone);
                        }
                    }
                });
                //endregion

                //region 商家介绍
                ailv_promote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hasFragment = true;
                        FragmentManager fragmentManager = ShopDetailsActivity.this.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ProfileFragment profileFragment = new ProfileFragment();
                        Bundle bundle = new Bundle();
                        try {
                            bundle.putString(ProfileFragment.SHOPNAME, seller_info.getString("shopname"));
                            bundle.putString(ProfileFragment.NUMBER, seller_info.getString("goodsnum"));
                            bundle.putString(ProfileFragment.DISTANCE, seller_info.getString("shortest_path"));
                            bundle.putString(ProfileFragment.PROFILE, seller_info.getString("shop_notice"));
                            bundle.putString(ProfileFragment.SHOPICON, seller_info.getString("shoplogo"));
                            bundle.putString(ProfileFragment.INFOICON, seller_info.getString("zhizhao"));
                            bundle.putString(ProfileFragment.STARTDATE, seller_info.getString("createtime"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        profileFragment.setArguments(bundle);
                        fragmentTransaction.setCustomAnimations(R.anim.zoom_load_in, R.anim.zoom_load_out, R.anim.zoom_load_in, R.anim.zoom_load_out);
                        fragmentTransaction.add(R.id.layout_contain, profileFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                //endregion

                //region 幻灯片
                CreateFirstView(JsonClass.jrrj(seller_info, "player"));
                //endregion

                //region 商家推荐
                CreateShopDetailViewBestGoods(JsonClass.jrrj(seller_info, "best_goods"));
                //endregion

                //region 附件商铺
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CreateNearbyShopView(sender.getJSONArray("nearby_shops"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Default.fixListViewHeight(nearby_shop);
                    }
                }, 1);
                //endregion
            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        JSONObject para = new JSONObject();
        try {
            para.put("seller_id", sellerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(ShopDetailsActivity.this, "store/index", !hsf, para, new AsynServer.BackObject() {
            @Override
            public void Back(final JSONObject sender) {
                FileInfo.SetUserString(getFileKey(), sender.toString(), ShopDetailsActivity.this);
                try {
                    initialized(sender, serverCallBack());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    String sellerID;

    private void collect() {
        JSONObject para = new JSONObject();
        try {
            para.put("seller_id", sellerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(this, "store/collect", false, para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                JSONObject json_status = JsonClass.jj(sender, "status");
                int succeed = JsonClass.ij(json_status, "succeed");
                if (succeed == 1) {
                    Default.showToast("店铺收藏成功");
                } else {
                    MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                }
            }
        });
    }

    public void CreateFirstView(JSONArray sender) {
        if (sender.length() > 0) {

            slideView.setInfo(this, sender);

        } else {
            rv_player.removeAllViews();
            rv_player.addView(new NoDataView(this));
        }
    }

    public void CreateShopeDetailsView(JSONArray sender, String detailType) {
        if (sender.length() > 0) {
            switch (detailType) {
                case ShopDetailsView.DetailA:
                    lv_promote.setAdapter(new AdapterDetail(this, sender, detailType));
                    break;
                case ShopDetailsView.DetailB:
                    lv_other.setAdapter(new AdapterDetail(this, sender, detailType));
                    break;
                default:
            }
        } else {
            layout_promate.removeAllViews();
            ViewGroup.LayoutParams lp = layout_promate.getLayoutParams();
            lp.height = Default.dip2px(90, this);
            layout_promate.setLayoutParams(lp);
            layout_promate.addView(new NoDataView(this));
        }
    }

    public void CreateShopDetailViewBestGoods(JSONArray sender) {
        if (sender.length() > 0) {
            sdpv.setInfo(sender);
        } else {
            rv_profile.removeAllViews();
            ViewGroup.LayoutParams lp = rv_profile.getLayoutParams();
            lp.height = Default.dip2px(90, this);
            rv_profile.setLayoutParams(lp);
            rv_profile.addView(new NoDataView(this));
        }
    }

    public void CreateNearbyShopView(JSONArray sender) {
        if (sender.length() > 0) {
            nearby_shop.setAdapter(new AdapterNearByShop(this, sender));
        } else {
            rv_nearBy.removeAllViews();
            ViewGroup.LayoutParams lp = rv_nearBy.getLayoutParams();
            lp.height = Default.dip2px(90, this);
            rv_nearBy.setLayoutParams(lp);
            rv_nearBy.addView(new NoDataView(this));
        }
    }

    @Override
    public void clickItem(Integer clickPosition, View clickView, JSONObject json_item) {
        if (!ItemClick.switchNormalListener(JsonClass.sj(json_item, "data_type"), JsonClass.sj(json_item, "ad_link"))) {
            ItemClick.judgeSpecialListener(null, clickView, JsonClass.sj(json_item, "data_type"), JsonClass.sj(json_item, "ad_link"), JsonClass.sj(json_item, "num_iid"));
        }
    }

    //region Adapter
    private class AdapterDetail extends BaseAdapter {
        JSONArray jrr;
        Context c;
        String detailType;

        private AdapterDetail(Context c, JSONArray jrr, String detailType) {
            this.c = c;
            this.jrr = jrr;
            this.detailType = detailType;
        }

        @Override
        public int getCount() {
            return jrr.length();
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
                convertView = new ShopDetailsView(c);
            }
            try {
                ((ShopDetailsView) convertView).setInfo(jrr.getJSONObject(position), detailType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    private class AdapterNearByShop extends BaseAdapter {
        JSONArray jrr;
        private Context c;

        private AdapterNearByShop(Context c, JSONArray jrr) {
            this.jrr = jrr;
            this.c = c;
        }

        @Override
        public int getCount() {
            return jrr.length();
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
                convertView = new NearbyShopView(c);
            }
            try {
                ((NearbyShopView) convertView).setInfo(jrr.getJSONObject(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    //endregion
}
