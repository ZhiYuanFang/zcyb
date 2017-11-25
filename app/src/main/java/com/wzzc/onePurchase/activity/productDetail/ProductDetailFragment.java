package com.wzzc.onePurchase.activity.productDetail;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.onePurchase.activity.productDetail.mainView.ProductDetailShowProductView;
import com.wzzc.onePurchase.activity.productDetail.mainView.SearchForNotPublishedView;
import com.wzzc.onePurchase.childview.info.OnePurchaseWaitingBuyInfo;
import com.wzzc.onePurchase.item.OnePurchaseUserShowOrderItem;
import com.wzzc.onePurchase.view.AnotherItemLayoutView;
import com.wzzc.onePurchase.view.OnePurchasePanelOperationButtonLayoutView;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/3/24.
 */

public class ProductDetailFragment extends RelativeLayout implements ProductionDetailDelegate{

    private RelativeLayout contain_production;
    private RelativeLayout contain_producInfo;
    private RelativeLayout contain_operationButton;
    private LinearLayout contain_anotherItem;
    private RelativeLayout contain_lastWined;
    private RelativeLayout contain_nowWined;


    /** 当前产品ID*/
    private String nowGoodID;
    /** 幸运者晒单数量*/
    private int number_luckyMan;
    /** 评论数量*/
    private int number_comments;
    private ProductDetailShowProductView productDetailShowProductView;
    private OnePurchaseWaitingBuyInfo onePurchaseWaitingBuyInfo;
    private OnePurchasePanelOperationButtonLayoutView onePurchasePanelOperationButtonLayoutView;
    private SearchForNotPublishedView searchForNotPublishedView;
    private AnotherItemLayoutView anotherItemLayoutView_allRecord;
    private AnotherItemLayoutView anotherItemLayoutView_imageMore;
    private AnotherItemLayoutView anotherItemLayoutView_hasSomeLuckyBoy;
    private OnePurchaseUserShowOrderItem onePurchaseUserShowOrderItem;

    View view;
    public ProductDetailFragment(Context context) {
        super(context);
        view =  LayoutInflater.from(context).inflate(R.layout.fragment_productdetail_layout,null);
        init();
        addView(view);
    }


    public void init() {
        contain_production = (RelativeLayout) view.findViewById(R.id.contain_production);
        contain_producInfo = (RelativeLayout) view.findViewById(R.id.contain_producInfo);
        contain_operationButton = (RelativeLayout) view.findViewById(R.id.contain_operationButton);
        contain_anotherItem = (LinearLayout) view.findViewById(R.id.contain_anotherItem);
        contain_lastWined = (RelativeLayout) view.findViewById(R.id.contain_lastWined);
        contain_nowWined = (RelativeLayout) view.findViewById(R.id.contain_nowWined);

        productDetailShowProductView = new ProductDetailShowProductView(getContext());
        onePurchaseWaitingBuyInfo = new OnePurchaseWaitingBuyInfo(getContext());
        onePurchasePanelOperationButtonLayoutView = new OnePurchasePanelOperationButtonLayoutView(getContext());
        searchForNotPublishedView = new SearchForNotPublishedView(getContext());
        onePurchaseUserShowOrderItem = new OnePurchaseUserShowOrderItem(getContext());
        contain_production.addView(productDetailShowProductView);
        contain_producInfo.addView(onePurchaseWaitingBuyInfo);
        //region add another layout
        //所有云购记录
        anotherItemLayoutView_allRecord = new AnotherItemLayoutView(getContext());
        anotherItemLayoutView_allRecord.setOnClickListener(clickListener_allRecords);
        contain_anotherItem.addView(anotherItemLayoutView_allRecord);
        //图文详情
        anotherItemLayoutView_imageMore = new AnotherItemLayoutView(getContext());
        anotherItemLayoutView_imageMore.setOnClickListener(clickListener_imageMore);
        contain_anotherItem.addView(anotherItemLayoutView_imageMore);
        //已有--个幸运者晒单 --条评论
        anotherItemLayoutView_hasSomeLuckyBoy = new AnotherItemLayoutView(getContext());
        anotherItemLayoutView_hasSomeLuckyBoy.setOnClickListener(clickListener_hasSomeLuckyBoy);
        contain_anotherItem.addView(anotherItemLayoutView_hasSomeLuckyBoy);
        //endregion
    }


