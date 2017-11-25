package com.wzzc.other_view.production.list.main_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.base.ExtendBaseView;
import com.wzzc.index.ShoppingCart.ShoppingCartActivity;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.BasicTitleView;
import com.wzzc.other_function.FileInfo;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/6.
 * <p>
 * 产品列表核心
 */

public class BrowseProductionView extends ExtendBaseView implements ListViewScrollDelegate {
    private static final String TAG = "BrowseProductionView";
    //region ```
    /**
     * 打包传输关键字
     */
    public static final String TYPEID = "type", KEYWORDS = "keyWords", CATEGORY_ID = "category_id", PRICE_RANGE = "price_range", BRAND_ID = "brand_id", INTRO = "intro", SORTBY = "sort_by";
    /**
     * 打包传输 TYPEID 内容
     */
    public static final String TYPEVALUE_ZCB_PRODUCTION = "zcb_production", TYPEVALUE_GCB_PRODUCTION = "gcb_production";

    /**
     * 打包传输 SORTBY 内容
     */
    public static final Integer SORT_INTEGRATED = 0, SORT_SALES = 1, SORT_NEW_PRODUCTION = 2, SORT_PRICE = 3;
    /**
     * 排序
     */
    public static final String SORT_BY_INTEGRATED = "click_count_desc", SORT_BY_SALES = "sales_desc", SORT_BY_NEW_PRODUCTIONS = "goods_id_desc", SORT_BY_PRICE = null;
    /**
     * 接口
     */
    public static final String URL_ZCB = "exsearch", URL_GCB = "search";
    //region 组件
    private EditText et_browse_production;
    @ViewInject(R.id.layout_item_browse_production)
    private LinearLayout layout_item_browse_production;
    @ViewInject(R.id.elv_production_browse)
    private ListView elv_production_browse;
    @ViewInject(R.id.tv_noData)
    private TextView tv_noData;
    @ViewInject(R.id.ib_top)
    private ImageButton ib_top;
    @ViewInject(R.id.ib_cart)
    private ImageButton ib_cart;
    @ViewInject(R.id.basicTitleView)
    private BasicTitleView basicTitleView;
    //endregion
    private String url;
    private String keyWords;
    private Integer category_id;
    private String price_range;
    private Integer brand_id;
    private String intro;
    private String sort_by;
    private Integer sby;
    private int state;
    private CurrentAdapter cAdapter;
    private ArrayList<Button> arr_bts;
    private boolean fixElv;
    private
    @ColorInt
    int colorItemBackground;
    private
    @ColorInt
    int colorItemProduction;
    private Drawable backgroundItemTVNowPrice;
    private
    @ColorInt
    int colorItemTvNowPrice;

    //endregion

    public BrowseProductionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrowseProductionView(Context context) {
        super(context);
    }

