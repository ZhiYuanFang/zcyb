package com.wzzc.NextSuperDeliver.main_view.a;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.NextSuperDeliver.ShopBean;
import com.wzzc.NextSuperDeliver.list.Category;
import com.wzzc.NextSuperDeliver.main_view.a_b.BrandActivity;
import com.wzzc.NextSuperDeliver.search.SuperDeliverSearch;
import com.wzzc.NextSuperDeliver.search.SuperDeliverSearchDelegate;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.new_index.SuperDeliver.main_view.childView.SelectItemView;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.ClickBean;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.NoDataView;
import com.wzzc.other_view.SlideView.SlideDelegate;
import com.wzzc.other_view.SlideView.SlideView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/21.
 * 超值送首页
 */
@ContentView(R.layout.super_deliver_home_layout)
public class SuperDeliverHome extends BaseView implements View.OnClickListener , SlideDelegate ,SuperKillItemDelegate , BrandViewDelegate , DoubleGoodsDelegate{
    public SuperDeliverHomeDelegate superDeliverHomeDelegate;
    public ClickDelegate clickDelegate;
    public SuperDeliverSearchDelegate superDeliverSearchDelegate;
    //region title
    @ViewInject(R.id.btn_back)
    RelativeLayout layout_back;
    @ViewInject(R.id.layout_search)
    RelativeLayout layout_search;
    //endregion
    //region 分类
    @ViewInject(R.id.siv)
    SelectItemView siv;
    @ViewInject(R.id.contain_select_layout)
    LinearLayout contain_select_layout;
    @ViewInject(R.id.lv_more)
    RelativeLayout lv_more;

    ArrayList<SelectItemView> arrListSelectItem;//分类集合
    //endregion
    @ViewInject(R.id.listView)
    ListView listView;

    NomalAdapter myAdapter;
    ArrayList<Category> categoryArrayList;//分类集合
    private ShopBean shopBean;

    public SuperDeliverHome(Context context) {
        super(context);
        init();
    }

