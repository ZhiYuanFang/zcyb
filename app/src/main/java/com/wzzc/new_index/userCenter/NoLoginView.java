package com.wzzc.new_index.userCenter;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.LoginActivity;
import com.wzzc.new_index.userCenter.regest.personalAgent.RegisterPersonalAgentActivity;
import com.wzzc.zcyb365.R;

import java.util.Calendar;


/**
 * Created by by Administrator on 2017/5/20.
 *
 */

public class NoLoginView extends BaseView {
    //region 组件
    @ViewInject(R.id.tv_upGrate)
    TextView tv_upGrate;
    @ViewInject(R.id.ib_look)
    ImageButton ib_look;
    @ViewInject(R.id.user_names)
    TextView user_names;
    @ViewInject(R.id.user_money)
    TextView user_money;
    @ViewInject(R.id.layout_usercenter_part)
    LinearLayout layout_usercenter_part;
    @ViewInject(R.id.layout_usercenter_item)
    LinearLayout layout_usercenter_item;
    @ViewInject(R.id.layout_order_state)
    LinearLayout layout_order_state;
    @ViewInject(R.id.tv_date)
    TextView tv_date;
    @ViewInject(R.id.lab_setting)
    TextView lab_setting;
    //endregion
    public NoLoginView(Context context) {
        super(context);
        init();
    }

    public NoLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        setDate();
        user_names.setOnClickListener(goLogin);
        layout_usercenter_part.setOnClickListener(goLogin);
        layout_usercenter_item.setOnClickListener(goLogin);
        lab_setting.setOnClickListener(goLogin);
        layout_order_state.setOnClickListener(goLogin);

        tv_upGrate.setOnClickListener(personAgentListener());

        ib_look.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // region LookMoney
                String password = "*******";
                if (user_money.getText().equals(password)) {
                    user_money.setText("￥0.00");
                    ib_look.setBackgroundResource(R.drawable.usercenter_2);
                } else {
                    user_money.setText(password);
                    ib_look.setBackgroundResource(R.drawable.usercenter_1);
                }
                //endregion
            }
        });

    }

    //region Helper
    private void setDate () {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        tv_date.setText((month +1)+ "月" + day + "日");
    }
    //endregion
    //region Action
    private OnClickListener goLogin = new OnClickListener() {
        @Override
        public void onClick(View v) {
            GetBaseActivity().AddActivity(LoginActivity.class);
        }
    };

    protected OnClickListener personAgentListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(RegisterPersonalAgentActivity.NONE, true);
                GetBaseActivity().AddActivity(RegisterPersonalAgentActivity.class, 0, intent);
            }
        };
    }
    //endregion
}
