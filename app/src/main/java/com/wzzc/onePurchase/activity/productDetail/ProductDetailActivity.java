package com.wzzc.onePurchase.activity.productDetail;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.productDetail.mainView.ProductDetailTitleLayoutView;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/3/23.
 *
 * 商品详情
 */

public class ProductDetailActivity extends BaseActivity implements ProductionDetailFragmentReSetDelegate {

    @ViewInject(R.id.contain_title)
    private RelativeLayout contain_title;
    @ViewInject(R.id.contain_layout)
    private RelativeLayout contain_layout;


    /**
     * 全部的期（第一期，第二期，第三期，第四期...）
     */
    private ArrayList<String> allTimes;
    /**
     * 全部的期的ID（第一期ID，第二期ID，第三期ID，第四期ID...）
     */
    private ArrayList<String> allTimesID;
    /**
     * 当前是第几期 1----  0对应第几项
     */
    private Integer nowTime;

    private ProductDetailTitleLayoutView productDetailTitleLayoutView;

    private ProductDetailFragment productDetailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allTimes = new ArrayList<>();
        allTimesID = new ArrayList<>();

        productDetailFragment = new ProductDetailFragment(this);
        productDetailTitleLayoutView = new ProductDetailTitleLayoutView(ProductDetailActivity.this);
        contain_title.addView(productDetailTitleLayoutView);

    }

    @Override
    protected void viewFirstLoad() {
        super.viewFirstLoad();
        getServerInfo();
    }

    public void getServerInfo () {
        // TODO: 2017/3/23  获取全部期数 以及当前显示第几期
        //region 假数据
        String url = "http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
        int number = 10;
        for (int i = 0 ; i < number ; i ++) {
            allTimes.add("第"+ (i+1) + "期");
            allTimesID.add("X00" + i);
        }
        nowTime = 2;

        //endregion

        productDetailTitleLayoutView.setInfo(allTimes,allTimesID,nowTime,this);
    }

    @Override
    public void refreshFragment(JSONObject sender) {
       alphaStart(contain_layout,sender);

        if (contain_layout.getChildCount() == 0) {
            contain_layout.addView(productDetailFragment);
        }

    }

    //region Animation
    protected void alphaStart (final View v , final JSONObject sender) {
        AlphaAnimation ani = new AlphaAnimation(1,0.3f);
        ani.setDuration(100);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                productDetailFragment.refreshInfoImage(sender);
                alphaEnd(v);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ani.setFillAfter(true);
        v.startAnimation(ani);
    }

    private void alphaEnd (View v) {
        AlphaAnimation ani = new AlphaAnimation(0.3f,1);
        ani.setDuration(100);
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(ani);
    }

    //endregion

}
