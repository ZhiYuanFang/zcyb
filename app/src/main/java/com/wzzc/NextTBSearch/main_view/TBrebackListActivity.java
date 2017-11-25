package com.wzzc.NextTBSearch.main_view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.NextSuperDeliver.list.Category;
import com.wzzc.NextSuperDeliver.list.SelectItemFragment;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.new_index.SuperDeliver.main_view.childView.SelectDialogDelegate;
import com.wzzc.new_index.SuperDeliver.main_view.childView.SelectItemView;
import com.wzzc.new_index.SuperDeliver.main_view.childView.SelectSecondDelegate;
import com.wzzc.new_index.SuperDeliver.main_view.childView.SelectSecondItemView;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.AsynServer.ListViewScrollDelegate;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_view.NoDataView;
import com.wzzc.other_view.editSelect.EditSelectText;
import com.wzzc.other_view.editSelect.SelectDelegate;
import com.wzzc.other_view.progressDialog.CustomProgressDialog;
import com.wzzc.taobao.TBDetailActivity;
import com.wzzc.taobao.TaoBao;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/7/4.
 */
@ContentView(R.layout.activity_tbrebacklist)
public class TBrebackListActivity extends NewBaseActivity implements SelectDelegate ,TBrebackItemDelegate, ListViewScrollDelegate, SelectDialogDelegate, SelectSecondDelegate,ClickDelegate {
    public static final String KeyWords = "keyWords";
    public static final String HighBack = "highBack";
    public static final String IsJD = "is_jd";
    String keyWords;
    boolean isJD = false;
    //region 组件
    @ViewInject(R.id.ndv)
    NoDataView ndv;
    @ViewInject(R.id.siv)
    SelectItemView siv;
    @ViewInject(R.id.et_search)
    private EditSelectText et_search;
    @ViewInject(R.id.contain_select_layout)
    private LinearLayout contain_select_layout;
    @ViewInject(R.id.layout_focus)
    RelativeLayout layout_focus;
    @ViewInject(R.id.siv_focus)
    SelectItemView siv_focus;
    @ViewInject(R.id.lv_more)
    private RelativeLayout lv_more;
    @ViewInject(R.id.ib_top)
    private ImageView ib_top;
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.btn_back)
    RelativeLayout btn_back;
    @ViewInject(R.id.ssiv)
    SelectSecondItemView ssiv;
    @ViewInject(R.id.contain_ssiv)
    RelativeLayout contain_ssiv;
    @ViewInject(R.id.layout_search)
    Button bt_search;
    //endregion
    //region 筛选/组件
    @ViewInject(R.id.layout_desc)
    RelativeLayout layout_desc;
    @ViewInject(R.id.layout_rebate)
    RelativeLayout layout_rebate;
    @ViewInject(R.id.layout_coupons)
    RelativeLayout layout_coupons;
    @ViewInject(R.id.layout_filter)
    RelativeLayout layout_filter;
    @ViewInject(R.id.img_filter)
    ImageView img_filter;
    @ViewInject(R.id.filter_select)
    ImageView filter_select;
    @ViewInject(R.id.tv_desc)
    TextView tv_desc;
    @ViewInject(R.id.tv_rebate)
    TextView tv_rebate;
    @ViewInject(R.id.tv_coupons)
    TextView tv_coupons;
    @ViewInject(R.id.tv_filter)
    TextView tv_filter;
    @ViewInject(R.id.switch_coupons)
    Switch switch_coupons;
    //endregion
    //全部的listItem
    ArrayList<SelectItemView> arrListSelectItem;
    public NomalAdapter myAdapter;
    public boolean addSecond;
    boolean isSecondItemClass;
    ArrayList<Category> categoryArrayList;
    View shouldLoginView;
    EditSelectText.SelectFragmentList skf;
    FragmentTransaction fragmentTransaction;
    boolean is_TMail;
    boolean useFilter;
    String sort_method;
    String filterStartBack, filterEndBack, filterMoneyBack, filterMoneyEnd;
    String sort_order_type;
    Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(GetIntentData(IsJD) == null){
            isJD = false;
        }else{
            isJD = (boolean) GetIntentData(IsJD);
            findViewById(R.id.need_hide_layout).setVisibility(View.GONE);
            findViewById(R.id.textView254).setVisibility(View.GONE);
        }
    }

    @Override
    protected void init() {
        sort_method = TBrebackItemDelegate.Sort_Method_Default;
        // 判断是否为京东，走京东流程
        if(GetIntentData(IsJD) == null){
            isJD = false;
        }else{
            isJD = (boolean) GetIntentData(IsJD);
            findViewById(R.id.need_hide_layout).setVisibility(View.GONE);
            findViewById(R.id.textView254).setVisibility(View.GONE);
        }

        initFilter();
        if (GetIntentData(KeyWords) == null) {
            keyWords = "";
        } else
            keyWords = (String) GetIntentData(KeyWords);
        myAdapter = new MyAdapter(this,isJD);
        listView.setAdapter(myAdapter);
        ssiv.ssd = this;
        btn_back.setOnClickListener(this);
        ib_top.setOnClickListener(this);
        lv_more.setOnClickListener(this);
        siv.setOnClickListener(this);
        layout_focus.setOnClickListener(this);
        et_search.setText(keyWords);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               bt_search.callOnClick();
                return false;
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeSelect();
                return false;
            }
        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(et_search.getText().toString());
            }
        });
        String high_back = "56%";
        if ( GetIntentData(HighBack) != null) {
            high_back = (String) GetIntentData(HighBack);
        }
        if(!isJD) {
            showDialog(high_back);
        }else{
            bt_search.callOnClick();
            et_search.selectDelegate = TBrebackListActivity.this;
        }
    }
    //region 筛选
    protected void initFilter() {
        layout_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter(et_search.getText().toString(),is_TMail);
            }
        });
        layout_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sort_method) {
                    case TBrebackItemDelegate.Sort_Method_Number:
                    case TBrebackItemDelegate.Sort_Method_Rebate:
                        img_filter.setBackground(ContextCompat.getDrawable(TBrebackListActivity.this, R.drawable.triangle_top_color_gray));
                        break;
                    default:
                        img_filter.setBackground(ContextCompat.getDrawable(TBrebackListActivity.this, R.drawable.triangle_top_color_orange));

                }
                showSelectDesc(v);
            }
        });

        layout_rebate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sort_method.equals(sort_method = TBrebackItemDelegate.Sort_Method_Rebate)) {
                    sort_method = TBrebackItemDelegate.Sort_Method_Default;
                    sort_order_type = "";
                    noUse();
                } else {
                    sort_method = TBrebackItemDelegate.Sort_Method_Rebate;
                    sort_order_type = TBrebackItemDelegate.Sort_Order_Desc;
                    useReback();
                }
                showFilterList(filterStartBack, filterEndBack, filterMoneyBack, filterMoneyEnd, sort_method, sort_order_type, is_TMail, et_search.getText().toString());
            }
        });

        switch_coupons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_TMail = isChecked;
                if (is_TMail) {
                    switch_coupons.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.orangered));
                } else {
                    switch_coupons.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.tv_Gray));
                }
              
                showFilterList(filterStartBack, filterEndBack, filterMoneyBack, filterMoneyEnd, sort_method, sort_order_type, is_TMail, et_search.getText().toString());
            }
        });
    }

    protected void showSelectDesc(View view) {
        PopupMenu popupMenu;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            popupMenu = new PopupMenu(TBrebackListActivity.this, view, Gravity.CENTER);
        } else {
            popupMenu = new PopupMenu(TBrebackListActivity.this, view);
        }
        popupMenu.inflate(R.menu.sort_tb_reback_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemID = item.getItemId();
                String filter_sort = item.getTitle().toString();
                tv_desc.setText(filter_sort);
                switch (itemID) {
                    case R.id.desc_1:
                        sort_method = TBrebackItemDelegate.Sort_Method_Default;
                        sort_order_type = "";
                        break;
                    case R.id.desc_4:
                        sort_method = TBrebackItemDelegate.Sort_Method_Number;
                        sort_order_type = TBrebackItemDelegate.Sort_Order_Desc;
                        break;
                    default:
                }
                useDesc();
                showFilterList(filterStartBack, filterEndBack, filterMoneyBack, filterMoneyEnd, sort_method, sort_order_type, is_TMail, et_search.getText().toString());
                return false;
            }
        });
        popupMenu.show();
    }

    private void useDesc() {
        tv_desc.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.orangered));
        img_filter.setBackground(ContextCompat.getDrawable(TBrebackListActivity.this, R.drawable.triangle_bottom_color_orange));
        tv_rebate.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.tv_Gray));
    }

    private void useReback() {
        tv_desc.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.tv_Gray));
        img_filter.setBackground(ContextCompat.getDrawable(TBrebackListActivity.this, R.drawable.triangle_bottom_color_gray));
        tv_rebate.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.orangered));
    }
    private void noUse() {
        tv_desc.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.tv_Gray));
        img_filter.setBackground(ContextCompat.getDrawable(TBrebackListActivity.this, R.drawable.triangle_bottom_color_gray));
        tv_rebate.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.tv_Gray));
    }
    //endregion
    private void showDialog (String highBack) {
        final Dialog dialog = new Dialog(this,R.style.CustomAlertDialog);
        View v = LayoutInflater.from(this).inflate(R.layout.layout_high_back_dialog,null);
        final TextView tv_high_back = (TextView) v.findViewById(R.id.tv_high_back);
        final RelativeLayout layout_high_back = (RelativeLayout) v.findViewById(R.id.layout_high_back);
        final TextView tv_info = (TextView) v.findViewById(R.id.tv_info);
        ImageView img_ball = (ImageView) v.findViewById(R.id.img_ball);
        tv_high_back.setText(highBack);
        dialog.setContentView(v);
        dialog.show();
        Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = Default.dip2px(300, this);
        dialog.getWindow().setAttributes(params);
        Animation ball = AnimationUtils.loadAnimation(this,R.anim.from_nomal_to_small);
        ball.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layout_high_back.setAlpha(0f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation ani = AnimationUtils.loadAnimation(TBrebackListActivity.this, R.anim.from_small_to_nomal);
                ani.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        layout_high_back.setAlpha(1f);
                        tv_high_back.setVisibility(View.GONE);
                        tv_info.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tv_high_back.setVisibility(View.VISIBLE);
                        tv_info.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                bt_search.callOnClick();
                                et_search.selectDelegate = TBrebackListActivity.this;
                            }
                        },1500);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layout_high_back.startAnimation(ani);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img_ball.startAnimation(ball);
    }

    private void showList(String keyWords, Category category) {
        System.out.println("showList");
        if (category == null) {
            category = new Category();
        }
        this.keyWords = keyWords;
        this.category = category;
        initDataFromCache(category.getCategoryID());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONObject para = new JSONObject();
                try {
                    if(!isJD){
                        para.put("minrebate", filterStartBack);
                        para.put("maxrebate", filterEndBack);
                        para.put("is_tmall",is_TMail ? 1 : 0);
                    }
                    para.put("minprice", filterMoneyBack);
                    para.put("maxprice", filterMoneyEnd);
                    para.put("sort",sort_method);
                    para.put("order",sort_order_type);
                    para.put("keywords", TBrebackListActivity.this.keyWords);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(isJD){
                    initDataFromServer(null, "JinDong/search", !FileInfo.IsAtUserString(getFileKey(),TBrebackListActivity.this), para, listView, TBrebackListActivity.this.category.getCategoryID());
                }else{
                    initDataFromServer(null, "TaobaoGoods/search", !FileInfo.IsAtUserString(getFileKey(),TBrebackListActivity.this), para, listView, TBrebackListActivity.this.category.getCategoryID());
                }
            }
        }, 1);
    }
    @Override
    protected String getFileKey() {
        if(isJD){
            return "TBrebackListActivity" + keyWords+"JD" ;
        }
        return "TBrebackListActivity" + keyWords ;
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
        AsynServer.wantShowDialog = false;
        myAdapter.clearData();
        addSecond = true;
    }

    private void cb(Object obj, String s) {
        callBack(obj, s);
    }

    CustomProgressDialog customProgressDialog;//打开淘宝客户端时对话框
    public void showQuan(String goods_name, String url){
        if (!TaoBao.hasInstallation()) {
            Intent intent = new Intent();
            intent.putExtra(TBDetailActivity.GoodsName,goods_name);
            intent.putExtra(TBDetailActivity.Item,url);
            intent.putExtra(TBDetailActivity.Type,1);
            AddActivity(TBDetailActivity.class,0,intent);
        } else {
            customProgressDialog = CustomProgressDialog.createDialog(TBrebackListActivity.this,R.style.CustomProgressDialog);
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
    }

    @Override
    protected void publish() {
        showList(keyWords, category);
    }

    public void callBack(Object json_data, String catID) {
        JSONObject sender = (JSONObject) json_data;
        String keyWords = JsonClass.sj(sender,"keywords");
        et_search.setText(keyWords);
        ArrayList<Object> data = new ArrayList<>();
        categoryArrayList = new ArrayList<>();
        if (sender.has("cat_list") && !isSecondItemClass) {
            JSONArray jrrSelectItem = JsonClass.jrrj(sender, "cat_list");
            for (int i = 0 ; i < jrrSelectItem.length() ; i ++) {
                JSONObject json = JsonClass.jjrr(jrrSelectItem,i);
                categoryArrayList.add(new Category(JsonClass.sj(json,"cat_id"),JsonClass.sj(json,"cat_name"),JsonClass.sj(json,"parent_id")));
            }
            initSelectItemLayout(jrrSelectItem,catID);
        }

        if (addSecond) {
            if (sender.has("cat_list_sub")) {
                JSONArray jrr = JsonClass.jrrj(sender, "cat_list_sub");
                if (jrr.length() > 0) {
                    contain_ssiv.setVisibility(View.VISIBLE);
                    ssiv.setInfo(JsonClass.jrrj(sender, "cat_list_sub"), catID);
                } else if (!isSecondItemClass) {
                    contain_ssiv.setVisibility(View.GONE);
                }
            } else if (!isSecondItemClass) {
                contain_ssiv.setVisibility(View.GONE);
            }
            addSecond = false;
        }


        JSONArray jrr_pro = JsonClass.jrrj(sender, "goods_list");
        if (jrr_pro.length() == 0) {
            ndv.setVisibility(View.VISIBLE);
        } else {
            ndv.setVisibility(View.GONE);
            for (int i = 0; i < jrr_pro.length(); i++) {
                data.add(JsonClass.jjrr(jrr_pro, i));
            }
        }
        myAdapter.addData(data);

    }

    @Override
    public void clickCancel() {
        removeFragment();
    }

    /**
     * 添加按钮
     */
    protected void initSelectItemLayout(JSONArray jsonArray, String t) {
        siv_focus.setType(t);
        siv.focus(t.equals(""));
        if (contain_select_layout.getChildCount() > 0) {
            contain_select_layout.removeAllViews();
        }
        arrListSelectItem = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String type = JsonClass.sj(JsonClass.jjrr(jsonArray, i), "cat_id");
            String name = JsonClass.sj(JsonClass.jjrr(jsonArray, i), "cat_name");
            SelectItemView siv = new SelectItemView(this, name, type);
            siv.setOnClickListener(selectItemClickListener());
            siv.focus(t.equals(type));
            arrListSelectItem.add(siv);
            contain_select_layout.addView(siv, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (t.equals(type)) {
                siv_focus.setName(name);
            }
        }

        if (siv_focus.getType().equals("")) {
            siv_focus.setName("全部");
        }
        siv_focus.focus(true);
    }

    //region Action
    protected View.OnClickListener selectItemClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siv.focus(false);
                for (int i = 0; i < arrListSelectItem.size(); i++) {
                    SelectItemView selectItemView = arrListSelectItem.get(i);
                    selectItemView.focus(false);
                }
                ((SelectItemView) v).focus(true);
                category.setCategoryID(((SelectItemView) v).type);
                et_search.setText(((SelectItemView) v).getName());
                siv_focus.setName(((SelectItemView) v).getName());
                siv_focus.setType(((SelectItemView) v).getType());
                siv_focus.focus(true);
                showList(/*et_search.getText().toString()*/((SelectItemView) v).getName(), category);
            }
        };
    }

    //region 选择类别
    private void showAllSelectItem() {
        addFragment();
    }

    private FragmentManager mFragmentManager;

    private void addFragment() {
        RotateAnimation ra = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true);
        ra.setDuration(400);
        lv_more.startAnimation(ra);
        hasFragment = true;
        if (null == mFragmentManager) {
            mFragmentManager = getSupportFragmentManager();
        }

        SelectItemFragment selectItemFragment = new SelectItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SelectItemFragment.SELECT_ITEM_DATA, categoryArrayList);
        bundle.putParcelable(SelectItemFragment.FocusCategory, category);
        bundle.putParcelable(SelectItemFragment.SDD, this);
        selectItemFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.push_top_in, R.anim.push_top_out, R.anim.push_top_in, R.anim.push_top_out);

        fragmentTransaction.add(R.id.lisdv, selectItemFragment);

        // 加入到BackStack中
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void removeFragment() {
        //region ```
        RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true);
        ra.setDuration(400);
        lv_more.startAnimation(ra);
        //endregion
        hasFragment = false;
        if (null == mFragmentManager) {
            mFragmentManager = getSupportFragmentManager();
        }
        mFragmentManager.popBackStack();
    }

    //endregion


    long lastTime = 0L;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_focus:
                long nowTime = SystemClock.currentThreadTimeMillis();
                System.out.print("nowTime = " + nowTime + " lastTime = " + lastTime + " ----- ");
                System.out.println("nowTime - lastTime = " + (nowTime - lastTime));
                if (nowTime - lastTime < 10) {
                    ib_top.callOnClick();
                } else {
                    lastTime = nowTime;
                }

                break;
            case R.id.lv_more:
                //region more
                if (hasFragment) {
                    removeFragment();

                } else {

                    showAllSelectItem();
                }
                //endregion
                break;
            case R.id.ib_top:
                Default.scrollToListviewTop(listView);
                break;
            case R.id.siv:
                siv.focus(true);
                category.setCategoryID( siv.type);
                et_search.setText("");
                for (int i = 0; i < arrListSelectItem.size(); i++) {
                    SelectItemView selectItemView = arrListSelectItem.get(i);
                    selectItemView.focus(false);
                }
                showList(et_search.getText().toString(), category);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            default:
        }
    }

    @Override
    public void clickOK(Category category) {
        isSecondItemClass = false;
        siv.focus(category.getCategoryID().equals(""));
        this.category = category;

        String name = "";
        for (int i = 0; i < arrListSelectItem.size(); i++) {
            SelectItemView selectItemView = arrListSelectItem.get(i);
            if (selectItemView.type.equals(category.getCategoryID())) {
                selectItemView.focus(true);
                name = selectItemView.getName();
                siv_focus.setName(selectItemView.getName());
                siv_focus.setType(category.getCategoryID());
            } else {
                selectItemView.focus(false);
            }
        }
        removeFragment();
        et_search.setText(name);
        showList(/*et_search.getText().toString()*/name, category);
    }

    @Override
    public void secondClick(String cat_id, String name) {
        isSecondItemClass = true;
        category.setCategoryID(cat_id);
        et_search.setText(name);
        siv_focus.setName(name);
        showList(/*et_search.getText().toString()*/name, category);

    }

    //endregion

    @Override
    public void dismissToastComponent() {
        ib_top.setVisibility(View.GONE);
        layout_focus.setVisibility(View.GONE);
    }

    @Override
    public void showToastComponent() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.update_animator_recory);
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

        layout_focus.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrollChanged(int state) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public void showLogin(View v) {
        shouldLoginView = v;
        AddActivity(LoginActivity.class);
    }

    @Override
    public void changeSelectList(String str) {
        TaoBao.searchKeyList(str, new TaoBao.TaoBaoCallBack() {
            @Override
            public void call(final JSONObject json) {
                System.out.println("keyList : " + json.toString());
                TBrebackListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeSelect();
                        JSONArray jrr = JsonClass.jrrj(json,"result");
                        if (jrr.length() > 0) {
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            skf = new EditSelectText.SelectFragmentList();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EditSelectText.SelectFragmentList.Type, EditSelectText.SelectFragmentList.Type_2);
                            bundle.putParcelable("sd", TBrebackListActivity.this);
                            bundle.putString("jrr", JsonClass.jrrj(json, "result").toString());
                            skf.setArguments(bundle);
                            fragmentTransaction.setCustomAnimations(R.anim.push_top_in,0);
                            fragmentTransaction.replace(R.id.contain_fragment, skf);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            hasFragment = true;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void itemClick(String str) {
        et_search.setText(str);
        bt_search.callOnClick();
    }

    @Override
    public void closeSelect() {
        getSupportFragmentManager().popBackStack();
        hasFragment = false;
    }

    @Override
    public void hasFocus(boolean hasFocus) {

    }

    @Override
    public void search(String key) {
        closeSelect();
        if (arrListSelectItem == null ) {
            arrListSelectItem = new ArrayList<>();
        }
        for (int i = 0; i < arrListSelectItem.size(); i++) {
            arrListSelectItem.get(i).focus(false);
        }
        siv.focus(true);
        contain_ssiv.setVisibility(View.GONE);
        showList(key, category);
    }

    @Override
    public void showFilter(String keyWords ,boolean is_TMail) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FilterFragment ff = new FilterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FilterFragment.TBD, this);
        bundle.putBoolean(FilterFragment.Is_Tmail, is_TMail);
        bundle.putString(FilterFragment.KeyWords,keyWords);
        bundle.putString(FilterFragment.Money_start, filterMoneyBack);
        bundle.putString(FilterFragment.Money_end, filterMoneyEnd);
        bundle.putString(FilterFragment.Reback_start, filterStartBack);
        bundle.putString(FilterFragment.Reback_end, filterEndBack);
        bundle.putString(FilterFragment.Sold_method, sort_method);
        bundle.putString(FilterFragment.Sold_order_type, sort_order_type);
        ff.setArguments(bundle);
        ft.setCustomAnimations(R.anim.activity_load_in, R.anim.activity_exit_out,R.anim.activity_load_in, R.anim.activity_exit_out);
        ft.replace(R.id.layout_fragment_filter, ff);
        ft.addToBackStack(null);
        ft.commit();
        hasFragment = true;
    }

    @Override
    public void showFilterList(final String filterStartBack, final String filterEndBack, final String filterMoneyBack, final String filterMoneyEnd, final String sold_method, final String sort_order, final boolean is_TMail, final String keyWords) {
        System.out.println("showFilterList");
        useFilter = !((filterStartBack == null || filterStartBack.length() == 0) &&
                (filterEndBack == null || filterEndBack.length() == 0) &&
                (filterMoneyBack == null || filterMoneyBack.length() == 0) &&
                (filterMoneyEnd == null || filterMoneyEnd.length() == 0));
        this.filterStartBack = filterStartBack;
        this.filterEndBack = filterEndBack;
        this.filterMoneyBack = filterMoneyBack;
        this.filterMoneyEnd = filterMoneyEnd;
        this.sort_method = sold_method;
        this.sort_order_type = sort_order;
        this.is_TMail = is_TMail;
        this.keyWords = keyWords;
        if (useFilter) {
            tv_filter.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.orangered));
            filter_select.setBackground(ContextCompat.getDrawable(TBrebackListActivity.this, R.drawable.filter_orange));
        } else {
            tv_filter.setTextColor(ContextCompat.getColor(TBrebackListActivity.this, R.color.tv_Gray));
            filter_select.setBackground(ContextCompat.getDrawable(TBrebackListActivity.this, R.drawable.filter));
        }
        switch (sort_method) {
            case TBrebackItemDelegate.Sort_Method_Default:
                tv_desc.setText(TBrebackListActivity.this.getString(R.string.filter_com));
                useDesc();
                break;
            case TBrebackItemDelegate.Sort_Method_Number:
                tv_desc.setText("销量排序");
                useDesc();
                break;
            case TBrebackItemDelegate.Sort_Method_Rebate:
                useReback();
                break;
            default:
                noUse();
        }
        initDataFromCache(category.getCategoryID());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONObject para = new JSONObject();
                try {
                    if (!isJD) {
                        para.put("minrebate", filterStartBack);
                        para.put("maxrebate", filterEndBack);
                        para.put("is_tmall", is_TMail ? 1 : 0);
                    }
                    para.put("minprice", filterMoneyBack);
                    para.put("maxprice", filterMoneyEnd);
                    para.put("sort", sold_method);
                    para.put("order", sort_order);
                    para.put("keywords", keyWords);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isJD) {
                    initDataFromServer(null, "JinDong/search", /*!FileInfo.IsAtUserString(getFileKey(),TBrebackListActivity.this)*/true, para, listView, category.getCategoryID());
                } else {
                    initDataFromServer(null, "TaobaoGoods/search", /*!FileInfo.IsAtUserString(getFileKey(),TBrebackListActivity.this)*/true, para, listView, category.getCategoryID());
                }
            }
        }, 1);
    }

    @Override
    protected void onActivityResult(Bundle bundle) {
        super.onActivityResult(bundle);
        //专为登陆量身定制
        if (shouldLoginView != null) {
            shouldLoginView.callOnClick();
            shouldLoginView = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        AsynServer.wantShowDialog = false;
    }

    @Override
    public void shouldLogin(View clickView) {

    }

    private class MyAdapter extends NomalAdapter {
        boolean isJdItem;

        public MyAdapter(Context c, boolean isJDitem) {
            super(c);
            isJdItem = isJDitem;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(isJdItem){
                if (!(convertView instanceof JDrebackListItemView)) {
                    convertView = new JDrebackListItemView(c);
                }
                ((JDrebackListItemView) convertView).setInfo(TBrebackListActivity.this,TBrebackListActivity.this, (JSONObject) data.get(position));
            }else{
                if (!(convertView instanceof TBrebackListItemView)) {
                    convertView = new TBrebackListItemView(c);
                }
                boolean  isQuan = TBrebackListActivity.this.switch_coupons.isChecked();
                ((TBrebackListItemView) convertView).setInfo(TBrebackListActivity.this,TBrebackListActivity.this, (JSONObject) data.get(position), isQuan);
            }
            return convertView;
        }
    }
}
