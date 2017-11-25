package com.wzzc.NextSuperDeliver.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextIndex.views.e.User;
import com.wzzc.NextSuperDeliver.Production;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/4/29.
 * <p>
 * 超值送列表页item
 */
@ContentView(R.layout.view_superdeliverlistitem)
public class SuperDeliverListItemView extends BaseView {
    public ClickDelegate clickDelegate;
    public SuperDeliverListItemDelegate superDeliverListItemDelegate;
    //region 组件
    @ViewInject(R.id.img_icon)
    ImageView img_icon;
    @ViewInject(R.id.pro_layout)
    private ExtendImageView pro_layout;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_money)
    private TextView tv_money;
    @ViewInject(R.id.tv_category)
    private TextView tv_info;
    @ViewInject(R.id.tv_earn)
    private TextView tv_earn;
    @ViewInject(R.id.tv_shop)
    private TextView tv_shop;
    @ViewInject(R.id.layout_coupon)
    RelativeLayout layout_coupon;
    @ViewInject(R.id.tv_coupon_info)
    TextView tv_coupon_info;
    //endregion

    public SuperDeliverListItemView(Context context) {
        super(context);
        init();
    }

    public SuperDeliverListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        layout_coupon.setOnClickListener(buyClick);
        tv_name.setOnClickListener(buyClick);
        pro_layout.setOnClickListener(buyClick);
        tv_shop.setOnClickListener(buyClick);
    }

    private OnClickListener buyClick = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            Production production = (Production) v.getTag();
            if (production.is_best()) {
                if (production.isShow_coupon()) {
                    superDeliverListItemDelegate.getCoupons(production);
                } else {
                    ItemClick.judgeSpecialListener(clickDelegate, v, ItemClick.SuperDiscount_goods, production.getGoods_id(), production.getNum_iid());
                }
            } else
                showTaoBaoSearchActivityDialog(clickDelegate, v, ItemClick.SuperDiscount_goods, production);

        }
    };

    protected void showTaoBaoSearchActivityDialog(final ClickDelegate clickDelegate, final View currentView, final String data_type, final Production production) {
        if (!User.is_activate() && User.isShowActivity(Default.getActivity()) && User.getActivity_msg().length() > 0) {
            MessageBox.Show(getContext(), Default.AppName, User.getActivity_msg(), new String[]{User.getActivity_btn_goActivity(), User.getActivity_check_text(),
                    User.getActivity_btn_justBuy()}, new MessageBox.MessBtnBack() {
                @Override
                public void Back(int index) {
                    switch (index) {
                        case 0:
                            ItemClick.switchNormalListener(ItemClick.SuperDiscount_list_3, null);
                            break;
                        case 1:
                            User.setShowActivity(Default.getActivity(), false);
                            break;
                        case 2:
                            if (production.isShow_coupon()) {
                                superDeliverListItemDelegate.getCoupons(production);
                            } else {
                                ItemClick.judgeSpecialListener(clickDelegate, currentView, data_type, production.getGoods_id(), production.getNum_iid());
                            }

                            break;
                        default:
                    }
                }
            });
        } else {
            if (production.isShow_coupon()) {
                superDeliverListItemDelegate.getCoupons(production);
            } else {
                ItemClick.judgeSpecialListener(clickDelegate, currentView, data_type, production.getGoods_id(), production.getNum_iid());
            }
        }
    }

    public void setInfo(ClickDelegate clickDelegate, final SuperDeliverListItemDelegate superDeliverListItemDelegate, final Production production, Category category) {
        this.clickDelegate = clickDelegate;
        this.superDeliverListItemDelegate = superDeliverListItemDelegate;
        switch (category.getpType()) {
//            case 1:
//                break;
            case 2:
                img_icon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.super_kill));
                break;
            case 3:
                img_icon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.high_back));
                break;
//            case 4:
//                break;
            default:
                img_icon.setVisibility(GONE);
        }
        if (production.is_best()) {
            img_icon.setVisibility(VISIBLE);
            img_icon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.high_back));
        }
        if (production.isShow_coupon()) {
            //有优惠券
            layout_coupon.setVisibility(VISIBLE);
            tv_coupon_info.setText(production.getCoupon_info());
            RelativeLayout.LayoutParams lp = (LayoutParams) tv_coupon_info.getLayoutParams();
            lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            tv_coupon_info.setLayoutParams(lp);
        } else {
            layout_coupon.setVisibility(GONE);
        }
        pro_layout.radio = GetBaseActivity().getResources().getDimension(R.dimen.RoundRadio);
        pro_layout.setPath(production.getGoods_thumb());

        final int size = Default.dip2px(10, getContext());
        ImageHelper.handlerImage(getContext(), production.getIcon(), size, size, new ImageHelper.HandlerImage() {
            @Override
            protected void imageBack(Bitmap bitmap) {
                SpannableString spanString = new SpannableString("图 " + production.getGoods_name());
                spanString.setSpan(new ImageSpan(getContext(), bitmap), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_name.setText(spanString);
            }
        });
        tv_money.setText("￥" + production.getShop_price());
        tv_earn.setText(production.getRebate_text());
        tv_info.setText(production.getCoupon_info());
        layout_coupon.setTag(production);
        tv_shop.setTag(production);
        tv_name.setTag(production);
        pro_layout.setTag(production);
    }
}
