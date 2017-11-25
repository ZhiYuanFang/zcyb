package com.wzzc.NextIndex.views.e.other_activity.backmoney.main_view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.taobao.TBDetailActivity;
import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.taobao.TaoBao;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/5/18.
 */

public class BackMoneyView extends BaseView implements View.OnClickListener {
    //    private static final String HAS_CLOSED = "1";
//    private static final String IS_DOING = "0";
    //region ```
    @ViewInject(R.id.tv_state_back_order)
    TextView tv_state;
    @ViewInject(R.id.img_production)
    ExtendImageView img_production;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_cancel)
    TextView tv_cancel;
    @ViewInject(R.id.tv_fillin)
    TextView tv_fillin;
    @ViewInject(R.id.tv_order_now)
    TextView tv_order_now;
    //endregion
    private String order_id;
    private String goods_id;
    private String taobaoke_order_id;
    private String goods_from;
    String prompt;
    String shop_price;
    public BackMoneyView(Context context) {
        super(context);
        init();
    }

    public BackMoneyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        tv_cancel.setOnClickListener(this);
        tv_fillin.setOnClickListener(this);
        tv_order_now.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (goods_from){
                    case "淘宝":{
                        //region tb
                        Intent intent = new Intent();
                        intent.putExtra(TBDetailActivity.Type, 0);
                        intent.putExtra(TBDetailActivity.Item, taobaoke_order_id);
                        intent.putExtra(TBDetailActivity.GoodsName, tv_name.getText().toString());
                        if (Default.useApp) {
                            if (TaoBao.hasInstallation()) {
                                TaoBao.showItemDetailPage(taobaoke_order_id, new AlibcTradeCallback() {
                                    @Override
                                    public void onTradeSuccess(TradeResult tradeResult) {
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Default.showToast(s);
                                    }
                                });
                            } else {
                                GetBaseActivity().AddActivity(TBDetailActivity.class, 0, intent);
                            }
                        } else {
                            GetBaseActivity().AddActivity(TBDetailActivity.class, 0, intent);
                        }
                        //endregion
                        break;}
                    default://region tb
                        Intent intent = new Intent();
                        intent.putExtra(TBDetailActivity.Type, 0);
                        intent.putExtra(TBDetailActivity.Item, taobaoke_order_id);
                        intent.putExtra(TBDetailActivity.GoodsName, tv_name.getText().toString());
                        if (Default.useApp) {
                            if (TaoBao.hasInstallation()) {
                                TaoBao.showItemDetailPage(taobaoke_order_id, new AlibcTradeCallback() {
                                    @Override
                                    public void onTradeSuccess(TradeResult tradeResult) {
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Default.showToast(s);
                                    }
                                });
                            } else {
                                GetBaseActivity().AddActivity(TBDetailActivity.class, 0, intent);
                            }
                        } else {
                            GetBaseActivity().AddActivity(TBDetailActivity.class, 0, intent);
                        }
                        //endregion
                }
            }
        });
        tv_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_order_now.callOnClick();
            }
        });
        img_production.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_order_now.callOnClick();
            }
        });
    }

    public void setInfo(JSONObject sender) {
        goods_from = JsonClass.sj(sender, "goods_from");
        order_id = JsonClass.sj(sender, "id");
        goods_id = JsonClass.sj(sender, "goods_id");
        taobaoke_order_id = JsonClass.sj(sender, "num_iid");
        tv_order_now.setTag(new String[]{goods_id, taobaoke_order_id});
        tv_name.setText(JsonClass.sj(sender, "goods_name"));
        prompt = JsonClass.sj(sender, "prompt_text2");
        shop_price = JsonClass.sj(sender,"shop_price");
//            String status = sender.getString("status");
        if (sender.has("operate_list")) {
            Object operate = JsonClass.oj(sender, "operate_list");
            if (operate instanceof JSONObject) {
                JSONObject json_operate = JsonClass.jj(sender, "operate_list");
                if (json_operate.has("cancel")) {
                    tv_cancel.setText(JsonClass.sj(json_operate, "cancel"));
                    tv_cancel.setVisibility(VISIBLE);
                } else {
                    tv_cancel.setVisibility(GONE);
                }
                if (json_operate.has("gobuy")) {
                    tv_order_now.setText(JsonClass.sj(json_operate, "gobuy"));
                    tv_order_now.setVisibility(VISIBLE);
                } else {
                    tv_order_now.setVisibility(GONE);
                }
                if (json_operate.has("fillin")) {
                    tv_fillin.setText(JsonClass.sj(json_operate, "fillin"));
                    ViewGroup.LayoutParams lp = tv_fillin.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tv_fillin.setLayoutParams(lp);
                } else {
                    ViewGroup.LayoutParams lp = tv_fillin.getLayoutParams();
                    lp.width = 0;
                    tv_fillin.setLayoutParams(lp);
                }
            } else {
                ViewGroup.LayoutParams lp = tv_fillin.getLayoutParams();
                lp.width = 0;
                tv_fillin.setLayoutParams(lp);
                tv_order_now.setVisibility(GONE);
                tv_cancel.setVisibility(GONE);
            }
        } else {
            ViewGroup.LayoutParams lp = tv_fillin.getLayoutParams();
            lp.width = 0;
            tv_fillin.setLayoutParams(lp);
            tv_order_now.setVisibility(GONE);
            tv_cancel.setVisibility(GONE);
        }

        tv_state.setText(JsonClass.sj(sender, "order_status"));
        img_production.setPath(JsonClass.sj(sender, "original_img"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel: {
                //region 取消订单
                MessageBox.Show(getContext(), Default.AppName, "订单一旦撤销将无法复原，确定撤销么？", new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index) {
                            case 0:
                                break;
                            case 1:
                                JSONObject para = new JSONObject();
                                try {
                                    para.put("operate", "cancel");//操作 cancel取消 fillin填写淘宝订单号
                                    para.put("taobaoke_order_id", taobaoke_order_id);
                                    para.put("id", order_id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                AsynServer.BackObject(getContext(), "SuperDiscount/order_operate", true, para, new AsynServer.BackObject() {
                                    @Override
                                    public void Back(JSONObject sender) {
                                        try {
                                            JSONObject status = sender.getJSONObject("status");
                                            int succeed = status.getInt("succeed");
                                            if (succeed == 1) {
                                                Default.showToast("撤销成功", Toast.LENGTH_LONG);
                                                GetBaseActivity().refresh();
                                            } else {
                                                MessageBox.Show(status.getString("error_desc"));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                break;
                        }
                    }
                });
                //endregion
                break;
            }
            case R.id.tv_fillin: {
                //region 填写订单
                final Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
                dialog.show();
                View view = LayoutInflater.from(getContext()).inflate(R.layout.backmoney_dialog_layout, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
                final EditText et_order_number = (EditText) view.findViewById(R.id.et_order_number);
                TextView tv_promot = (TextView) view.findViewById(R.id.tv_promot);
                TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
                TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
                tv_money.setText(shop_price);
                tv_name.setText(BackMoneyView.this.tv_name.getText());
                tv_promot.setText(prompt);
                tv_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String order_number = et_order_number.getText().toString();
                        if (order_number.trim().length() > 0) {
                            dialog.dismiss();
//                            ViewGroup.LayoutParams lp = tv_fillin.getLayoutParams();
//                            lp.width = 0;
//                            tv_fillin.setLayoutParams(lp);
                            JSONObject para = new JSONObject();
                            try {
                                para.put("operate", "fillin");//操作 cancel取消 fillin填写淘宝订单号
                                para.put("taobaoke_order_id", order_number);
                                para.put("id", order_id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AsynServer.BackObject(getContext(), "SuperDiscount/order_operate", true, para, new AsynServer.BackObject() {
                                @Override
                                public void Back(JSONObject sender) {
                                    try {
                                        JSONObject status = sender.getJSONObject("status");
                                        int succeed = status.getInt("succeed");
                                        if (succeed == 1) {
                                            Default.showToast("订单号填写成功", Toast.LENGTH_LONG);
                                        } else {
                                            MessageBox.Show(status.getString("error_desc"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            Default.showToast("请填写订单号", Toast.LENGTH_LONG);
                        }
                    }
                });
                tv_cancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
                Default.getActivity().getWindow().getAttributes().gravity = Gravity.CENTER;
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = Default.dip2px(300, Default.getActivity());
                dialog.getWindow().setAttributes(params);
                //endregion
                break;
            }
            default:
        }
    }
}
