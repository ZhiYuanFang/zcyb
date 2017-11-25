package com.wzzc.onePurchase.activity.index.main_view.limiteRevealed.mianView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.item.OnePurchaseIndexProductionWaitingBuyItem;
import com.wzzc.onePurchase.view.OnePurchasePanelOperationButtonLayoutView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/22.
 *
 * 每次访问服务器获取数据进行存储
 *
 * 每次退出则删除数据
 */

public class LimitedRevealedMainView extends BaseView implements View.OnClickListener{

    //region 组件
    @ViewInject(R.id.tv_today_revealed)
    private TextView tv_today_revealed;
    @ViewInject(R.id.tv_morrow_revealed)
    private TextView tv_morrow_revealed;
    @ViewInject(R.id.tv_midTime)
    private TextView tv_midTime;
    @ViewInject(R.id.tv_forTime)
    private TextView tv_forTime;
    @ViewInject(R.id.tv_endTime)
    private TextView tv_endTime;
    @ViewInject(R.id.layout_contain_waitingBuy)
    private LinearLayout layout_contain_waitingBuy;
    @ViewInject(R.id.layout_contain_operationButton)
    private LinearLayout layout_contain_operationButton;
    @ViewInject(R.id.listview_rule)
    private ListView listView_rule;
    //endregion

    String productionName,nowPrice,goods_id,imgPath;
    Long shortTime;
    Integer hasEnteredNumber,allNeedNumber,remainingEnterNumber;
    OnePurchaseIndexProductionWaitingBuyItem onePurchaseIndexProductionWaitingBuyItem;
    OnePurchasePanelOperationButtonLayoutView onePurchasePanelOperationButtonLayoutView;
    private String for_time , mid_time , end_time;
    public LimitedRevealedMainView(Context context) {
        super(context);
        init();
    }

    public LimitedRevealedMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        tv_today_revealed.setOnClickListener(this);
        tv_morrow_revealed.setOnClickListener(this);
        tv_forTime.setOnClickListener(this);
        tv_midTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
    }

    public void setInfo () {

        initialized();
    }

    private void initialized () {
        tv_today_revealed.callOnClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_today_revealed:
                tv_today_revealed.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                tv_morrow_revealed.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                tv_midTime.callOnClick();
                break;
            case R.id.tv_morrow_revealed:
                tv_morrow_revealed.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                tv_today_revealed.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                tv_midTime.callOnClick();
                break;
            case R.id.tv_forTime:
                focusTime(tv_forTime);
                break;
            case R.id.tv_midTime:
                focusTime(tv_midTime);
                setProductionFromServers();
                break;
            case R.id.tv_endTime:
                focusTime(tv_endTime);
                break;
            default:
        }
    }
    //region Method
    private void setProductionFromServers () {
        // TODO: 2017/3/23 访问服务器获取限时揭晓在tv_midTime.getText的数据
        JSONObject sender = new JSONObject();
        //region 假设数据
        try {
            String url = "http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
            sender.put("goods_id","1341");
            sender.put("shortTime",421L);
            sender.put("imgPath",url);
            sender.put("productionName","(第4期)小米（MI）小米手环 防水只能腕带运动睡眠计步器");
            sender.put("nowPrice","$5288.00");
            sender.put("hasEnteredNumber",5200);
            sender.put("allNeedNumber",6300);
            sender.put("remainingEnterNumber",1100);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion

        try {
            goods_id = sender.getString("goods_id");
            shortTime = sender.getLong("shortTime");
            imgPath = sender.getString("imgPath");
            productionName = sender.getString("productionName");
            nowPrice = sender.getString("nowPrice");
            hasEnteredNumber = sender.getInt("hasEnteredNumber");
            allNeedNumber = sender.getInt("allNeedNumber");
            remainingEnterNumber = sender.getInt("remainingEnterNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (layout_contain_waitingBuy.getChildCount() <= 0) {
            onePurchaseIndexProductionWaitingBuyItem = new OnePurchaseIndexProductionWaitingBuyItem(getContext());
            layout_contain_waitingBuy.addView(onePurchaseIndexProductionWaitingBuyItem);
        } else {
            onePurchaseIndexProductionWaitingBuyItem = (OnePurchaseIndexProductionWaitingBuyItem) layout_contain_waitingBuy.getChildAt(0);
        }
        onePurchaseIndexProductionWaitingBuyItem.setTextSize(7);
        onePurchaseIndexProductionWaitingBuyItem.setInfo(goods_id,shortTime,imgPath,productionName,nowPrice,hasEnteredNumber,allNeedNumber,remainingEnterNumber);
        if (layout_contain_operationButton.getChildCount() <= 0) {
            onePurchasePanelOperationButtonLayoutView = new OnePurchasePanelOperationButtonLayoutView(getContext());
            layout_contain_operationButton.addView(onePurchasePanelOperationButtonLayoutView);
        } else {
            onePurchasePanelOperationButtonLayoutView = (OnePurchasePanelOperationButtonLayoutView) layout_contain_operationButton.getChildAt(0);
        }
        onePurchasePanelOperationButtonLayoutView.setInfo(goods_id);

    }
    //endregion

    //region Helper
    private void focusTime (TextView view) {
        tv_forTime.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_poup));
        tv_midTime.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_poup));
        tv_endTime.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.white_poup));
        tv_forTime.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        tv_midTime.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        tv_endTime.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        view.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.red_poup));
        view.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_White));
    }
    //endregion
}
