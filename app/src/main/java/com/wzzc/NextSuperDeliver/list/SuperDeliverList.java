package com.wzzc.NextSuperDeliver.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextIndex.views.e.LoginDelegate;
import com.wzzc.NextSuperDeliver.Production;
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
 * Created by by Administrator on 2017/8/8.
 *
 */
@ContentView(R.layout.super_deliver_list)
public class SuperDeliverList extends NewBaseActivity implements FilterSuperDeliverDelegate, ListViewScrollDelegate, ClickDelegate,
        SuperDeliverListItemDelegate, SelectDialogDelegate, ArrangeDelegate,LoginDelegate {
    public static final String CATEGORY = "category";//分类
    private static final String ListItem = "listItem";//加载数据类别
    //region search
    @ViewInject(R.id.tv_category)
    TextView tv_category;
    @ViewInject(R.id.edt_search)
    EditText edt_search;
    @ViewInject(R.id.tv_search)
    TextView tv_search;
    @ViewInject(R.id.btn_back)
    RelativeLayout layout_back;
    //endregion
    //region 筛选/组件
    @ViewInject(R.id.layout_desc)
    RelativeLayout layout_desc;
    @ViewInject(R.id.layout_rebate)
    RelativeLayout layout_rebate;
    @ViewInject(R.id.layout_filter)
    RelativeLayout layout_filter;
    @ViewInject(R.id.filter_select)
    ImageView filter_select;
    @ViewInject(R.id.tv_desc)
    TextView tv_desc;
    @ViewInject(R.id.tv_rebate)
    TextView tv_rebate;
    @ViewInject(R.id.tv_filter)
    TextView tv_filter;
    @ViewInject(R.id.switch_coupons)
    Switch switch_coupons;
    //endregion
    @ViewInject(R.id.superDeliverListView)
    ListView superDeliverListView;
    @ViewInject(R.id.imb_top)
    ImageButton imb_top;
    @ViewInject(R.id.touchFilter)
    RelativeLayout touchFilter;
    //region 无数据
    @ViewInject(R.id.layout_noData)
            RelativeLayout layout_noData;
    @ViewInject(R.id.layout_noData_filter)
    LinearLayout layout_noData_filter;
    @ViewInject(R.id.tv_noData_category)
            TextView tv_noData_category;
    @ViewInject(R.id.tv_noData_keyWords)
    TextView tv_noData_keyWords;
    @ViewInject(R.id.tv_noData_pType)
    TextView tv_noData_pType;
    @ViewInject(R.id.tv_noData_rebate)
    TextView tv_noData_rebate;
    @ViewInject(R.id.tv_noData_money)
    TextView tv_noData_money;
    //endregion
    Category category;//选择分类
    private ArrayList<Category> categoryArrayList;
    private static SuperDeliverList superDeliverList;
    private MyAdapter myAdapter;
    FragmentTransaction fragmentTransaction;
    String key;
    boolean firstClick;//第一次进入
    View currentView;//当前点击的view
    BackChildList backChildList;//筛选分类
    BackMainList backMainList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(CATEGORY)) {
            category = bundle.getParcelable(CATEGORY);
        } else {
            category = new Category(Category.AllCategoryID, Category.AllCategoryName, Category.AllCategoryID);
        }

        changeSearchCategory(category);
    }

    @Override
    protected void init() {

        //region 搜索栏
        tv_category.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == KeyEvent.KEYCODE_SEARCH) {
                    tv_search.callOnClick();
//                }
                return false;
            }
        });
        layout_back.setOnClickListener(this);
        //endregion

        //region 筛选栏
        layout_desc.setOnClickListener(this);
        layout_rebate.setOnClickListener(this);
        switch_coupons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int color = isChecked ? R.color.orangered : R.color.tv_Gray;
                switch_coupons.setTextColor(ContextCompat.getColor(SuperDeliverList.this, color));
                category.setIsCoupon(isChecked ? 1 : 0);
                tv_search.callOnClick();
            }
        });
        layout_filter.setOnClickListener(this);
        touchFilter.setOnClickListener(this);//屏蔽下层组件获取点击事件，进而捕获ActionUP事件
        touchFilter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("````` can onTouch");
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN :
                        System.out.println("````` can ACTION_DOWN");
                        downX = event.getX();
                        downTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("````` can ACTION_UP");
                        float upX = event.getX();
                        long upTime = System.currentTimeMillis();
                        if ((downX - upX) > distanceX && (upTime-downTime) < distanceTime) {
                            System.out.println("````` can");
                            layout_filter.callOnClick();
                        } else {
                            System.out.println("````` can't");
                        }
                        break;
                    default:
                        System.out.println("````` can event.getAction()");
                }
                return false;
            }
        });
        //endregion

        //region 暂无产品
        tv_noData_category.setOnClickListener(this);
        layout_noData_filter.setOnClickListener(this);
        //endregion

        imb_top.setOnClickListener(this);
        myAdapter = new MyAdapter(this);
        superDeliverListView.setAdapter(myAdapter);
        superDeliverList = this;
        useKey();

        filter(category);
    }

    private void initData() {
        category = new Category();
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
                if(5 == category.getpType() || 6 == category.getpType()){
                    return;
                }
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
                key = null;
            }
        };
    }

    private void cb(Object obj, String s) {
        switch (s) {
            case ListItem:
                JSONObject data = (JSONObject) obj;
                if (data.has("cat_info")) {
                    JSONObject json_cat_info = JsonClass.jj(data,"cat_info");
                    Log.i("json_cat_info",json_cat_info.toString());
                    if (json_cat_info.has("cat_id")) {
                        category.setCategoryID(JsonClass.sj(json_cat_info,"cat_id"));
                    }
                    if (json_cat_info.has("cat_name")) {
                        category.setCategoryName(JsonClass.sj(json_cat_info,"cat_name"));
                    }
                    if (json_cat_info.has("parent_id")) {
                        category.setParentCategoryID(JsonClass.sj(json_cat_info,"parent_id"));
                    }
                    changeSearchCategory(category);
                }
                //region init category
                categoryArrayList = new ArrayList<>();
                categoryArrayList.add(new Category(Category.AllCategoryID, Category.AllCategoryName, Category.AllCategoryID));
                JSONArray jrr_cat_list = JsonClass.jrrj(data, "cat_list");
                for (int i = 0; i < jrr_cat_list.length(); i++) {
                    JSONObject json_category = JsonClass.jjrr(jrr_cat_list, i);
                    Category cur_category = new Category();
                    cur_category.setCategoryID(JsonClass.sj(json_category, "cat_id"));
                    cur_category.setParentCategoryID(JsonClass.sj(json_category, "parent_id"));
                    cur_category.setCategoryName(JsonClass.sj(json_category, "cat_name"));
                    categoryArrayList.add(cur_category);
                }
                //endregion
                if (backChildList != null) {
                    backChildList.backCatParentInfo(JsonClass.jj(data,"cat_info"));
                    backChildList.backChildSelect(JsonClass.jrrj(data,"cat_list_sub"));
                }
                if (backMainList != null) {
                    backMainList.backMainSelect(jrr_cat_list);
                }
                JSONArray jrr_goods_list = JsonClass.jrrj(data, "goods_list");
                ArrayList<Object> arrList = new ArrayList<>();
                for (int i = 0; i < jrr_goods_list.length(); i++) {
                    JSONObject json = JsonClass.jjrr(jrr_goods_list, i);
                    Production production = new Production();
                    production.setGoods_id(JsonClass.sj(json,"goods_id"));
                    production.setGoods_name(JsonClass.sj(json,"goods_name"));
                    production.setShop_price(JsonClass.sj(json,"shop_price"));
                    production.setIs_new(JsonClass.ij(json,"is_new") == 1);
                    production.setIs_seckill(JsonClass.ij(json,"is_seckill") == 1);
                    production.setIs_best(JsonClass.ij(json,"is_best") == 1);
                    production.setIs_hot(JsonClass.ij(json,"is_hot") == 1);
                    production.setGoods_thumb(JsonClass.sj(json,"goods_thumb"));
                    production.setGoods_img(JsonClass.sj(json,"goods_img"));
                    production.setRebates(JsonClass.sj(json,"rebates"));
                    production.setRebate_text(JsonClass.sj(json,"rebate_text"));
                    production.setIcon(JsonClass.sj(json,"icon"));
                    production.setNum_iid(JsonClass.sj(json,"num_iid"));
                    production.setShow_coupon(JsonClass.ij(json,"show_coupon") == 1);
                    production.setCoupon_info(JsonClass.sj(json,"coupon_info"));
                    production.setCoupon_click_url(JsonClass.sj(json,"coupon_click_url"));
                    production.setFooter_title(JsonClass.sj(json,"footer_title"));
                    production.setGoods_from(JsonClass.sj(json,"goods_from"));
                    arrList.add(production);
                }
                myAdapter.addData(arrList);

                if (firstClick && arrList.size() == 0) {//第一次加载时
                    showNoData();
                } else {
                    dismissNoData();
                }
                if (!newServer) {
                    firstClick = false;
                }
                break;
            default:
        }
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        switch (s) {
            case ListItem:
                myAdapter.clearData();
                AsynServer.wantShowDialog = false;
                break;
            default:
        }
    }

    @Override
    protected void publish() {
        filter(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_category:
                showCategoryFragment();
                break;
            case R.id.tv_search:
                category.setKeyWords(edt_search.getText().toString());
                filter(category);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.layout_desc:
                showArrangeFragment();
                break;
            case R.id.layout_rebate:
                category.setSort_method(Filter.FilterReBate);
                category.setSort_order(Filter.SortOrderDesc);
                clearArrangeColor();
                changeFocusBetweenArrangeAndReBate(category.getSort_method());
                filter(category);
                break;
            case R.id.layout_filter:
                showFilterFragment();
                break;
            case R.id.tv_noData_category:
                tv_category.callOnClick();
                break;
            case R.id.layout_noData_filter:
                layout_filter.callOnClick();
                break;
            case R.id.imb_top:
                Default.scrollToListviewTop(superDeliverListView);
                break;
            default:
        }
    }

    //region Action

    /**
     * 分类
     */
    private void showCategoryFragment() {
        outFragment();
        hasFragment = true;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SelectItemFragment selectItemFragment = new SelectItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SelectItemFragment.SELECT_ITEM_DATA, categoryArrayList);
        bundle.putParcelable(SelectItemFragment.FocusCategory, category);
        bundle.putParcelable(SelectItemFragment.SDD, this);
        selectItemFragment.setArguments(bundle);
        fragmentTransaction.setCustomAnimations(R.anim.push_top_in, R.anim.push_top_out, R.anim.push_top_in, R.anim.push_top_out);
        fragmentTransaction.add(R.id.contain_category, selectItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 筛选
     */
    private void showFilterFragment() {
        useKey();
        firstClick = true;
        outFragment();
        hasFragment = true;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FilterSuperDeliverFragment filterSuperDeliverFragment = new FilterSuperDeliverFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FilterSuperDeliverFragment.CateGory, category);
        bundle.putParcelable(FilterSuperDeliverFragment.FilterSuperDeliverDelegate, this);
        filterSuperDeliverFragment.setArguments(bundle);
        fragmentTransaction.setCustomAnimations(R.anim.activity_load_in, R.anim.activity_exit_out, R.anim.activity_load_in, R.anim.activity_exit_out);
        fragmentTransaction.add(R.id.contain_filter, filterSuperDeliverFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 排序选择
     */
    private void showArrangeFragment() {
        outFragment();
        hasFragment = true;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ArrangeFragment arrangeFragment = new ArrangeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ArrangeFragment.ArrangeDelegate, this);
        bundle.putString(ArrangeFragment.SortMethod, category.getSort_method());
        bundle.putString(ArrangeFragment.SortOrder, category.getSort_order());
        arrangeFragment.setArguments(bundle);
        fragmentTransaction.setCustomAnimations(R.anim.push_top_in, R.anim.push_top_out, R.anim.push_top_in, R.anim.push_top_out);
        fragmentTransaction.add(R.id.layout_arrange, arrangeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    //endregion

    //region FilterSuperDeliverDelegate
    @Override
    public void filter(Category category) {
        firstClick = true;
        if (category.hasSetFilter()) {
            tv_filter.setTextColor(ContextCompat.getColor(this, R.color.orangered));
            filter_select.setBackground(ContextCompat.getDrawable(this, R.drawable.filter_orange));
        } else {
            tv_filter.setTextColor(ContextCompat.getColor(this, R.color.tv_Gray));
            filter_select.setBackground(ContextCompat.getDrawable(this, R.drawable.filter));
        }
        this.category = category;
        initDataFromCache(ListItem);
        JSONObject para = new JSONObject();
        try {
            para.put("keywords", category.getKeyWords());
            para.put("category_id", category.getCategoryID());
            para.put("ptype", category.getpType());
            para.put("minrebate", category.getMinRebate());
            para.put("maxrebate", category.getMaxRebate());
            para.put("minprice", category.getMinPrice());
            para.put("maxprice", category.getMaxPrice());
            para.put("sort", category.getSort_method());
            para.put("order", category.getSort_order());
            para.put("is_coupon", category.getIsCoupon());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initDataFromServer(this, "SuperDiscount/list", !FileInfo.IsAtUserString(getFileKey(),SuperDeliverList.this), para, superDeliverListView, ListItem);
        changeSearchCategory(category);
        changeArrangeText(category.getSort_method(), category.getSort_order());
    }

    @Override
    public void specialFilterChild(Category category, BackChildList backChildList) {
        this.category = category;
        useKey();
        firstClick = true;
        this.backChildList = backChildList;
        this.backMainList = null;
        filter(category);
    }

    @Override
    public void specialFilterMain(Category category, BackMainList backMainList) {
        this.category = category;
        useKey();
        firstClick = true;
        this.backChildList = null;
        this.backMainList = backMainList;
        filter(category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator CREATOR = new Creator<SuperDeliverList>() {
        @Override
        public SuperDeliverList createFromParcel(Parcel source) {
            return superDeliverList;
        }

        @Override
        public SuperDeliverList[] newArray(int size) {
            return new SuperDeliverList[0];
        }
    };

    //endregion

    //region ListViewScrollDelegate
    @Override
    public void dismissToastComponent() {
        imb_top.setVisibility(View.GONE);
    }

    @Override
    public void showToastComponent() {
        imb_top.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrollChanged(int state) {

    }

    //endregion

    //region ClickDelegate
    @Override
    public void shouldLogin(final View clickView) {
        MessageBox.Show(this, Default.AppName,"尚未登陆", new String[]{"取消","前往登陆"}, new MessageBox.MessBtnBack() {
            @Override
            public void Back(int index) {
                switch (index){
                    case 0:
                        break;
                    case 1:
                        currentView = clickView;
                        if (clickView != null) {
                            Intent intent = new Intent();
                            intent.putExtra(LoginActivity.LoginDelegate,SuperDeliverList.this);
                            AddActivity(LoginActivity.class,0,intent);
                        }
                        break;
                    default:
                }
            }
        });
    }

    //endregion

    //region SuperDeliverListItemDelegate
    @Override
    public void buyGoods(String goodID) {

    }

    @Override
    public void getCoupons(Production production) {
        goCoupon( production.getCoupon_click_url(), production.getFooter_title(), production.getGoods_id());
    }

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
                        customProgressDialog = CustomProgressDialog.createDialog(SuperDeliverList.this,R.style.CustomProgressDialog);
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

    //region SelectDialogDelegate
    @Override
    public void clickOK(Category category) {
        SuperDeliverList.this.category.setCategoryID(category.getCategoryID());
        SuperDeliverList.this.category.setCategoryName(category.getCategoryName());
        changeSearchCategory(category);
        outFragment();
        tv_search.callOnClick();
    }

    @Override
    public void clickCancel() {
        outFragment();
    }

    //endregion

    //region ArrangeDelegate
    @Override
    public void arrange(String sort_method, String sort_order) {
        category.setSort_method(sort_method);
        category.setSort_order(sort_order);
        changeArrangeText(sort_method, sort_order);
        changeFocusBetweenArrangeAndReBate(sort_method);
        filter( category);
    }
    //endregion

    //region LoginDelegate
    @Override
    public void loginOK() {
        if (currentView != null) {
            currentView.callOnClick();
        }
    }
    //endregion

    //region Method

    /** 显示无数据*/
    public void showNoData () {
        layout_noData.setVisibility(View.VISIBLE);
        tv_noData_category.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_noData_category.setText(category.getCategoryName());
        tv_noData_keyWords.setText(category.getKeyWords());
        tv_noData_pType.setText(category.getPTypeString());
        if (Integer.valueOf(category.getMaxRebate()) > Integer.valueOf(category.getMinRebate())) {
            tv_noData_rebate.setText(category.getMinRebate() + " - " + category.getMaxRebate());
        } else {
            tv_noData_rebate.setText(category.getMinRebate() + " - ∞");
        }
        if (Integer.valueOf(category.getMaxPrice()) > Integer.valueOf(category.getMinPrice())) {
            tv_noData_money.setText(category.getMinPrice() + " - " + category.getMaxPrice());
        } else {
            tv_noData_money.setText(category.getMinPrice() + " - ∞");
        }
    }

    /** 隐藏无数据*/
    public void dismissNoData () {
        layout_noData.setVisibility(View.GONE);
    }

    /**
     * 修改搜索选项
     */
    public void changeSearchCategory(Category category) {
        tv_category.setText(category.getCategoryName());
        edt_search.setText(category.getKeyWords());
    }

    /**
     * 修改排序选中项
     */
    public void changeArrangeText(String sort_method, String sort_order) {
        switch (sort_method) {
            case Filter.FilterDefault:
                tv_desc.setText(getString(R.string.filter_com));
                break;
            case Filter.FilterMoney:
                switch (sort_order) {
                    case Filter.SortOrderDesc:
                        tv_desc.setText(getString(R.string.filter_money_desc));
                        break;
                    case Filter.SortOrderAsc:
                        tv_desc.setText(getString(R.string.filter_money_asc));
                        break;
                    default:
                }
                break;
            case Filter.FilterNumber:
                switch (sort_order) {
                    case Filter.SortOrderDesc:
                        tv_desc.setText(getString(R.string.filter_number_desc));
                        break;
                    case Filter.SortOrderAsc:
                        tv_desc.setText(getString(R.string.filter_number_asc));
                        break;
                    default:
                }
                break;
            default:
        }
    }

    /**
     * 聚焦排序选项
     */
    public void changeFocusBetweenArrangeAndReBate(String sort_method) {
        clearArrangeColor();
        switch (sort_method) {
            case Filter.FilterReBate:
                focusArrangeColor(tv_rebate);
                break;
            default:
                focusArrangeColor(tv_desc);
        }
    }
    //endregion

    //region Helper

    /**
     * 清除所有排序选择聚焦
     */
    private void clearArrangeColor() {
        tv_desc.setTextColor(ContextCompat.getColor(this, R.color.tv_Gray));
        tv_rebate.setTextColor(ContextCompat.getColor(this, R.color.tv_Gray));
    }

    /**
     * 聚焦某一个排序类型
     */
    private void focusArrangeColor(TextView tv) {
        tv.setTextColor(ContextCompat.getColor(this, R.color.orangered));
    }

    //endregion

    //region Adapter
    private class MyAdapter extends NomalAdapter {
        public MyAdapter(Context c) {
            super(c);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (!(convertView instanceof SuperDeliverListItemView)) {
                convertView = new SuperDeliverListItemView(c);
            }
            ((SuperDeliverListItemView) convertView).setInfo(SuperDeliverList.this, SuperDeliverList.this, (Production) getItem(position),category);
            return convertView;
        }
    }
    //endregion

    private void useKey() {
        key = "SuperDeliverList" +category.getKeyWords() + category.getCategoryID();//为了减少存储次数，防止滚动时存储
    }
}
