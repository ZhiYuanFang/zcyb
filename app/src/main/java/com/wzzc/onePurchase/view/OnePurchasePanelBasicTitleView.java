package com.wzzc.onePurchase.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/22.
 */

public class OnePurchasePanelBasicTitleView extends BaseView {

    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.btn_back)
    private ImageButton btn_back;
    @ViewInject(R.id.btn_home)
    private ImageButton btn_home;
    private String title;

    public OnePurchasePanelBasicTitleView(Context context) {
        super(context);
        init();
    }

    public OnePurchasePanelBasicTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init (AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.OnePurchasePanelBasicTitleElement);
        title = t.getString(R.styleable.OnePurchasePanelBasicTitleElement_title);
        init();
        t.recycle();

    }

    private void init () {
        btn_back.setOnClickListener(backClick);
        btn_home.setOnClickListener(goHome);
        if (title == null) {
            title = "";
        }
        setTv_title(title);
    }

    public void setTv_title (String title ) {
        this.title = title;
        tv_title.setText(title);
    }

    //region Action
    private OnClickListener backClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            GetBaseActivity().BackActivity();
        }
    };

    private OnClickListener goHome = new OnClickListener() {
        @Override
        public void onClick(View v) {
            GetBaseActivity().BackActivity(0,null);
        }
    };
    //endregion
}
