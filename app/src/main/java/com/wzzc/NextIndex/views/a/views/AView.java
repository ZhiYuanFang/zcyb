package com.wzzc.NextIndex.views.a.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextIndex.views.a.HomeDelegate;
import com.wzzc.NextSuperDeliver.SuperDeliver;
import com.wzzc.NextSuperDeliver.list.Category;
import com.wzzc.NextSuperDeliver.list.SuperDeliverList;
import com.wzzc.NextTBSearch.JDrebackActivity;
import com.wzzc.NextTBSearch.TBrebackActivity;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.classification.ClassifyActivity;
import com.wzzc.index.home.a.GcbActivity;
import com.wzzc.index.home.b.ZcbActivity;
import com.wzzc.index.home.c.PrizeActivity;
import com.wzzc.index.home.d.PrizeOneActivity;
import com.wzzc.index.home.h.NewNearbyActivity;
import com.wzzc.other_activity.LoadWebActivity;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.SlideView.SlideDelegate;
import com.wzzc.other_view.SlideView.SlideView;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/26.
 * <p>
 * 幻灯片+分类
 */
@ContentView(R.layout.view_a)
public class AView extends BaseView implements SlideDelegate {
    HomeDelegate homeDelegate;
    ClickDelegate clickDelegate;
    //region ```
    @ViewInject(R.id.slideView)
    SlideView slideView;
    @ViewInject(R.id.layout_contain_nav)
    RelativeLayout layout_contain_nav;
    //endregion
    LayoutInflater layoutInflater;

    public AView(Context context) {
        super(context);
        init();
    }

