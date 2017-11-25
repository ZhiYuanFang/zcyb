package com.wzzc.NextIndex.views.e.other_activity.money.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.other_function.MessageBox;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.money.MoneyActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/4/20.
 * <p>
 * 提现
 */

public class WithdrawalsView extends BaseView implements View.OnClickListener {
    //region 组件
    @ViewInject(R.id.input_money)
    private EditText money;
    @ViewInject(R.id.edit_input)
    private EditText edit_input;
    @ViewInject(R.id.btn_tj)
    private Button btn_tj;
    @ViewInject(R.id.btn_reset)
    private Button btn_reset;
    //endregion
    ArrayList<Button> arrListButton;
    private Double balanceMoney;

    public WithdrawalsView(Context context) {
        super(context);
        init();
    }

    private void init() {
        arrListButton = new ArrayList<Button>() {{
            add(btn_tj);
            add(btn_reset);
        }};

        for (Button bt : arrListButton) {
            bt.setOnClickListener(this);
        }
    }

    public void setInfo(Double balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    @Override
    public void onClick(View v) {
        changeFocusButtonColor((Button) v);
        switch (v.getId()) {
            case R.id.btn_tj: {
                if (money.getText().toString().length() <= 0) {
                    Default.showToast("请输入要提现金额");
                } else if (Double.valueOf(money.getText().toString()) >= balanceMoney) {
                    Default.showToast("当前余额不足，请重新选择提现金额", Toast.LENGTH_LONG);
                } else {
                    JSONObject para = new JSONObject();
                    try {
                        para.put("session", Default.GetSession());
                        para.put("amount", money.getText());
                        para.put("user_note", edit_input.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsynServer.BackObject(getContext(), "account/apply", para, new AsynServer.BackObject() {
                        @Override
                        public void Back(final JSONObject sender) {
                            try {
                                JSONObject status = sender.getJSONObject("status");
                                if (status.getInt("succeed") == 0) {
                                    MessageBox.Show(status.getString("error_code"));
                                } else {
                                    MessageBox.Show(getContext(), Default.AppName, "您的提现申请已成功提交，请等待管理员的审核！", new String[]{"取消", "返回帐户明细列表"}, new MessageBox.MessBtnBack() {
                                        @Override
                                        public void Back(int index) {
                                            switch (index) {
                                                case 0:
                                                    GetBaseActivity().BackActivity();
                                                    break;
                                                case 1:
                                                    ((MoneyActivity) GetBaseActivity()).toView(MoneyActivity.WITHDRAWALS);
                                                    break;
                                                default:
                                            }
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
            }
            case R.id.btn_reset: {

                break;
            }
            default:
        }
    }

    //region Helper
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
