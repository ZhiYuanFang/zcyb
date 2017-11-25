package com.wzzc.NextIndex.views.e.other_activity.collection.main_view.production;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.other_function.AsynServer.AsynServer;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.OnClick;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.NextIndex.views.e.other_activity.collection.CollectionActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/3/1.
 */

public class CollectionShopView extends BaseView {

    @ViewInject(R.id.tv_name)
    TextView lab_name;
    @ViewInject(R.id.img_shop)
    ExtendImageView img_shop;
    private String rec_id;
    public CollectionShopView(Context context) {
        super(context);
    }

    public void setInfo(JSONObject[] data) {
        try {
            lab_name.setText(data[0].getString("name"));
            img_shop.setPath(data[0].getString("logo"));
            rec_id = data[0].getString("rec_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.tv_goshop,R.id.img_delete})
    public void click (View view) {
        Integer tag = Integer.parseInt(view.getTag().toString());
        switch (tag){
            case 0:
                JSONObject para = new JSONObject();
                try {
                    para.put("rec_id",rec_id);
                    para.put("del_store",1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsynServer.BackObject(GetBaseActivity() , "user/collect/delete", para, new AsynServer.BackObject() {
                    @Override
                    public void Back(JSONObject sender) {
                        GetBaseActivity().AddActivity(CollectionActivity.class);
                        GetBaseActivity().finish();
                    }
                });
                break;
            case 1:
                Default.showToast("Developing..." , Toast.LENGTH_LONG);
                break;
            default:
        }
    }
}
