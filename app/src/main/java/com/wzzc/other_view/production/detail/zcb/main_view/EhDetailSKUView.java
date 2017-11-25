package com.wzzc.other_view.production.detail.zcb.main_view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.other_view.production.detail.main_view.FlowLayout;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zcyb365 on 2016/10/18.
 */
public class EhDetailSKUView extends BaseView implements View.OnClickListener {

    @ViewInject(R.id.tv_name)
    private TextView lab_name;
    @ViewInject(R.id.main_view)
    FlowLayout main_view;
    private ArrayList<SKUButtonView> list_view = new ArrayList<>();

    public EhDetailSKUView(Context context) {
        super(context);
    }

    public void setInfo(JSONObject data) {
        try {
            lab_name.setText(data.getString("name"));
            JSONArray arr = data.getJSONArray("value");
            for (int i = 0; i < arr.length(); i++) {
                SKUButtonView skuButtonView = new SKUButtonView(GetBaseActivity());
                skuButtonView.setInfo(arr.getJSONObject(i));
                main_view.addView(skuButtonView);
                skuButtonView.setOnClickListener(this);
                list_view.add(skuButtonView);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String GetSpec() {
        for (int i = 0; i < list_view.size(); i++) {
            if (list_view.get(i).isSelect()) {
                return list_view.get(i).id;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        SKUButtonView view = (SKUButtonView) v;
        for (int i = 0; i < list_view.size(); i++) {
            list_view.get(i).setSelect(false);
        }
        view.setSelect(true);
    }

    class SKUButtonView extends BaseView {

        @ViewInject(R.id.tv_name)
        private TextView lab_name;
        @ViewInject(R.id.img_icon)
        private ImageView img_icon;
        public String id;

        public SKUButtonView(Context context) {
            super(context);
        }

        public void setInfo(JSONObject data) throws JSONException {
            lab_name.setText(data.getString("label"));
            id = data.getString("id");
        }

        public boolean isSelect() {
            return img_icon.getVisibility() == VISIBLE;
        }

        public void setSelect(Boolean value) {
            if (value) {
                lab_name.setBackgroundResource(R.drawable.sku_button_select);
                img_icon.setVisibility(VISIBLE);
            } else {
                lab_name.setBackgroundResource(R.drawable.sku_button_noselect);
                img_icon.setVisibility(GONE);
            }
        }
    }
}
