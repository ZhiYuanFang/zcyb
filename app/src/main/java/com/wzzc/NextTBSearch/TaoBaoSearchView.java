package com.wzzc.NextTBSearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextTBSearch.main_view.TBrebackListActivity;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.editSelect.EditSelectText;
import com.wzzc.other_view.editSelect.SelectDelegate;
import com.wzzc.other_view.production.detail.main_view.FlowLayout;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/7/3.
 */
@ContentView(R.layout.view_taobaosearch)
public class TaoBaoSearchView extends BaseView {
    //region ```
    @ViewInject(R.id.layout_contain)
    RelativeLayout layout_contain;
    @ViewInject(R.id.et_search)
    EditSelectText et_search;
    @ViewInject(R.id.layout_search)
    RelativeLayout layout_search;
    @ViewInject(R.id.flowLayout)
    FlowLayout flowLayout;
    //endregion.

    String highBack;

    public TaoBaoSearchView(Context context) {
        super(context);
        init();
    }

    public TaoBaoSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        layout_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.selectDelegate.closeSelect();
                String keyWords = et_search.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(TBrebackListActivity.KeyWords, keyWords);
                intent.putExtra(TBrebackListActivity.HighBack, highBack);
                GetBaseActivity().AddActivity(TBrebackListActivity.class, 0, intent);
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                layout_search.callOnClick();
                return false;
            }
        });
    }

    public void setInfo(JSONObject json, String searchKey, SelectDelegate selectDelegate) {
        et_search.selectDelegate = selectDelegate;
        if (searchKey != null && searchKey.length() > 0) {
            et_search.setText(searchKey);
        }
        highBack = JsonClass.sj(json, "footer_title");
        JSONArray jrr_keyWords = JsonClass.jrrj(json, "hot_words");
        flowLayout.removeAllViews();
        for (int i = 0; i < jrr_keyWords.length(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_key_taobao, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_keyWords);
            tv.setText(JsonClass.sjrr(jrr_keyWords, i));
            tv.setOnClickListener(itemWordsClick);
            flowLayout.addView(view);
        }
        ImageHelper.handlerImage(getContext(),JsonClass.sj(json, "bg_img"), 67, 167, new ImageHelper.HandlerImage() {
            @Override
            protected void imageBack(Bitmap bitmap) {
                layout_contain.setBackground(new BitmapDrawable(GetBaseActivity().getResources(),bitmap));
            }
        });
    }

    private OnClickListener itemWordsClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String keyWords = (String) ((TextView) v).getText();
            et_search.setText(keyWords);
            layout_search.callOnClick();
        }
    };

    public void setSearchText (String str) {
        et_search.setText(str);
    }
}
