package com.wzzc.other_function.pay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/4/24.
 *
 * 支付方式
 */

public class PayView extends BaseView{
    //region 组件
    @ViewInject(R.id.radio_zfb)
    private RadioButton radio_zfb;
    @ViewInject(R.id.radio_wx)
    private RadioButton radio_wx;
    @ViewInject(R.id.tv_zfb)
    private TextView tv_zfb;
    @ViewInject(R.id.tv_wx)
    private TextView tv_wx;
    @ViewInject(R.id.eiv_zfb)
    private ExtendImageView eiv_zfb;
    @ViewInject(R.id.eiv_wx)
    private ExtendImageView eiv_wx;
    @ViewInject(R.id.layout_paySelect)
    private RelativeLayout layout_paySelect;
    @ViewInject(R.id.contain_view)
            private LinearLayout contain_view;
    //endregion
    int payment_id;
    public PayView(Context context) {
        super(context);
        init();
    }

    public PayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {

    }

    public void setInfo (boolean hasOrder,Integer payMentID) {
        if (hasOrder) {
            layout_paySelect.setVisibility(View.GONE);
        } else {
            layout_paySelect.setVisibility(View.VISIBLE);
        }

        if (payMentID!=null) {
            payment_id = payMentID;
        }
    }

    public int getPayment_id () {
        if (radio_zfb.isChecked()) {
            //支付宝支付
            payment_id = 1;
        } else if (radio_wx.isChecked()) {
            //微信支付
            payment_id = 3;
        }
        return payment_id;
    }
}