    protected void init() {
        et_browse_production = getSearchEditView();
        et_browse_production.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (et_browse_production.getRight() - event.getX() - et_browse_production.getPaddingRight() <= et_browse_production.getCompoundDrawables()[2].getIntrinsicWidth()) {
                        searchProduction();
                    }
                }
                return false;
            }
        });
        et_browse_production.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchProduction();
                }
                return false;
            }
        });
        basicTitleView.setViewOfTitle(et_browse_production);

        ib_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTop();
            }
        });

        ib_cart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    Integer cart_type = url.equals(URL_GCB) ? ShoppingCartActivity.SHOP_CART : ShoppingCartActivity.EXCHAGE_CART;
                    Intent intent = new Intent();
                    intent.putExtra(ShoppingCartActivity.TYPE, cart_type);
                    GetBaseActivity().AddActivity(ShoppingCartActivity.class, 0, intent);
                } else {
                    MessageBox.Show(getContext(), Default.AppName, "前往个人中心登陆！", new String[]{"取消", "登陆"}, new MessageBox.MessBtnBack() {
                        @Override
                        public void Back(int index) {
                            switch (index) {
                                case 0:
                                    break;
                                case 1:
                                    GetBaseActivity().AddActivity(LoginActivity.class);
                                    break;
                                default:
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void viewFirstLoad() {
        super.viewFirstLoad();
        AsynServer.listViewScrollDelegate = this;
    }

    @Override
    protected void setOthersInfo(JSONObject jsonInComing) throws JSONException {

    }

    @Override
    protected String getFileKey() {
        return TAG + url + category_id + sort_by + keyWords;
    }

    @Override
    protected ServerCallBack serverCallBack() {
        return new ServerCallBack() {
            @Override
            public void callBack(Object json_data) {
                System.out.println("``` json_data : " + json_data.toString());
                JSONArray jrr_data = (JSONArray) json_data;
                final ArrayList<JSONObject> data = new ArrayList<>();
                for (int i = 0; i < jrr_data.length(); i++) {
                    try {
                        Object obj_json = jrr_data.get(i);
                        JSONObject json = obj_json instanceof JSONObject ? new JSONObject(String.valueOf(obj_json)) : new JSONObject();
                        data.add(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                cAdapter.setData(data);
                Default.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fixElv) {
                            Default.fixListViewHeight(elv_production_browse);
                        }
                    }
                });
            }
        };
    }

    @Override
    protected void setInfoFromService(String type) {
        if (url != null && sort_by != null) {
            JSONObject para = new JSONObject();
            try {
                JSONObject filter = new JSONObject();
                filter.put("keywords", keyWords);
                filter.put("category_id", category_id);
                filter.put("price_range", price_range);
                filter.put("brand_id", brand_id);
                filter.put("intro", intro);
                filter.put("sort_by", sort_by);
                para.put("filter", filter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            firstInitFromFile = true;
            AsynServer.BackObject(GetBaseActivity(), url, true, elv_production_browse, para, new AsynServer.BackObject() {
                @Override
                public void Back(JSONObject sender) {
                    if (firstInitFromFile) {
                        cAdapter.clearData();
                        firstInitFromFile = false;
                    }
//                    FileInfo.SetUserString(getFileKey(), sender.toString(), getContext());
                    AsynServer.wantShowDialog = false;
                    try {
                        initialized(sender, serverCallBack());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //region Method

    public void setColorItemTvNowPrice(@ColorInt int colorItemTvNowPrice) {
        this.colorItemTvNowPrice = colorItemTvNowPrice;
    }

    public void setBackgroundItemTVNowPrice(Drawable dra) {
        this.backgroundItemTVNowPrice = dra;
    }

    public void setItemProductionColor(@ColorInt int colorItem) {
        this.colorItemProduction = colorItem;
    }

    public void setColorItem(@ColorInt int colorItem) {
        this.colorItemBackground = colorItem;
    }

    public void setSearchKey(String searchKey) {
        et_browse_production.setText(searchKey);
    }

    public void searchProduction() {
        if (url.equals(URL_GCB)) {
            FileInfo.RemoveUserString(FileInfo.PRODUCTION_GCB_INTEGRATED, getContext());
            FileInfo.RemoveUserString(FileInfo.PRODUCTION_GCB_NEW_PRODUCTION, getContext());
            FileInfo.RemoveUserString(FileInfo.PRODUCTION_GCB_PRICE, getContext());
            FileInfo.RemoveUserString(FileInfo.PRODUCTION_GCB_SALES, getContext());
        } else {
            FileInfo.RemoveUserString(FileInfo.PRODUCTION_ZCB_INTEGRATED, getContext());
            FileInfo.RemoveUserString(FileInfo.PRODUCTION_ZCB_NEW_PRODUCTION, getContext());
            FileInfo.RemoveUserString(FileInfo.PRODUCTION_ZCB_PRICE, getContext());
            FileInfo.RemoveUserString(FileInfo.PRODUCTION_ZCB_SALES, getContext());
        }
        cAdapter.clearData();
        String str = et_browse_production.getText().toString();
        if (str.trim().length() > 0) {
            keyWords = et_browse_production.getText().toString();
            setInfoFromService("...");
        } else {
            keyWords = "";
            setInfoFromService("...");
        }
    }

    public EditText getSearchEditView() {
        EditText et = new EditText(getContext());
        et.setHint("请输入您要搜索的商品名");
        et.setHintTextColor(ContextCompat.getColor(GetBaseActivity(), R.color.tv_hint_red));
        et.setTextColor(ContextCompat.getColor(GetBaseActivity(), R.color.tv_White));
        et.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.browse_production_edit_bg));
        et.setTextSize(14);
        et.setPadding(3, 0, 3, 0);
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.search_transpart);
        d.setBounds(0, 0, d.getIntrinsicWidth() * 2, d.getMinimumHeight() * 2);
        et.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        et.setCompoundDrawablePadding(3);
        return et;
    }

    public void setBasicTitleVisible(int visible) {
        basicTitleView.setVisibility(visible);
    }

    public void scrollToBottom() {
        elv_production_browse.scrollTo(elv_production_browse.getScrollX(), 1);
    }

    public void scrollToTop() {
        Default.scrollToListviewTop(elv_production_browse);
    }

    /**
     * 是否格式化listView的高度
     */
    public void setFixElv(boolean fix) {
        fixElv = fix;
    }

    public void setCartVisible(int visible) {
        ib_cart.setVisibility(visible);
    }

    public void setTopVisible(int visible) {
        ib_top.setVisibility(visible);
    }
    //endregion

    public void setInfo(String typeValue, Integer category_id, String price_range, Integer brand_id, String intro, String keyWords, Integer sby) {
        if (typeValue == null) {
            return;
        }
        switch (typeValue) {
            case TYPEVALUE_GCB_PRODUCTION:
                url = URL_GCB;
                break;
            case TYPEVALUE_ZCB_PRODUCTION:
                url = URL_ZCB;
                break;
            default:
                url = URL_ZCB;
        }
        cAdapter = new CurrentAdapter(GetBaseActivity());
        elv_production_browse.setAdapter(cAdapter);
        this.category_id = category_id;
        this.price_range = price_range;
        this.brand_id = brand_id;
        this.intro = intro;
        this.keyWords = keyWords;
        this.sby = sby;
        et_browse_production.setText(keyWords);

        arr_bts = new ArrayList<>();
        tv_noData.setVisibility(View.GONE);
        elv_production_browse.setVisibility(View.VISIBLE);
        final ArrayList<String> arr_item = new ArrayList<String>() {{
            add("综合");
            add("销量");
            add("新品");
            add("价格");
        }};
        final int firstIndex;
        if (sby != null) {
            firstIndex = sby;
        } else {
            firstIndex = 0;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addItemSelect(arr_item, firstIndex);
            }
        }, 0);

    }

    public void addItemSelect(ArrayList<String> arrayList, int firstIndex) {
        layout_item_browse_production.setWeightSum(arrayList.size());

        for (int i = 0; i < arrayList.size(); i++) {
            Button btn = new Button(GetBaseActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            btn.setText(arrayList.get(i));
            btn.setLayoutParams(lp);
            btn.setBackgroundColor(ContextCompat.getColor(GetBaseActivity(), R.color.bg_White));
            btn.setOnClickListener(clickListener);
            layout_item_browse_production.addView(btn);
            arr_bts.add(btn);
            if (i == firstIndex) {
                btn.setTextColor(ContextCompat.getColor(GetBaseActivity(), R.color.tv_Red));
                btn.callOnClick();
            } else {
                btn.setTextColor(ContextCompat.getColor(GetBaseActivity(), R.color.tv_Gray));
            }

            if (i != arrayList.size() - 1) {
                TextView tv_line = new TextView(GetBaseActivity());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 0;
                tv_line.setBackgroundColor(ContextCompat.getColor(GetBaseActivity(), R.color.tv_line));
                tv_line.setLayoutParams(params);
                layout_item_browse_production.addView(tv_line);
            }
        }
    }

    //region Action
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            keyWords = et_browse_production.getText().toString();
            Button btn = (Button) v;
            String str_search_for = btn.getText().toString();
            switch (str_search_for) {
                case "综合":
                    sort_by = SORT_BY_INTEGRATED;
                    break;
                case "销量":
                    sort_by = SORT_BY_SALES;
                    break;
                case "新品":
                    sort_by = SORT_BY_NEW_PRODUCTIONS;
                    break;
                case "价格":
                    sort_by = SORT_BY_PRICE;

                    Default.showToast(getContext().getString(R.string.notDevelop), Toast.LENGTH_LONG);
                    return;
//                    break;
                default:
                    Default.showToast(str_search_for + "触发事件", Toast.LENGTH_SHORT);
            }
            changeBtn(btn);
//            judgeFileHasDataAndInitializedData();
            if (!Default.isConnect(getContext())) {
                showNoNetWorkView();
            } else
                judgeNetConnectedAndSetInfoFromService(null);
        }
    };

    //endregion

    //region Helper
    private void changeBtn(Button button) {
        int clickIndex = 0;
        for (int i = 0; i < arr_bts.size(); i++) {
            arr_bts.get(i).setTextColor(ContextCompat.getColor(GetBaseActivity(), R.color.tv_Gray));
            if (button.getText().toString().trim().equals(arr_bts.get(i).getText().toString().trim())) {
                clickIndex = i;
            }

        }


        arr_bts.get(clickIndex).setTextColor(ContextCompat.getColor(GetBaseActivity(), R.color.tv_Red));
    }

    @Override
    public void dismissToastComponent() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.update_animator_small);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ib_top.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ib_top.startAnimation(animation);
    }

    @Override
    public void showToastComponent() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.update_animator_recory);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ib_top.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ib_top.startAnimation(animation);
    }

    @Override
    public void scrollChanged(int state) {
        switch (state) {
            case 1: {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.update_animator_small);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ib_cart.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ib_cart.startAnimation(animation);
                break;
            }
            default:
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.update_animator_recory);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ib_cart.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ib_cart.startAnimation(animation);
        }
    }
    //endregion

    //region class
    private class CurrentAdapter extends BaseAdapter {
        Context c;
        ArrayList<JSONObject> data;
        private boolean zcb;

        CurrentAdapter(Context c) {
            this.c = c;
            data = new ArrayList<>();
            System.out.println("server url : " + url);
            if (url != null && url.equals(URL_ZCB)) {
                zcb = true;
            }
        }

        public void setData(ArrayList<JSONObject> data) {

            if (data == null) {
                return;
            }
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        public void clearData() {
            this.data.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size() % 2 == 0 ? data.size() / 2 : data.size() / 2 + 1;
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
        public int getItemViewType(int position) {
            if (position + 1 == getCount() && data.size() % 2 == 1) {
                return 1;
            } else return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LinearLayout layout = new LinearLayout(c);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 5, 10, 5);
//                layout.setLayoutParams(params);//华为出错
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setWeightSum(2);

                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params1.weight = 1;
                params1.setMarginEnd(5);
                Browse_ProductionView bpv1 = new Browse_ProductionView(c);
                if (colorItemBackground != 0) {
                    bpv1.setBackGroundColor(colorItemBackground);
                }
                if (colorItemProduction != 0) {
                    bpv1.setProductionNameColor(colorItemProduction);
                }
                if (backgroundItemTVNowPrice != null) {
                    bpv1.setTv_nowPriceBackground(backgroundItemTVNowPrice);
                }
                if (colorItemTvNowPrice != 0) {
                    bpv1.setTv_nowPriceColor(colorItemTvNowPrice);
                }
                bpv1.setLayoutParams(params1);
                layout.addView(bpv1);
                bpv1.setInfo(data.get(position * 2), zcb);
                switch (getItemViewType(position)) {
                    case 0: {
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params2.weight = 1;
                        params2.setMarginStart(5);
                        Browse_ProductionView bpv2 = new Browse_ProductionView(c);
                        if (colorItemBackground != 0) {
                            bpv2.setBackGroundColor(colorItemBackground);
                        }
                        if (colorItemProduction != 0) {
                            bpv2.setProductionNameColor(colorItemProduction);
                        }
                        if (backgroundItemTVNowPrice != null) {
                            bpv2.setTv_nowPriceBackground(backgroundItemTVNowPrice);
                        }
                        if (colorItemTvNowPrice != 0) {
                            bpv2.setTv_nowPriceColor(colorItemTvNowPrice);
                        }
                        bpv2.setLayoutParams(params2);
                        layout.addView(bpv2);
                        Browse_ProductionView[] bpvs = new Browse_ProductionView[]{bpv1, bpv2};
                        layout.setTag(bpvs);
                        bpv2.setInfo(data.get(position * 2 + 1), zcb);
                        break;
                    }
                    case 1: {
                        Browse_ProductionView[] bpvs = new Browse_ProductionView[]{bpv1};
                        layout.setTag(bpvs);
                        break;
                    }
                    default:
                }
                convertView = layout;
            } else {

                LinearLayout layout = (LinearLayout) convertView;

                Browse_ProductionView[] bpvs = (Browse_ProductionView[]) layout.getTag();
                Browse_ProductionView bpv1 = bpvs[0];
                bpv1.setInfo(data.get(position * 2), zcb);

                switch (getItemViewType(position)) {
                    case 0:
                        if (bpvs.length > 1) {
                            Browse_ProductionView bpv2 = bpvs[1];
                            bpv2.setInfo(data.get(position * 2 + 1), zcb);
                        }
                        break;
                    case 1:
                        break;
                    default:
                }
                convertView = layout;
            }

            return convertView;
        }
    }
    //endregion
}
