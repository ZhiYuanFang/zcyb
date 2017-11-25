package com.wzzc.other_view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.NextIndex.NextIndex;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/4/1.
 * <p>
 * 大部分页面顶部title
 */
@ContentView(R.layout.view_basictitle)
public class BasicTitleView extends BaseView {
    //region 组件
    @ViewInject(R.id.btn_back)
    private RelativeLayout btn_back;
    @ViewInject(R.id.lab_title)
    private TextView lab_title;
    @ViewInject(R.id.bt_go_home)
    public RelativeLayout bt_go_home;
    @ViewInject(R.id.contain_title)
    public RelativeLayout contain_title;
    //endregion
    private String title;
    private boolean hasBack = true;
    private boolean hasHome = true;

    public BasicTitleView(Context context) {
        super(context);
        init();
    }

    public BasicTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.BasicTitleElement);
        title = t.getString(R.styleable.BasicTitleElement_title);
        hasBack = t.getBoolean(R.styleable.BasicTitleElement_hasBack, true);
        hasHome = t.getBoolean(R.styleable.BasicTitleElement_hasHome, true);
        init();
        t.recycle();
    }

    public void setViewOfTitle(View view) {
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contain_title.addView(view);
    }

    public void setTitleOnClickListener(OnClickListener listener) {
        lab_title.setOnClickListener(listener);
    }

    public void setTitle(String str) {
        System.out.println("setTitle : " + str);
        lab_title.setText(str);
    }

    public void setHomeVisible(int visibility) {
        bt_go_home.setVisibility(visibility);
    }

    public void setBackVisible(int visibility) {
        btn_back.setVisibility(visibility);
    }

    private void init() {
        if (title == null) {
            title = "";
        }
        lab_title.setText(title);

        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GetBaseActivity().onBackPressed();
            }
        });

        bt_go_home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetBaseActivity(), NextIndex.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                GetBaseActivity().startActivity(intent);
            }
        });

        if (!hasBack)
            btn_back.setVisibility(GONE);

        if (!hasHome)
            bt_go_home.setVisibility(GONE);
    }

    public void setGoHomeGone(){
        bt_go_home.setVisibility(GONE);
    }

    public void goTop(){
        Intent intent = new Intent(GetBaseActivity(), NextIndex.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        GetBaseActivity().startActivity(intent);
    }
}
