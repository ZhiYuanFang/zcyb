package com.wzzc.NextIndex.views.e.other_activity.BusinessShop.contract.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.NextIndex.views.e.other_activity.BusinessShop.contract.ContractActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/22.
 */
public class ContractView extends BaseView {
    @ViewInject(R.id.img_icon)
    private RelativeLayout img_icon;
    @ViewInject(R.id.lab_recommend)
    private TextView lab_recommend;
    @ViewInject(R.id.lab_shop)
    private TextView lab_shop;
    @ViewInject(R.id.lab_state)
    private TextView lab_state;
    @ViewInject(R.id.img_delete)
    private ImageView img_delete;

    private ExtendImageView img_view1;
    private int[] idlist = new int[1];

    public ContractView(Context context) {
        super(context);
        img_view1 = ExtendImageView.Create(img_icon);
    }

    public ContractView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setInfo(JSONObject[] data) {
        try {
            lab_recommend.setText("推荐人： "+data[0].getString("parent_name"));
            lab_shop.setText("推荐店铺："+data[0].getString("shop_name"));
            if (data[0].getInt("status")==1){
                lab_state.setText("状态：已支付");
            }else if (data[0].getInt("status")==2){
                lab_state.setText("状态：已撤销");
            }else {
                lab_state.setText("状态：等待处理");
            }
            img_view1.setPath(data[0].getString("img"));
            idlist[0] = data[0].getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @OnClick({R.id.img_delete})
    public void btn_goto_onclick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        JSONObject para = new JSONObject();
        try {
            para.put("id",idlist[tag]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynServer.BackObject(GetBaseActivity() , "contract/delete", para, new AsynServer.BackObject() {
            @Override
            public void Back(JSONObject sender) {
                GetBaseActivity().AddActivity(ContractActivity.class);
                GetBaseActivity().finish();
            }
        });
    }
}