    public SuperDeliverHome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        layout_back.setOnClickListener(this);
        layout_search.setOnClickListener(this);
        myAdapter = new MyAdapter(getContext());
        listView.setAdapter(myAdapter);
        lv_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                superDeliverHomeDelegate.showSelectFragment(categoryArrayList);
            }
        });
    }

    public ListView getListView() {
        return listView;
    }

    public void setInfo(SuperDeliverHomeDelegate superDeliverHomeDelegate, ClickDelegate clickDelegate,SuperDeliverSearchDelegate superDeliverSearchDelegate, JSONObject jsonObject) {
        this.superDeliverHomeDelegate = superDeliverHomeDelegate;
        this.clickDelegate = clickDelegate;
        this.superDeliverSearchDelegate = superDeliverSearchDelegate;
        if (jsonObject.has("cat_list")) {
            JSONArray jrrCats = JsonClass.jrrj(jsonObject,"cat_list");
            categoryArrayList = new ArrayList<>();
            for (int i = 0 ; i < jrrCats.length() ; i ++) {
                JSONObject jsonCat = JsonClass.jjrr(jrrCats,i);
                Category category = new Category(JsonClass.sj(jsonCat,"cat_id"),
                        JsonClass.sj(jsonCat,"cat_name"),
                        JsonClass.sj(jsonCat,"parent_id"),
                        null,
                        null,
                        null,
                        null,
                        0,
                        null,
                        null,
                        null,
                        0);
                categoryArrayList.add(category);
            }
            initSelectItemLayout(categoryArrayList,"");
        }
        if (myAdapter.getCount() == 0) {
            ArrayList<Object> topArray = new ArrayList<>();
            topArray.add(JsonClass.jrrj(jsonObject,"slide_ads"));
            topArray.add(jsonObject);
            JSONArray jrr_brands = JsonClass.jrrj(jsonObject,"brand_list");
            for (int i = 0 ; i < jrr_brands.length() ; i ++) {
                JSONObject json_brand = JsonClass.jjrr(jrr_brands,i);
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
                shopBean.setBrand_desc(JsonClass.sj(json_brand,"brand_desc"));
                topArray.add(shopBean);
            }
            myAdapter.addData(topArray);
        }
        JSONArray jrr_goods = JsonClass.jrrj(jsonObject, "goods_list");
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < jrr_goods.length(); i+=2) {
            Production[] productions = new Production[2];
            if (i < jrr_goods.length()) {
                JSONObject json_good = JsonClass.jjrr(jrr_goods, i);
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
                productions[0] = production;
            }
            if (i + 1 < jrr_goods.length()) {
                JSONObject json_good = JsonClass.jjrr(jrr_goods, i + 1);
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
                productions[1] = production;
            }
            if (productions[0] != null) {
                arrayList.add(productions);
            }
        }
        myAdapter.addData(arrayList);
    }

    //region 选择item按钮

    /**
     * 添加按钮
     */
    protected void initSelectItemLayout(ArrayList<Category> categories, final String t) {
        siv.focus(t == null || t.length() == 0);
        siv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (superDeliverHomeDelegate != null) {
                    Category category = new Category();
                    category.setCategoryName(((SelectItemView)v).getName());
                    category.setCategoryID(((SelectItemView)v).getType());
                    superDeliverHomeDelegate.search(category);
                }
            }
        });
        arrListSelectItem = new ArrayList<>();
        if (contain_select_layout.getChildCount() > 0) {
            contain_select_layout.removeAllViews();
        }
        for (int i = 0; i < categories.size(); i++) {
            String type = categories.get(i).getCategoryID();
            String name = categories.get(i).getCategoryName();
            SelectItemView siv = new SelectItemView(getContext(), name, type);
            siv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (superDeliverHomeDelegate != null) {
                        Category category = new Category();
                        category.setCategoryName(((SelectItemView)v).getName());
                        category.setCategoryID(((SelectItemView)v).getType());
                        superDeliverHomeDelegate.search(category);
                    }
                }
            });
            arrListSelectItem.add(siv);
            contain_select_layout.addView(siv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
    //endregion


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                GetBaseActivity().BackActivity();
                break;
            case R.id.layout_search:
                Intent intent = new Intent();
                intent.putExtra(SuperDeliverSearch.SuperDeliverSearchDelegate,superDeliverSearchDelegate);
                GetBaseActivity().AddActivity(SuperDeliverSearch.class,0,intent);
                break;
            default:
        }
    }
    //region  SlideDelegate
    @Override
    public void clickItem(Integer clickPosition, View clickView, JSONObject json_item) {
        ClickBean clickBean = new ClickBean(JsonClass.sj(json_item,"ad_link"),JsonClass.sj(json_item,"ad_code"),JsonClass.sj(json_item,"data_type"),JsonClass.sj(json_item,"num_iid"));
        itemClick(clickBean);
    }
    //endregion

    //region SuperKillItemDelegate
    @Override
    public void itemClick(ClickBean clickBean) {
        if (!ItemClick.switchNormalListener(clickBean.getData_type(),clickBean.getAd_link())) {
            ItemClick.judgeSpecialListener(clickDelegate,null,clickBean.getData_type(),clickBean.getAd_link(),clickBean.getNum_iid());
        }
    }
    //endregion

    //region BrandViewDelegate
    @Override
    public void chooseShop(ShopBean shopBean) {
        Intent intent = new Intent();
        intent.putExtra(BrandActivity.SHOPBEAN,shopBean);
        GetBaseActivity().AddActivity(BrandActivity.class,0,intent);
    }
    //endregion

    //region DoubleGoodsDelegate
    @Override
    public void chooseProduction(final Production production) {
        if (!User.is_activate() && User.isShowActivity(getContext()) && User.getActivity_msg().length() > 0) {
            MessageBox.Show(getContext(), Default.AppName, User.getActivity_msg(), new String[]{User.getActivity_btn_goActivity(), User.getActivity_check_text(),
                    User.getActivity_btn_justBuy()}, new MessageBox.MessBtnBack() {
                @Override
                public void Back(int index) {
                    switch (index) {
                        case 0:
                            ItemClick.switchNormalListener(ItemClick.SuperDiscount_list_3, null);
                            break;
                        case 1:
                            User.setShowActivity(getContext(), false);
                            break;
                        case 2:
                            if (production.isShow_coupon()) {
                                superDeliverHomeDelegate.getCoupons(production);
                            } else {
                                ItemClick.judgeSpecialListener(clickDelegate,null,ItemClick.SuperDiscount_goods,production.getGoods_id(),production.getNum_iid());
                            }
                            break;
                        default:
                    }
                }
            });
        } else {
            if (production.isShow_coupon()) {
                superDeliverHomeDelegate.getCoupons(production);
            } else {
                ItemClick.judgeSpecialListener(clickDelegate,null,ItemClick.SuperDiscount_goods,production.getGoods_id(),production.getNum_iid());
            }
        }
    }
    //endregion

    private class MyAdapter extends NomalAdapter {
        private static final int slide_ads = 0;
        private static final int superSeckill = 1;
        private static final int brand_list = 2;
        private static final int goods_list = 3;

        public MyAdapter(Context c) {
            super(c);
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position).getClass().equals(JSONArray.class)) {
                return slide_ads;
            } else if (getItem(position).getClass().equals(JSONObject.class)) {
                return superSeckill;
            } else if (getItem(position).getClass().equals(ShopBean.class)) {
                return brand_list;
            } else if (getItem(position).getClass().equals(Production[].class)) {
                return goods_list;
            } else return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case slide_ads:
                    if (!(convertView instanceof SlideView)) {
                        convertView = new SlideView(c);
                    }

                    if (convertView instanceof SlideView) {
                        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Default.dip2px(117f,c)));
                    }
                    ((SlideView) convertView).setInfo(SuperDeliverHome.this, (JSONArray) getItem(position));
                    break;
                case superSeckill:
                    if (!(convertView instanceof SuperKillItemLayout) ){
                        convertView = new SuperKillItemLayout(c);
                    }
                    convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ((SuperKillItemLayout) convertView).setInfo(SuperDeliverHome.this, (JSONObject) getItem(position));
                    break;
                case brand_list:
                    if (!(convertView instanceof BrandView)) {
                        convertView = new BrandView(c);
                    }
                    convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    ((BrandView) convertView).setInfo(SuperDeliverHome.this, (ShopBean) getItem(position),(int)((ShopBean) getItem(position)).getTag() == 0);
                    break;
                case goods_list:
                    if (!(convertView instanceof DoubleGoodsView)) {
                        convertView = new DoubleGoodsView(c);
                    }
                    convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ((DoubleGoodsView) convertView).setInfo(SuperDeliverHome.this,(Production[]) getItem(position));
                    break;
                default:
                    convertView = new NoDataView(c);
            }
            return convertView;
        }
    }
}
