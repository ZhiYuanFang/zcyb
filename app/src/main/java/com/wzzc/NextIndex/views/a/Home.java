package com.wzzc.NextIndex.views.a;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextIndex.NextDelegate;
import com.wzzc.NextIndex.views.a.views.AView;
import com.wzzc.NextIndex.views.a.views.BView;
import com.wzzc.NextIndex.views.a.views.CView;
import com.wzzc.NextIndex.views.a.views.DView;
import com.wzzc.NextIndex.views.a.views.EView;
import com.wzzc.NextIndex.views.a.views.FView;
import com.wzzc.NextIndex.views.a.views.GView;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_view.OverrideScrollView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/8/5.
 * 主页面
 */
@ContentView(R.layout.a_view)
public class Home extends BaseView implements View.OnClickListener , HomeDelegate{
    private NextDelegate nextDelegate;
    private ClickDelegate clickDelegate;
    @ViewInject(R.id.sv)
    OverrideScrollView sv;
    //region 主界面所有楼层
    @ViewInject(R.id.a_view)
    AView a_view;
    @ViewInject(R.id.b_view)
    BView b_view;
    @ViewInject(R.id.c_view)
    CView c_view;//广告位
    @ViewInject(R.id.d_view)
    DView d_view;
    @ViewInject(R.id.e_view)
    EView e_view;
    @ViewInject(R.id.f_view)
    FView f_view;
    @ViewInject(R.id.g_view)
    GView g_view;
    @ViewInject(R.id.h_view)
    CView h_view;//广告位
    //endregion
    //region 悬浮栏
    @ViewInject(R.id.txt_edit)
    private EditText txt_edit;
    @ViewInject(R.id.lab_address)
    private TextView lab_adress;
    @ViewInject(R.id.lab_scan)
    private ImageView lab_scan;
    @ViewInject(R.id.changeColor_layout)
    private RelativeLayout changeColor_layout;
    //endregion
    public Home(Context context) {
        super(context);
        init();
    }

    public Home(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        //region 监听滚动事件，处理悬浮栏变化
        sv.setOnScrollChangeListener(new OverrideScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(OverrideScrollView osv, int x, int y, int oldx, int oldy) {
                changeColor_layout.setAlpha(y/ Default.px2sp(185f,getContext()));
            }
        });
        //endregion
        //region 搜索
        txt_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (nextDelegate != null) {
                    nextDelegate.searchProduction(v.getText().toString());
                }
                return false;
            }
        });
        //endregion
        lab_scan.setOnClickListener(this);
        lab_adress.setOnClickListener(this);
    }

    public void setInfo (NextDelegate nextDelegate, ClickDelegate clickDelegate , JSONObject json) {
        this.nextDelegate = nextDelegate;
        this.clickDelegate = clickDelegate;
        //region pop
        JSONObject json_pop = JsonClass.jj(json, "popup");
        JSONArray jrr_data = JsonClass.jrrj(json_pop, "data");
        if (jrr_data.length() > 0) {
            nextDelegate.showAdvertising(JsonClass.jjrr(jrr_data, 0));
        }
        //endregion
        //region AView
        JSONObject json_a = new JSONObject();
        //region slide
        JSONArray jrr_slide = JsonClass.jrrj(JsonClass.jj(json, "player"), "data");
        try {
            json_a.put(AView.SLID, jrr_slide);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion
        //region item
        JSONArray jrr_item = JsonClass.jrrj(JsonClass.jj(json, "nav"), "data");
        try {
            json_a.put(AView.ITEM, jrr_item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion
        a_view.setInfo(this,clickDelegate,json_a);
        //endregion
        JSONObject json_b = JsonClass.jj(JsonClass.jj(json, "top_best"), "data");
        b_view.setInfo(this,clickDelegate,json_b);
        JSONArray jrr_row1_ads = JsonClass.jrrj(JsonClass.jj(json, "row1_ads"), "data");
        if (jrr_row1_ads.length() > 0) {
            c_view.setInfo(this,clickDelegate,jrr_row1_ads);
            c_view.setVisibility(VISIBLE);
        } else {
            c_view.setVisibility(GONE);
        }
        JSONObject json_d = JsonClass.jj(json, "dailyPreference");
        d_view.setInfo(this,clickDelegate,json_d);
        JSONObject json_e = JsonClass.jj(json, "womensGoods");
        e_view.setInfo(this,clickDelegate,json_e);
        JSONObject json_f = JsonClass.jj(json, "baby");
        f_view.setInfo(this,clickDelegate,json_f);
        JSONObject json_g = JsonClass.jj(json, "best");
        g_view.setInfo(this,clickDelegate,json_g);
        JSONArray jrr_row2_ads = JsonClass.jrrj(JsonClass.jj(json, "row2"), "data");
        if (jrr_row2_ads.length() > 0) {
            h_view.setInfo(this,clickDelegate,jrr_row2_ads);
            h_view.setVisibility(VISIBLE);
        } else {
            h_view.setVisibility(GONE);
        }
    }

    public void setAddress (String address) {
        lab_adress.setText(address);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lab_scan:
                scanForTDCode();
                break;
            case R.id.lab_address:
                changeLocation();
                break;
            default:

        }
    }

    @Override
    public void scanForTDCode() {
        nextDelegate.scanForTDCode();
    }

    @Override
    public void changeLocation() {
        nextDelegate.changeLocation();
    }
}
