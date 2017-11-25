package com.wzzc.onePurchase.activity.specialProduction.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.specialProduction.SpecialSearchDelegate;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/3/30.
 * <p>
 * 搜索头条
 */

public class SpecialTitleView extends BaseView implements View.OnClickListener {
    @ViewInject(R.id.btn_home)
    private ImageButton ibn_home;
    @ViewInject(R.id.ibn_search)
    private ImageButton ibn_search;
    @ViewInject(R.id.et_searchText)
    private EditText et_searchText;
    @ViewInject(R.id.btn_back)
    private ImageButton btn_back;
    private SpecialSearchDelegate specialSearchDelegate;
    public SpecialTitleView(Context context) {
        super(context);
        init();
    }

    public SpecialTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ibn_home.setOnClickListener(this);
        ibn_search.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        et_searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    turnToProduction();
                }
                return false;
            }
        });
    }

    public void setInfo (SpecialSearchDelegate specialSearchDelegate) {
        this.specialSearchDelegate = specialSearchDelegate;
    }

    protected void turnToProduction() {
        String keyWords = String.valueOf(et_searchText.getText());
        if (specialSearchDelegate != null || keyWords != null) {
            specialSearchDelegate.search(keyWords);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibn_home:
                GetBaseActivity().BackActivity(0,null);
                break;
            case R.id.ibn_search:
                turnToProduction();
                break;
            case R.id.btn_back:
                GetBaseActivity().BackActivity();

                break;
            default:
        }
    }
}
