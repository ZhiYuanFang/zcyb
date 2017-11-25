package com.wzzc.new_index.SuperDeliver.main_view.childView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextSuperDeliver.list.Category;
import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.detail.main_view.FlowLayout;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/5/3.
 *
 * 超值送|显示更多选择按钮对话框
 */
@ContentView(R.layout.view_selectitemdialog)
public class SelectItemDialogView extends BaseView {
    public SelectDialogDelegate sdd;
    //region 组件
    @ViewInject(R.id.flowLayout)
    FlowLayout flowLayout;
    @ViewInject(R.id.contain_dialog)
    RelativeLayout contain_dialog;
    //endregion
    ArrayList<TextView> items;
    public SelectItemDialogView(Context context) {
        super(context);
        init();
    }

    public SelectItemDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        contain_dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sdd != null) {
                    sdd.clickCancel();
                }
            }
        });
    }

    public void setInfo (ArrayList<Category> categories, Category focusCategory){
        if (focusCategory == null){
            focusCategory = new Category();
        }
        items = new ArrayList<>();
        for (int i = 0 ; i < categories.size(); i ++) {
                Category category = categories.get(i);
                TextView textView = new TextView(GetBaseActivity().getBaseContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Default.dip2px(65, GetBaseActivity().getBaseContext()), Default.dip2px(30, GetBaseActivity().getBaseContext()));
                textView.setLayoutParams(params);
                textView.setTextSize(Default.sp2px(4, GetBaseActivity().getBaseContext()));
                textView.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Black));
                textView.setGravity(Gravity.CENTER);
                textView.setSingleLine(true);
                textView.setText(category.getCategoryName());
                String type = category.getCategoryID();
                textView.setTag(category);
                textView.setBackgroundResource(R.drawable.border);
                if (type.equals(focusCategory.getCategoryID())) {
                    textView.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                    textView.setBackgroundResource(R.drawable.bg_circle_red);
                }
                textView.setOnClickListener(itemClick);

                RelativeLayout rv = new RelativeLayout(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int space = Default.dip2px(3,getContext());
                rv.setPadding(space,space,space,space);
                rv.setLayoutParams(lp);
                rv.addView(textView);
                items.add(textView);
                flowLayout.addView(rv);
        }
    }

    private OnClickListener itemClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            for (TextView tv : items) {
                tv.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Black));
                tv.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.border));
            }
            ((TextView)v).setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
            v.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_circle_red));
            sdd.clickOK((Category) v.getTag());
        }
    };
}
