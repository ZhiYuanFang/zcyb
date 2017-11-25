package com.wzzc.NextSuperDeliver.main_view.a_b;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextIndex.views.e.LoginDelegate;
import com.wzzc.NextIndex.views.e.User;
import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.NextSuperDeliver.ShopBean;
import com.wzzc.NextSuperDeliver.main_view.a.BrandViewDelegate;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.NoDataView;
import com.wzzc.other_view.progressDialog.CustomProgressDialog;
import com.wzzc.taobao.TBDetailActivity;
import com.wzzc.taobao.TaoBao;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/23.
 *
 */
@ContentView(R.layout.activity_brand)
public class BrandActivity extends NewBaseActivity implements BrandViewDelegate , BrandProductionDelegate , ClickDelegate ,
        LoginDelegate{
    public static final String SHOPBEAN = "shopBean";
    ShopBean shopBean;
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.tv_brand)
    TextView tv_brand;
    NomalAdapter myAdapter;

    View curClickView;
    private static BrandActivity brandActivity;
    @Override
    protected void init() {
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        shopBean  = getIntent().getParcelableExtra(SHOPBEAN);
        tv_brand.setText(shopBean.getBrand_name());
        initDataFromCache(null);
        JSONObject para = new JSONObject();
        try {
            para.put("brand_id",shopBean.getBrand_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(null,"SuperDiscount/brand",!FileInfo.IsAtUserString(getFileKey(),this),para,null,null);
        brandActivity = this;
    }

    @Override
    protected String getFileKey() {
        return "BrandActivity" + shopBean.getBrand_id();
    }

    @Override
    protected CacheCallBack cacheCallBack() {
        return new CacheCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj,s);
            }
        };
    }

    @Override
    protected ServerCallBack serverCallBack() {
        return new ServerCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj,s);
            }
        };
    }

    private void cb (Object obj, String s) {
        JSONObject json_data = (JSONObject) obj;
        JSONObject json_brand_info = JsonClass.jj(json_data,"brand_info");
        JSONArray jrr_list = JsonClass.jrrj(json_data,"list");
        JSONArray jrr_brand_list = JsonClass.jrrj(json_data,"brand_list");
        ArrayList<Object> arrayList = new ArrayList<>();
        if (JsonClass.sj(json_brand_info,"banner") != null && JsonClass.sj(json_brand_info,"banner").length() > 0) {
            arrayList.add(json_brand_info);
        }
        for (int i = 0 ; i < jrr_list.length() ; i ++) {
            JSONObject json = JsonClass.jjrr(jrr_list,i);
            Production production = new Production(JsonClass.sj(json,"goods_id"),
                    JsonClass.sj(json,"goods_name"),
                    JsonClass.sj(json,"shop_price"),
                    JsonClass.sj(json,"is_new").equals("1"),
                    JsonClass.sj(json,"is_seckill").equals("1"),
                    JsonClass.sj(json,"is_best").equals("1"),
                    JsonClass.sj(json,"is_hot").equals("1"),
                    JsonClass.sj(json,"goods_thumb"),
                    JsonClass.sj(json,"goods_img"),
                    JsonClass.sj(json,"rebates"),
                    JsonClass.sj(json,"rebate_text"),
                    JsonClass.sj(json,"icon"),
                    JsonClass.sj(json,"num_iid"),
                    JsonClass.ij(json,"show_coupon") == 1,
                    JsonClass.sj(json,"coupon_info"),
                    JsonClass.sj(json,"coupon_click_url"),
                    JsonClass.sj(json,"footer_title"),
                    JsonClass.sj(json,"goods_from"),
                    JsonClass.ij(json,"left_day"),
                    JsonClass.sj(json,"coupon_price")
            );
            arrayList.add(production);
        }
        for (int i = 0 ; i < jrr_brand_list.length() ; i ++) {
            JSONObject json_brand = JsonClass.jjrr(jrr_brand_list,i);
            ShopBean shopBean = new ShopBean(JsonClass.sj(json_brand,"brand_id"),
                    JsonClass.sj(json_brand,"brand_name"),
                    JsonClass.sj(json_brand,"brand_logo"),
                    JsonClass.ij(json_brand,"left_day"));
            JSONArray jrr_goods_in_brand = JsonClass.jrrj(json_brand,"goods_list");
            ArrayList<Production> brandProductions = new ArrayList<>();
            for (int j = 0 ; j < jrr_goods_in_brand.length() ; j ++) {
                JSONObject json_good = JsonClass.jjrr(jrr_goods_in_brand,j);
                Production production = new Production(JsonClass.sj(json_good, "goods_id"),
                        JsonClass.sj(json_good, "goods_name"),
                        JsonClass.sj(json_good, "shop_price"),
                        JsonClass.sj(json_good, "is_new").equals("1"),
                        JsonClass.sj(json_good, "is_seckill").equals("1"),
                        JsonClass.sj(json_good, "is_best").equals("1"),
                        JsonClass.sj(json_good, "is_hot").equals("1"),
                        JsonClass.sj(json_good, "goods_thumb"),
                        JsonClass.sj(json_good,"goods_img"),
                        JsonClass.sj(json_good,"rebates"),
                        JsonClass.sj(json_good,"rebate_text"),
                        JsonClass.sj(json_good,"icon"),
                        JsonClass.sj(json_good,"num_iid"),
                        JsonClass.ij(json_good,"show_coupon") == 1,
                        JsonClass.sj(json_good,"coupon_info"),
                        JsonClass.sj(json_good,"coupon_click_url"),
                        JsonClass.sj(json_good,"footer_title"),
                        JsonClass.sj(json_good,"goods_from"),
                        JsonClass.ij(json_good,"left_day"),
                        JsonClass.sj(json_good,"coupon_price"));
                brandProductions.add(production);
            }
            shopBean.setProductionArrayList(brandProductions);
            shopBean.setTag(i);
            arrayList.add(shopBean);
        }
        arrayList.add(getString(R.string.noMore));
        myAdapter.addData(arrayList);
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        myAdapter.clearData();
    }

    @Override
    protected void publish() {
        JSONObject para = new JSONObject();
        try {
            para.put("brand_id",shopBean.getBrand_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(null,"SuperDiscount/brand",true,para,null,null);
    }

    @Override
    public void onClick(View v) {

    }

    //region BrandViewDelegate
    @Override
    public void chooseShop(ShopBean shopBean) {
        Intent intent = new Intent();
        intent.putExtra(BrandActivity.SHOPBEAN,shopBean);
        AddActivity(BrandActivity.class,0,intent);
    }
    //endregion
    
    //region BrandProductionDelegate
    @Override
    public void choose(final Production production) {
        if (!User.is_activate() && User.isShowActivity(this) && User.getActivity_msg().length() > 0) {
            MessageBox.Show(this, Default.AppName, User.getActivity_msg(), new String[]{User.getActivity_btn_goActivity(), User.getActivity_check_text(),
                    User.getActivity_btn_justBuy()}, new MessageBox.MessBtnBack() {
                @Override
                public void Back(int index) {
                    switch (index) {
                        case 0:
                            ItemClick.switchNormalListener(ItemClick.SuperDiscount_list_3, null);
                            break;
                        case 1:
                            User.setShowActivity(BrandActivity.this, false);
                            break;
                        case 2:
                            if (production.isShow_coupon()) {
                                goCoupon (production.getCoupon_click_url(), production.getCoupon_info(), production.getGoods_id());
                            } else {
                                ItemClick.judgeSpecialListener(BrandActivity.this,null,ItemClick.SuperDiscount_goods,production.getGoods_id(),production.getNum_iid());
                            }
                            break;
                        default:
                    }
                }
            });
        } else {
            if (production.isShow_coupon()) {
                goCoupon (production.getCoupon_click_url(), production.getCoupon_info(), production.getGoods_id());
            } else {
                ItemClick.judgeSpecialListener(BrandActivity.this,null,ItemClick.SuperDiscount_goods,production.getGoods_id(),production.getNum_iid());
            }
        }
    }
    //endregion

    //region 优惠券
    CustomProgressDialog customProgressDialog;//打开淘宝客户端时对话框
    private void goCoupon (final String url, String info, String goods_id) {
        JSONObject para = new JSONObject();
        try {
            para.put("id", goods_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(Default.getActivity(), "SuperDiscount/buy", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject object) {
                JSONObject json_status = JsonClass.jj(object, "status");
                int succeed = JsonClass.ij(json_status, "succeed");
                if (succeed == 1) {
                    if (!TaoBao.hasInstallation()) {
                        JSONObject sender = JsonClass.jj(object, "data");
                        String goods_name = JsonClass.sj(sender, "goods_name");
//                    String goods_from = JsonClass.sj(sender,"goods_from");
                        Intent intent = new Intent();
                        intent.putExtra(TBDetailActivity.GoodsName,goods_name);
                        intent.putExtra(TBDetailActivity.Item,url);
                        intent.putExtra(TBDetailActivity.Type,1);
                        AddActivity(TBDetailActivity.class,0,intent);
                    } else {
                        customProgressDialog = CustomProgressDialog.createDialog(BrandActivity.this,R.style.CustomProgressDialog);
                        customProgressDialog.setMessage("正在打开淘宝客户端");
                        customProgressDialog.show();
                        TaoBao.showPage(url, new AlibcTradeCallback() {
                            @Override
                            public void onTradeSuccess(TradeResult tradeResult) {
                                Default.showToast("成功打开淘宝");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Default.showToast(s);
                            }
                        });
                    }

                } else {
                    int error_code = JsonClass.ij(json_status, "error_code");
                    if (error_code == 100 ) {//您尚未登陆
                        shouldLogin(null);
                    } else
                        MessageBox.Show(JsonClass.sj(json_status, "error_desc"));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
    }
    //endregion

    //region ClickDelegate
    @Override
    public void shouldLogin(View clickView) {
        curClickView = clickView;
        MessageBox.Show(this,Default.AppName,"您尚未登陆",new String[]{"先逛逛","立即登陆"}, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                switch (index){
                    case 0:
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.putExtra(LoginActivity.LoginDelegate,BrandActivity.this);
                        AddActivity(LoginActivity.class,0,intent);
                        break;
                    default:
                }
            }
        });
    }
    //endregion

    //region LoginDelegate
    @Override
    public void loginOK() {
        if (curClickView != null) {
            curClickView.callOnClick();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator CREATOR = new Creator<BrandActivity>() {
        @Override
        public BrandActivity createFromParcel(Parcel source) {
            return brandActivity;
        }

        @Override
        public BrandActivity[] newArray(int size) {
            return new BrandActivity[0];
        }
    };
    //endregion

    private class MyAdapter extends NomalAdapter {
        private static final int BANNER = 0;
        private static final int BrandProduction = 1;
        private static final int SHOP = 2;
        private static final int STRING = 3;
        public MyAdapter(Context c) {
            super(c);
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position).getClass().equals(JSONObject.class)) {
                return BANNER;
            } else if (getItem(position).getClass().equals(Production.class)) {
                return BrandProduction;
            } else if (getItem(position).getClass().equals(ShopBean.class)) {
                return SHOP;
            } else if (getItem(position).getClass().equals(String.class)) {
                return STRING;
            } else
                return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)){
                case BANNER:
                    if (!(convertView instanceof BannerView)) {
                        convertView = new BannerView(c);
                    }
                    ((BannerView)convertView).setInfo((JSONObject) getItem(position));
                    break;
                case BrandProduction:
                    if (!(convertView instanceof BrandProductionView)) {
                        convertView = new BrandProductionView(c);
                    }
                    ((BrandProductionView)convertView).setInfo(BrandActivity.this,(Production) getItem(position));
                    break;
                case SHOP:
                    if (!(convertView instanceof OtherBrandView)) {
                        convertView = new OtherBrandView(c);
                    }
                    ((OtherBrandView)convertView).setInfo(BrandActivity.this,(ShopBean) getItem(position),(int)((ShopBean) getItem(position)).getTag() == 0);
                    break;
                case STRING:
                    if (!(convertView instanceof NoMore)) {
                        convertView = new NoMore(c);
                    }
                    ((NoMore)convertView).setInfo((String) getItem(position));
                    break;
                default:
                    convertView = new NoDataView(c);
            }
            return convertView;
        }
    }
}
