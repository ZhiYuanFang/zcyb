package com.wzzc.onePurchase.activity.ShopDetail.mainView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.item.OnePurchaseHasWinedInCloudShoppingItem;
import com.wzzc.onePurchase.item.OnePurchaseWaitingBuyInCloudShoppingItem;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/24.
 *
 * 云购详情
 */

public class ShopDetailMainView extends BaseView {

    @ViewInject(R.id.contain_production)
    private RelativeLayout contain_production;
    @ViewInject(R.id.tv_cloudCoatprofile)
    private TextView tv_cloudCoatprofile;
    @ViewInject(R.id.contain_cloudCode)
    private LinearLayout contain_cloudCode;
    private boolean hasPublished;
    private String goods_id,imgPath,productionName,nowPrice,winnerName,announcement_time;
    private Integer hasEnteredNumber ,  allNeedNumber , remainingEnterNumber;
    private JSONArray jsonArrayCloudCoat;
    public ShopDetailMainView(Context context) {
        super(context);
        init();
    }

    public ShopDetailMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
    }

    public void setInfo(JSONObject sender){
        //region 假数据
        String url = "http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
        goods_id = "G0292";
        imgPath = url;
        productionName = "小妞电动车之恩那个锂电踏板车电动MQ只能锂电车";
        nowPrice = null;
        winnerName = "林俊杰";
        announcement_time = "2016-03-01 20:15:26.255";
        hasEnteredNumber = 100;
        allNeedNumber = 150;
        remainingEnterNumber = 50;
        jsonArrayCloudCoat = new JSONArray();
        hasPublished = true;
        //endregion

        initialized();
    }

    private void initialized (){
        if (hasPublished) {
            //已揭晓
            OnePurchaseHasWinedInCloudShoppingItem onePurchaseHasWinedInCloudShoppingItem = new OnePurchaseHasWinedInCloudShoppingItem(getContext());
            onePurchaseHasWinedInCloudShoppingItem.setInfo(goods_id,imgPath,productionName,nowPrice,winnerName,announcement_time,false);
            contain_production.removeAllViews();
            contain_production.addView(onePurchaseHasWinedInCloudShoppingItem);
        } else {
            //进行中
            OnePurchaseWaitingBuyInCloudShoppingItem onePurchaseWaitingBuyInCloudShoppingItem = new OnePurchaseWaitingBuyInCloudShoppingItem(getContext());
            onePurchaseWaitingBuyInCloudShoppingItem.setInfo(goods_id,imgPath,productionName,nowPrice,hasEnteredNumber,allNeedNumber,remainingEnterNumber,false);
            contain_production.removeAllViews();
            contain_production.addView(onePurchaseWaitingBuyInCloudShoppingItem);
        }

        //region 假数据
        Integer number_cloudCoat = 96;
        //endregion
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString(String.valueOf(number_cloudCoat));
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red)),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBuilder.append("本期商品您总共拥有").append(spannableString).append("个云购码");
        tv_cloudCoatprofile.setText(sBuilder);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("1000012").append(" 10000020");

        TextView tv_0 = getTextViewOfCode();
        tv_0.setText(stringBuffer);
        contain_cloudCode.addView(tv_0);


    }


    private TextView getTextViewOfCode () {
        TextView textView = new TextView(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setPadding(0,0,30,0);
        textView.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        return textView;
    }
}
