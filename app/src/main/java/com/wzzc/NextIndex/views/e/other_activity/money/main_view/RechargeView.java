package com.wzzc.NextIndex.views.e.other_activity.money.main_view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.money.recharge.RechargesActivity;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/4/20.
 * <p>
 * 充值
 */

public class RechargeView extends BaseView implements View.OnClickListener {

    //region 组件
    @ViewInject(R.id.input_money)
    private EditText money;
    @ViewInject(R.id.edit_input)
    private EditText edit_input;
    @ViewInject(R.id.btn_group)
    private RadioGroup radioGroup_pay;
    @ViewInject(R.id.btn_tj)
    private Button btn_tj;
    @ViewInject(R.id.btn_reset)
    private Button btn_reset;
    //endregion
    private RadioButton checkRadioButton;
    ArrayList<Button> arrListButton;

    public RechargeView(Context context) {
        super(context);
        init();
    }

    private void init() {
        radioGroup_pay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //点击事件获取的选择对象
                checkRadioButton = (RadioButton) radioGroup_pay.findViewById(checkedId);
            }
        });
        radioGroup_pay.check(R.id.radio_pay_zfb);

        arrListButton = new ArrayList<Button>() {{
            add(btn_tj);
            add(btn_reset);
        }};

        for (Button bt : arrListButton) {
            bt.setOnClickListener(this);
        }
    }

    public void setInfo() {

    }

    @Override
    public void onClick(View v) {
        changeFocusButtonColor((Button) v);
        switch (v.getId()) {
            case R.id.btn_tj: {
                if (money.getText().length() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra(RechargesActivity.AMOUNT,money.getText());
                    intent.putExtra(RechargesActivity.PAYMENTID,checkRadioButton.getTag().toString());
                    intent.putExtra(RechargesActivity.USERNOTE,edit_input.getText());
                    GetBaseActivity().AddActivity(RechargesActivity.class,0,intent);
                } else {
                    Default.showToast("请输入金额");
                }
                break;
            }
            case R.id.btn_reset: {
                reset();
                break;
            }
            default:
        }
    }

    //region Helper
    private void reset() {
        money.setText("");
        edit_input.setText("");
    }

    private void changeFocusButtonColor(Button bt) {
        for (Button button : arrListButton) {
            button.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Black));
            button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.angel_border_noselect));
        }
        bt.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_White));
        bt.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.angel_border_selected));
    }
    //endregion
}