    public AView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        layoutInflater = LayoutInflater.from(getContext());
    }

    public static final String SLID = "slid";
    public static final String ITEM = "item";

    public void setInfo(HomeDelegate homeDelegate, ClickDelegate clickDelegate ,final JSONObject json) {
        this.homeDelegate = homeDelegate;
        this.clickDelegate = clickDelegate;
        //region 幻灯片
        JSONArray jrr_slid = JsonClass.jrrj(json, SLID);
        slideView.setInfo(this, jrr_slid);
        //endregion
        //region 按钮
        JSONArray jrr_item = JsonClass.jrrj(json, ITEM);
        LinearLayout layout_lin_nav = (LinearLayout) layoutInflater.inflate(R.layout.layout_lin_nav, null);
        layout_lin_nav.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout lin_1 = (LinearLayout) layout_lin_nav.findViewById(R.id.lin_1);
        LinearLayout lin_2 = (LinearLayout) layout_lin_nav.findViewById(R.id.lin_2);
        lin_1.setWeightSum(jrr_item.length() / 2);
        lin_2.setWeightSum(jrr_item.length() / 2);
        for (int i = 0; i < jrr_item.length() / 2; i++) {
            LinearLayout layout_nav = (LinearLayout) layoutInflater.inflate(R.layout.layout_nav, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.weight = 1;
            layout_nav.setLayoutParams(lp);
            ExtendImageView eiv_nav = (ExtendImageView) layout_nav.findViewById(R.id.eiv_icon);
//                    eiv_nav.setScaleType(ImageView.ScaleType.CENTER_CROP);
            TextView tv_nav = (TextView) layout_nav.findViewById(R.id.tv_nav);
            eiv_nav.setPath(JsonClass.sj(JsonClass.jjrr(jrr_item, i), "ad_code"));
            tv_nav.setText(JsonClass.sj(JsonClass.jjrr(jrr_item, i), "ad_title"));
            layout_nav.setTag(JsonClass.sj(JsonClass.jjrr(jrr_item, i), "data_type"));
            layout_nav.setOnClickListener(navClick);
            lin_1.addView(layout_nav);
        }

        for (int i = jrr_item.length() / 2; i < jrr_item.length(); i++) {
            LinearLayout layout_nav = (LinearLayout) layoutInflater.inflate(R.layout.layout_nav, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.weight = 1;
            layout_nav.setLayoutParams(lp);
            ExtendImageView eiv_nav = (ExtendImageView) layout_nav.findViewById(R.id.eiv_icon);
//            eiv_nav.setScaleType(ImageView.ScaleType.CENTER_CROP);
            TextView tv_nav = (TextView) layout_nav.findViewById(R.id.tv_nav);
            eiv_nav.setPath(JsonClass.sj(JsonClass.jjrr(jrr_item, i), "ad_code"));
            tv_nav.setText(JsonClass.sj(JsonClass.jjrr(jrr_item, i), "ad_title"));
            layout_nav.setTag(JsonClass.sj(JsonClass.jjrr(jrr_item, i), "data_type"));
            layout_nav.setOnClickListener(navClick);
            lin_2.addView(layout_nav);
        }

        layout_contain_nav.removeAllViews();
        layout_contain_nav.addView(layout_lin_nav);

        //endregion
    }

    //region ---------------------------入口图标------------------------------
    private static final String SuperDiscount = "SuperDiscount";//超值送
    private static final String TrainTickets = "TrainTickets";//火车票
    private static final String Recharge = "Recharge";//充值中心
    private static final String Stores = "Stores";//吃玩住行
    private static final String FactoryZone = "FactoryZone";//工厂币专区
    private static final String Exchange = "Exchange";//子成币专区
    private static final String Lottery1 = "Lottery1";//工厂币抽奖
    private static final String Lottery2 = "Lottery2";//子成币抽奖
    private static final String TaobaoGoods = "TaobaoGoods";//淘宝返利
    private static final String Category = "Category";//分类
    private static final String SuperDiscountT3 = "SuperDiscountT3";//高反推荐
    private static final String JingDong = "JingDong";  // 京东

    private OnClickListener navClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String data_type = (String) v.getTag();
            switch (data_type) {
                case SuperDiscount:
                    GetBaseActivity().AddActivity(SuperDeliver.class);
//                    GetBaseActivity().AddActivity(SuperDeliverActivity.class);
                    break;
                case TrainTickets: {
                    Intent intent = new Intent();
                    String url = "https://m.ctrip.com/webapp/train/v2/index?from=https%3A%2F%2Fm.ctrip.com%2Fhtml5%2F#!/index";
                    intent.putExtra(LoadWebActivity.URL, url);
                    intent.putExtra(LoadWebActivity.GOODS_NAME, "火车票");
                    intent.putExtra(LoadWebActivity.GOODS_FROM, LoadWebActivity.BROWSE);
                    GetBaseActivity().AddActivity(LoadWebActivity.class, 0, intent);
                    break;
                }
                case Recharge: {
                    Intent intent = new Intent();
                    String url = "http://chong.55.la/";
                    intent.putExtra(LoadWebActivity.URL, url);
                    intent.putExtra(LoadWebActivity.GOODS_NAME, "充值中心");
                    intent.putExtra(LoadWebActivity.GOODS_FROM, LoadWebActivity.BROWSE);
                    GetBaseActivity().AddActivity(LoadWebActivity.class, 0, intent);
                    break;
                }
                case Stores:
                    GetBaseActivity().AddActivity(NewNearbyActivity.class);
                    break;
                case FactoryZone:
                    GetBaseActivity().AddActivity(GcbActivity.class);
                    break;
                case Exchange:
                    GetBaseActivity().AddActivity(ZcbActivity.class);
                    break;
                case Lottery1:
                    GetBaseActivity().AddActivity(PrizeActivity.class);
                    break;
                case Lottery2:
                    GetBaseActivity().AddActivity(PrizeOneActivity.class);
                    break;
                case TaobaoGoods:
                    GetBaseActivity().AddActivity(TBrebackActivity.class);
                    break;
                case JingDong:
                    GetBaseActivity().AddActivity(JDrebackActivity.class);
                    break;
                case Category:
                    GetBaseActivity().AddActivity(ClassifyActivity.class);
                    break;
                case SuperDiscountT3:{
                    Intent intent = new Intent();
                    com.wzzc.NextSuperDeliver.list.Category category = new Category();
                    category.setpType(3);
                    intent.putExtra(SuperDeliverList.CATEGORY,category);
                    GetBaseActivity().AddActivity(SuperDeliverList.class,0,intent);
                    break;}
                default:
                    Default.showToast(data_type + " : " + getContext().getString(R.string.notDevelop));
            }
        }
    };
    //endregion

    @Override
    public void clickItem(Integer clickPosition,View clickView , JSONObject json_item) {
        if (!ItemClick.switchNormalListener(JsonClass.sj(json_item, "data_type"), JsonClass.sj(json_item, "ad_link"))) {
            ItemClick.judgeSpecialListener(clickDelegate,clickView,JsonClass.sj(json_item, "data_type"), JsonClass.sj(json_item, "ad_link"),JsonClass.sj(json_item, "num_iid"));
        }
    }
}