    public void refreshInfoImage(JSONObject sender) {
        // TODO: 2017/3/23 更新Image界面数据
        /* 产品图片集合*/
        ArrayList<String> productionIconsImage = new ArrayList<>();
        /* 对应产品图片集合的图片的ID*/
        ArrayList<String> productionIconsGoodsID = new ArrayList<>();
        //region 假数据
        String url = "http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
        int number = 10;
        boolean hasPublished = false;
        for (int i = 0 ; i < number ; i ++) {
            productionIconsImage.add(url);
            productionIconsGoodsID.add("P00" + i);
        }
        String rec_id = "X0231";
        String imgPath = url;
        String userName = "林俊杰";
        Integer allEnterBuyNumber = 10000;
        String lucky_code = "OIE2239";
        String announcement_time ="2015/04/24 16:04:56";
        String buyTime = "2015/04/24 13:04:56";
        //endregion

        onePurchaseUserShowOrderItem.setInfo(hasPublished,rec_id,imgPath,userName,allEnterBuyNumber,lucky_code,announcement_time,buyTime);
        if (hasPublished) {
            hasPublishedLayout();
        } else {
            notPublishLayout();
        }

        if (productDetailShowProductView != null) {
            productDetailShowProductView.setInfo(this,productionIconsImage,productionIconsGoodsID);
        }
    }

    @Override
    public void refreshInfoProfile(JSONObject sender) {
        // TODO: 2017/3/24 更新简介数据

        //region 假数据
        Integer number_luckyMan = 23;
        Integer number_comments = 10;
        String goods_id = "P008";
        String productionName = "OPPO R9s";
        String nowPrice="¥ 2799";
        Integer hasEnteredNumber=143;
        Integer allNeedNumber=500;
        Integer remainingEnterNumber=357;
        String publishingTime = "第五期正在进行中...";
        String promotionTimeID = "P002";
        boolean productionHasPublished = false;
        //endregion
        this.nowGoodID = goods_id;
        this.number_luckyMan = number_luckyMan;
        this.number_comments = number_comments;
        onePurchaseWaitingBuyInfo.setInfo(productionHasPublished ,productionName,nowPrice,hasEnteredNumber,allNeedNumber,remainingEnterNumber);


        if (productionHasPublished) {
            searchForNotPublishedView.setInfo(publishingTime,clickListener_toPromotion(promotionTimeID));
        } else {
            onePurchasePanelOperationButtonLayoutView.setInfo(nowGoodID);
        }


        anotherItemLayoutView_allRecord.setInfo("所有云购记录");
        anotherItemLayoutView_imageMore.setInfo("图文详情");
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        SpannableString spa_luckyMan = new SpannableString(String.valueOf(number_luckyMan));
        SpannableString spa_comments = new SpannableString(String.valueOf(number_comments));
        spa_luckyMan.setSpan(redSpan(),0,spa_luckyMan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spa_comments.setSpan(redSpan(),0,spa_luckyMan.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sBuilder.append("已有").append(spa_luckyMan).append("个幸运者晒单 ").append(spa_comments).append("条评论");
        anotherItemLayoutView_hasSomeLuckyBoy.setInfo(sBuilder);


    }

    //region Method
    public void hasPublishedLayout () {
        contain_operationButton.removeAllViews();
        contain_lastWined.removeAllViews();
        contain_nowWined.removeAllViews();
        contain_operationButton.addView(searchForNotPublishedView);
        contain_nowWined.addView(onePurchaseUserShowOrderItem);
    }

    public void notPublishLayout(){
        contain_operationButton.removeAllViews();
        contain_lastWined.removeAllViews();
        contain_nowWined.removeAllViews();
        contain_operationButton.addView(onePurchasePanelOperationButtonLayoutView);
        contain_lastWined.addView(onePurchaseUserShowOrderItem);
    }
    //endregion

    //region Helper
    private ForegroundColorSpan redSpan () {
        return new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.tv_Red));
    }
    //endregion

    //region Action
    private View.OnClickListener clickListener_allRecords = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/24 前往所有云购记录
            Default.showToast("前往所有云购记录", Toast.LENGTH_SHORT);
        }
    };

    private View.OnClickListener clickListener_imageMore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/24 前往图文详情 nowGoodID
            Default.showToast("前往图文详情" + nowGoodID , Toast.LENGTH_SHORT);
        }
    };

    private View.OnClickListener clickListener_hasSomeLuckyBoy = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/24 前往评论界面  nowGoodID
            Default.showToast("前往评论界面" + nowGoodID , Toast.LENGTH_SHORT);
        }
    };

    private OnClickListener clickListener_toPromotion (final String timeID) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/3/24 前往某一期界面
                Default.showToast("前往" + timeID+ "期界面",Toast.LENGTH_SHORT);
            }
        };
    }
    //endregion
}
