package com.wzzc.NextSuperDeliver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextIndex.views.e.LoginDelegate;
import com.wzzc.NextSuperDeliver.list.Category;
import com.wzzc.NextSuperDeliver.list.SelectItemFragment;
import com.wzzc.NextSuperDeliver.list.SuperDeliverList;
import com.wzzc.NextSuperDeliver.main_view.a.SuperDeliverHome;
import com.wzzc.NextSuperDeliver.main_view.a.SuperDeliverHomeDelegate;
import com.wzzc.NextSuperDeliver.main_view.b.BrandSpecialView;
import com.wzzc.NextSuperDeliver.search.SuperDeliverSearchDelegate;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.new_index.SuperDeliver.main_view.childView.SelectDialogDelegate;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_view.progressDialog.CustomProgressDialog;
import com.wzzc.taobao.TBDetailActivity;
import com.wzzc.taobao.TaoBao;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/7.
 */
@ContentView(R.layout.super_deliver_activity)
public class SuperDeliver extends NewBaseActivity implements SuperDeliverSearchDelegate, SuperDeliverHomeDelegate, ListViewScrollDelegate
        , ClickDelegate, SelectDialogDelegate, LoginDelegate {
    private static final String A = "100";
    private static final String B = "101";
    private static final String C = "102";
    private static final String D = "103";
    @ViewInject(R.id.layout_contain_view)
    RelativeLayout layout_contain_view;

    //region bottom
    @ViewInject(R.id.lv_bottom_a)
    LinearLayout lv_bottom_a;
    @ViewInject(R.id.lv_bottom_b)
    LinearLayout lv_bottom_b;
    @ViewInject(R.id.lv_bottom_c)
    LinearLayout lv_bottom_c;
    @ViewInject(R.id.lv_bottom_d)
    LinearLayout lv_bottom_d;

    @ViewInject(R.id.iv_bottom_a)
    ImageView iv_bottom_a;
    @ViewInject(R.id.iv_bottom_b)
    ImageView iv_bottom_b;
    @ViewInject(R.id.iv_bottom_c)
    ImageView iv_bottom_c;
    @ViewInject(R.id.iv_bottom_d)
    ImageView iv_bottom_d;

    @ViewInject(R.id.tv_bottom_a)
    TextView tv_bottom_a;
    @ViewInject(R.id.tv_bottom_b)
    TextView tv_bottom_b;
    @ViewInject(R.id.tv_bottom_c)
    TextView tv_bottom_c;
    @ViewInject(R.id.tv_bottom_d)
    TextView tv_bottom_d;

    ArrayList<LinearLayout> bottomArrayList;
    ArrayList<ImageView> imgArrayList;
    ArrayList<TextView> tvArrayList;
    //endregion
    //region Views
    SuperDeliverHome superDeliverHome;
    BrandSpecialView brandSpecialView;
    ArrayList<BaseView> viewArrayList;//界面的集合
    //endregion
    int currentView;//当前点击界面
    String key;//存储关键字,为null时不进行存储功能
    View lastClickView;
    private static SuperDeliver superDeliver;

    @Override
    protected void init() {
        bottomArrayList = new ArrayList<LinearLayout>() {{
            add(lv_bottom_a);
            add(lv_bottom_b);
            add(lv_bottom_c);
            add(lv_bottom_d);
        }};

        for (LinearLayout layout : bottomArrayList) {
            layout.setOnClickListener(this);
        }

        imgArrayList = new ArrayList<ImageView>() {{
            add(iv_bottom_a);
            add(iv_bottom_b);
            add(iv_bottom_c);
            add(iv_bottom_d);
        }};

        tvArrayList = new ArrayList<TextView>() {{
            add(tv_bottom_a);
            add(tv_bottom_b);
            add(tv_bottom_c);
            add(tv_bottom_d);

        }};

        superDeliverHome = new SuperDeliverHome(this);
        brandSpecialView = new BrandSpecialView(this);
        viewArrayList = new ArrayList<BaseView>() {{
            add(superDeliverHome);
            add(brandSpecialView);
        }};

        for (BaseView baseView : viewArrayList) {
            baseView.setVisibility(View.GONE);
            layout_contain_view.addView(baseView);
        }

        superDeliver = this;

        lv_bottom_a.callOnClick();
    }

    @Override
    protected String getFileKey() {
        return key;
    }

    @Override
    protected CacheCallBack cacheCallBack() {
        return new CacheCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj, s);
            }
        };
    }

    @Override
    protected ServerCallBack serverCallBack() {
        return new ServerCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj, s);
            }
        };
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        switch (s) {
            case A:
                if (superDeliverHome.getListView() != null && superDeliverHome.getListView().getAdapter() != null && superDeliverHome.getListView().getAdapter().getClass().equals(NomalAdapter.class)) {
                    ((NomalAdapter) superDeliverHome.getListView().getAdapter()).clearData();
                }
                AsynServer.wantShowDialog = false;
                noKey();
                break;
            case B:
                /*if (brandSpecialView.getListView() != null && brandSpecialView.getListView().getAdapter() != null && brandSpecialView.getListView().getAdapter().getClass().equals(NomalAdapter.class)) {
                    ((NomalAdapter) brandSpecialView.getListView().getAdapter()).clearData();
                } else {
                    Default.showToast("bug");
                }*/
                brandSpecialView.getMyAdapter().clearData();
                AsynServer.wantShowDialog = false;
                noKey();
                break;
            case C:
                break;
            case D:
                break;
            default:
        }
    }

    private void cb(Object obj, String s) {
        switch (s) {
            case A: {
                JSONObject json_data = (JSONObject) obj;
                superDeliverHome.setInfo(this, this, this, json_data);
                break;
            }
            case B: {
                JSONObject json_data = (JSONObject) obj;
                JSONArray jrr_list = JsonClass.jrrj(json_data, "list");
                ArrayList<ShopBean> arrayList = new ArrayList<>();
                for (int i = 0; i < jrr_list.length(); i++) {
                    JSONObject json_brand = JsonClass.jjrr(jrr_list, i);
                    ShopBean shopBean = new ShopBean(JsonClass.sj(json_brand, "brand_id"),
                            JsonClass.sj(json_brand, "brand_name"),
                            JsonClass.sj(json_brand, "brand_logo"),
                            JsonClass.ij(json_brand, "left_day"),
                            JsonClass.sj(json_brand, "brand_desc"));
                    JSONArray jrr_goods_in_brand = JsonClass.jrrj(json_brand, "goods_list");
                    ArrayList<Production> brandProductions = new ArrayList<>();
                    for (int j = 0; j < jrr_goods_in_brand.length(); j++) {
                        JSONObject json_good = JsonClass.jjrr(jrr_goods_in_brand, j);
                        Production production = new Production(JsonClass.sj(json_good, "goods_id"),
                                JsonClass.sj(json_good, "goods_name"),
                                JsonClass.sj(json_good, "shop_price"),
                                JsonClass.sj(json_good, "is_new").equals("1"),
                                JsonClass.sj(json_good, "is_seckill").equals("1"),
                                JsonClass.sj(json_good, "is_best").equals("1"),
                                JsonClass.sj(json_good, "is_hot").equals("1"),
                                JsonClass.sj(json_good, "goods_thumb"),
                                JsonClass.sj(json_good, "goods_img"),
                                JsonClass.sj(json_good, "rebates"),
                                JsonClass.sj(json_good, "rebate_text"),
                                JsonClass.sj(json_good, "icon"),
                                JsonClass.sj(json_good, "num_iid"),
                                JsonClass.ij(json_good, "show_coupon") == 1,
                                JsonClass.sj(json_good, "coupon_info"),
                                JsonClass.sj(json_good, "coupon_click_url"),
                                JsonClass.sj(json_good, "footer_title"),
                                JsonClass.sj(json_good, "goods_from"),
                                JsonClass.ij(json_good, "left_day"),
                                JsonClass.sj(json_good, "coupon_price"));
                        brandProductions.add(production);
                    }
                    shopBean.setProductionArrayList(brandProductions);
                    shopBean.setTag(i);
                    arrayList.add(shopBean);
                }
                brandSpecialView.setInfo(arrayList);
                break;
            }
            case C:
                break;
            case D:
                break;
            default:
        }
    }

    @Override
    protected void publish() {
        switch (currentView) {
            case 0:
                lv_bottom_a.callOnClick();
                break;
            case 1:
                lv_bottom_b.callOnClick();
                break;
            case 2:
                lv_bottom_c.callOnClick();
                break;
            case 3:
                lv_bottom_d.callOnClick();
                break;
            default:
        }
    }

    private void userKey() {
        key = "SuperDeliver" + currentView;
    }

    private void noKey() {
        key = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lv_bottom_a:
                defaultAllBottom();
                focusBottom(lv_bottom_a);
                currentView = 0;
                userKey();
                showA();
                initDataFromCache(A);
                initDataFromServer(this, "SuperDiscount/index", !FileInfo.IsAtUserString(getFileKey(),this), new JSONObject(), superDeliverHome.getListView(), A);
                break;
            case R.id.lv_bottom_b:
                defaultAllBottom();
                focusBottom(lv_bottom_b);
                currentView = 1;
                showB();
                userKey();
                initDataFromCache(B);
                initDataFromServer(this, "SuperDiscount/brands", !FileInfo.IsAtUserString(getFileKey(),this), new JSONObject(), brandSpecialView.getListView(), B);
                break;
            case R.id.lv_bottom_c:{
//                defaultAllBottom();
//                focusBottom(lv_bottom_c);
//                currentView = 2;
//                showC();
                Intent intent = new Intent();
                Category category= new Category();
                category.setpType(3);
                intent.putExtra(SuperDeliverList.CATEGORY,category);
                AddActivity(SuperDeliverList.class,0,intent);
                break;}
            case R.id.lv_bottom_d:{
//                defaultAllBottom();
//                focusBottom(lv_bottom_d);
//                currentView = 3;
//                showD();
                Intent intent = new Intent();
                Category category= new Category();
                category.setpType(2);
                intent.putExtra(SuperDeliverList.CATEGORY,category);
                AddActivity(SuperDeliverList.class,0,intent);
                break;}
            default:
        }
    }

    //region Views
    private void showA() {
        allGone();
        superDeliverHome.setVisibility(View.VISIBLE);
    }

    private void showB() {
        allGone();
        brandSpecialView.setVisibility(View.VISIBLE);
    }

    private void showC() {
        allGone();
    }

    private void showD() {
        allGone();
    }

    private void allGone() {
        for (BaseView baseView : viewArrayList) {
            baseView.setVisibility(View.GONE);
        }
    }
    //endregion

    //region Action-Bottom
    private void defaultAllBottom() {
        for (ImageView img : imgArrayList) {
            switch (Integer.valueOf(img.getTag().toString())) {
                case 1:
                    img.setBackground(ContextCompat.getDrawable(this, R.drawable.index_gray));
                    break;
                case 2:
                    img.setBackground(ContextCompat.getDrawable(this, R.drawable.super_brand_gray));
                    break;
                case 3:
                    img.setBackground(ContextCompat.getDrawable(this, R.drawable.super_high_back_gray));
                    break;
                case 4:
                    img.setBackground(ContextCompat.getDrawable(this, R.drawable.super_kill_gray));
                    break;
                default:
            }
        }

        for (TextView tv : tvArrayList) {
            tv.setTextColor(ContextCompat.getColor(this, R.color.tv_Gray));
        }
    }

    private void focusBottom(LinearLayout layout) {
        switch (Integer.valueOf(layout.getTag().toString())) {
            case 1:
                imgArrayList.get(0).setBackground(ContextCompat.getDrawable(this, R.drawable.index_focus));
                tvArrayList.get(0).setTextColor(ContextCompat.getColor(this, R.color.tv_Red));
                break;
            case 2:
                imgArrayList.get(1).setBackground(ContextCompat.getDrawable(this, R.drawable.super_brand_focus));
                tvArrayList.get(1).setTextColor(ContextCompat.getColor(this, R.color.tv_Red));
                break;
            case 3:
                imgArrayList.get(2).setBackground(ContextCompat.getDrawable(this, R.drawable.super_high_back_focus));
                tvArrayList.get(2).setTextColor(ContextCompat.getColor(this, R.color.tv_Red));
                break;
            case 4:
                imgArrayList.get(3).setBackground(ContextCompat.getDrawable(this, R.drawable.super_kill_focus));
                tvArrayList.get(3).setTextColor(ContextCompat.getColor(this, R.color.tv_Red));
                break;
            default:
        }
    }

    //endregion

    //region SuperDeliverSearchDelegate
    @Override
    public void search(String keyWords) {
        Category category = new Category();
        category.setKeyWords(keyWords);
        search(category);
    }
    //endregion

    //region SuperDeliverHomeDelegate
    @Override
    public void search(Category category) {
        Intent intent = new Intent();
        intent.putExtra(SuperDeliverList.CATEGORY, category);
        Default.getActivity().AddActivity(SuperDeliverList.class, 0, intent);
    }

    @Override
    public void showSelectFragment(ArrayList<Category> categories) {
        hasFragment = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SelectItemFragment selectItemFragment = new SelectItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SelectItemFragment.SELECT_ITEM_DATA, categories);
        bundle.putParcelable(SelectItemFragment.SDD, this);
        selectItemFragment.setArguments(bundle);
        fragmentTransaction.setCustomAnimations(R.anim.push_top_in, R.anim.push_top_out, R.anim.push_top_in, R.anim.push_top_out);
        fragmentTransaction.replace(R.id.contain_select_fragment, selectItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void getCoupons(Production production) {
        goCoupon(production.getCoupon_click_url(), production.getFooter_title(), production.getGoods_id());
    }

    CustomProgressDialog customProgressDialog;//打开淘宝客户端时对话框

    private void goCoupon(final String url, String info, String goods_id) {
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
                        intent.putExtra(TBDetailActivity.GoodsName, goods_name);
                        intent.putExtra(TBDetailActivity.Item, url);
                        intent.putExtra(TBDetailActivity.Type, 1);
                        AddActivity(TBDetailActivity.class, 0, intent);
                    } else {
                        customProgressDialog = CustomProgressDialog.createDialog(SuperDeliver.this, R.style.CustomProgressDialog);
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
                    if (error_code == 100) {//您尚未登陆
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

    //region ListViewScrollDelegate
    @Override
    public void dismissToastComponent() {

    }

    @Override
    public void showToastComponent() {

    }

    @Override
    public void scrollChanged(int state) {

    }
    //endregion

    //region ClickDelegate
    @Override
    public void shouldLogin(View clickView) {
        lastClickView = clickView;
        MessageBox.Show(this, Default.AppName, "您尚未登陆", new String[]{"先逛逛", "立即登陆"}, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent.putExtra(LoginActivity.LoginDelegate, SuperDeliver.this);
                        AddActivity(LoginActivity.class, 0, intent);
                        break;
                    default:
                }
            }
        });
    }
    //endregion

    //region SelectDialogDelegate
    @Override
    public void clickOK(Category category) {
        Intent intent = new Intent();
        intent.putExtra(SuperDeliverList.CATEGORY, category);
        AddActivity(SuperDeliverList.class, 0, intent);
    }

    @Override
    public void clickCancel() {
        outFragment();
    }

    //endregion

    //region LoginDelegate
    @Override
    public void loginOK() {
        if (lastClickView != null) {
            lastClickView.callOnClick();
        }
    }
    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator CREATOR = new ClassLoaderCreator<SuperDeliver>() {
        @Override
        public SuperDeliver createFromParcel(Parcel source) {
            return superDeliver;
        }

        @Override
        public SuperDeliver[] newArray(int size) {
            return new SuperDeliver[0];
        }

        @Override
        public SuperDeliver createFromParcel(Parcel source, ClassLoader loader) {
            return superDeliver;
        }
    };

}
