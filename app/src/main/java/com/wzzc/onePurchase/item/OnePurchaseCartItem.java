package com.wzzc.onePurchase.item;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.onePurchase.OnePurchaseShoppingCarDelegate;
import com.wzzc.onePurchase.action.LayoutTouchListener;
import com.wzzc.onePurchase.childview.info.OnePurchaseShoppingCarInfoView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/15.
 *
 * 购物车
 */

public class OnePurchaseCartItem extends RelativeLayout{
    private Integer HEIGHT_IMG;
    private Integer HEIGHT_DELETE,WIDTH_DELETE;
    public OnePurchaseShoppingCarDelegate onePurchaseShoppingCarDelegate;
    private boolean hasInit;
    private String goods_id;
    private String imgPath ;
    private String productionName , price;
    private Integer remaindNumber;
    ExtendImageView eiv_production; OnePurchaseShoppingCarInfoView onePurchaseShoppingCarInfoView;
    public OnePurchaseCartItem(Context context) {
        super(context);
        init();
    }
    public OnePurchaseCartItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void setInfo (String goods_id , String imgPath , String productionName ,
                         Integer remaindNumber , String price ,OnePurchaseShoppingCarDelegate onePurchaseShoppingCarDelegate ) {
        this.onePurchaseShoppingCarDelegate = onePurchaseShoppingCarDelegate;
        this.goods_id = goods_id;
        this.imgPath = imgPath;
        this.productionName = productionName;
        this.remaindNumber = remaindNumber;
        this.price = price;
        initialized ();
    }

    private void init (){
        setOnTouchListener(new LayoutTouchListener(getContext()));

        HEIGHT_IMG = Default.dip2px(80,getContext());
        HEIGHT_DELETE = HEIGHT_IMG/4;
        WIDTH_DELETE = HEIGHT_DELETE - Default.px2dip(1,getContext());
        Integer space = Default.dip2px(5,getContext());
        LinearLayout linearLayoutHORIZONTALAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        //region set AllParentLayout WEIGHT_SUM = 1
        linearLayoutHORIZONTALAllParent.setPadding(space,space,space,space);
        linearLayoutHORIZONTALAllParent.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHORIZONTALAllParent.setLayoutParams(lp_allParent);
        linearLayoutHORIZONTALAllParent.setWeightSum(1);
        linearLayoutHORIZONTALAllParent.setOnClickListener(comeToDetailForProduction);
        //endregion

        //region Add IMGLayout to allParent WEIGHT = 0 , HEIGHT == WIDTH == HEIGHT_IMG;
        RelativeLayout relativeLayoutIMG = new RelativeLayout(getContext());
        LinearLayout.LayoutParams lp_IMGLayout = new LinearLayout.LayoutParams(HEIGHT_IMG, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayoutIMG.setLayoutParams(lp_IMGLayout);
        relativeLayoutIMG.setPadding(space,0,space,0);
        lp_IMGLayout.weight = 0;
        //region Add ExtendImageView to IMGLayout;
        eiv_production = new ExtendImageView(getContext());
        LayoutParams lp_productionIMG = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
        eiv_production.setLayoutParams(lp_productionIMG);
        eiv_production.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        relativeLayoutIMG.addView(eiv_production);
        //endregion

        linearLayoutHORIZONTALAllParent.addView(relativeLayoutIMG);
        //endregion

        //region Add ProductionInfo to allParent WEIGHT = 1
        onePurchaseShoppingCarInfoView = new OnePurchaseShoppingCarInfoView(getContext());
        LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        onePurchaseShoppingCarInfoView.setLayoutParams(lp_info);
        onePurchaseShoppingCarInfoView.setPadding(space,0,space,20);
        lp_info.weight = 1;
        linearLayoutHORIZONTALAllParent.addView(onePurchaseShoppingCarInfoView);
        //endregion

        //region Add delete to allParent WEIGHT = 0 ; WIDTH = WIDTH_DELETE ;  HEIGHT = HEIGHT_DELETE
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.MATCH_PARENT );
        lp.weight = 0;
        lp.setMarginEnd(space);
        relativeLayout.setLayoutParams(lp);
        relativeLayout.setGravity(Gravity.BOTTOM);
        ImageButton ib_delete = new ImageButton(getContext());
        RelativeLayout.LayoutParams lp_delete = new RelativeLayout.LayoutParams(WIDTH_DELETE , HEIGHT_DELETE);
        ib_delete.setLayoutParams(lp_delete);

        ib_delete.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.btn_delete));
        relativeLayout.addView(ib_delete);
        linearLayoutHORIZONTALAllParent.addView(relativeLayout);

        ib_delete.setOnClickListener(deleteProductionFromCar);
        //endregion


        addView(linearLayoutHORIZONTALAllParent);
    }


    private void initialized () {
        eiv_production.setPath(imgPath);
        onePurchaseShoppingCarInfoView.setInfo(onePurchaseShoppingCarDelegate,goods_id,productionName,remaindNumber,price);

    }

    //region Action
    private OnClickListener comeToDetailForProduction = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/15 进入商品详情页面
            Default.showToast("id" + goods_id, Toast.LENGTH_LONG);

        }
    };

    private OnClickListener deleteProductionFromCar = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/21 删除产品
            Default.showToast("删除产品" + goods_id, Toast.LENGTH_LONG);

        }
    };
    //endregion
}
