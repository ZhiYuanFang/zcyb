package com.wzzc.index.classification;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.production.detail.main_view.FlowLayout;
import com.wzzc.other_view.progressDialog.CustomProgressDialogView;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/7/22.
 *
 */

public class ClassifyItemView extends BaseView {
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.flowLayout)
    FlowLayout flowLayout;

    public ClassifyItemView(Context context) {
        super(context);
        init();
    }

    public ClassifyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {

    }

    public void setInfo(Classify classify) {
        if (classify != null) {
            flowLayout.removeAllViews();
            flowLayout.addView(new CustomProgressDialogView(getContext()));
            tv_title.setText(classify.name);
            tv_title.setOnClickListener(ItemClick.listener(null,ItemClick.EXCHANGE_CATEGORY,classify.id,null));

            ArrayList<Classify> classifies = new ArrayList<>();
            if (classify.cat_id != null && classify.cat_id.length() > 0) {
                flowLayout.setVisibility(VISIBLE);
                for (int i = 0 ; i < classify.cat_id.length() ; i ++) {
                    classifies.add(new Classify(JsonClass.sj(JsonClass.jjrr(classify.cat_id,i),"id"),JsonClass.sj(JsonClass.jjrr(classify.cat_id,i),"name"),JsonClass.jrrj(JsonClass.jjrr(classify.cat_id,i),"cat_id")));
                }
            } else {
                flowLayout.setVisibility(GONE);
            }
            setFlowInfo(classifies);
        } else {
            Default.showToast("NULL !");
        }
    }

    public void setFlowInfo(ArrayList<Classify> arrayList) {
        flowLayout.removeAllViews();
        for (int i = 0; i < arrayList.size(); i++) {int space = Default.dip2px(6, GetBaseActivity().getBaseContext());
            RelativeLayout layout = new RelativeLayout(getContext());
            layout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setPadding(space, space/2, space, space/2);
            TextView textView = new TextView(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setPadding(space, space/2, space, space/2);
            textView.setTextSize(Default.sp2px(4, GetBaseActivity().getBaseContext()));
            layout.setBackgroundResource(R.drawable.border);
            textView.setTextColor(Color.parseColor("#FF3E3E3E"));
            textView.setGravity(Gravity.CENTER);
            textView.setText(arrayList.get(i).name);
            layout.setOnClickListener(ItemClick.listener(null,ItemClick.EXCHANGE_CATEGORY,arrayList.get(i).id,null));
            layout.addView(textView);
            flowLayout.addView(layout);
        }
    }
}
