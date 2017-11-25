package com.wzzc.index.classification;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.NoDataView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/7/22.
 *
 */

public class ClassifyActivity extends NewBaseActivity implements ClassifyDelegate {
    public static final String CategoryAll = "全部分类";
    Classify currentFirstClassify;
    IndexAdapter indexAdapter;
    ItemAdapter itemAdapter;
    @ViewInject(R.id.lv_index)
    ListView lv_index;
    @ViewInject(R.id.lv_item)
    ListView lv_item;
    @ViewInject(R.id.noDataView)
    NoDataView noDataView;
    @Override
    protected void init() {
        noDataView.setVisibility(View.GONE);
        indexAdapter = new IndexAdapter(this);
        itemAdapter = new ItemAdapter(this);
        lv_index.setAdapter(indexAdapter);
        lv_item.setAdapter(itemAdapter);
        loadSelectIndex();
    }

    @Override
    protected String getFileKey() {
        return "ClassifyActivity";
    }

    @Override
    protected CacheCallBack cacheCallBack() {
        return new CacheCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj, s);
            }
        };
    }

    @Override
    protected ServerCallBack serverCallBack() {
        return new ServerCallBack() {
            @Override
            public void callBack(Object obj, String s) {
                cb(obj, s);
            }
        };
    }

    private void cb(Object obj, String s) {
        switch (s) {
            case CategoryAll: {
                JSONArray data = (JSONArray) obj;
                ArrayList<Object> classifies = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    classifies.add(new Classify(JsonClass.sj(JsonClass.jjrr(data, i), "id"), JsonClass.sj(JsonClass.jjrr(data, i), "name"),JsonClass.jrrj(JsonClass.jjrr(data, i), "cat_id")));
                }
                indexAdapter.clearData();
                indexAdapter.addData(classifies);
                if (currentFirstClassify != null) {
                    indexAdapter.flushItem(currentFirstClassify.id);
                }
                break;
            }
            default:
        }
    }

    @Override
    protected void publish() {
        loadSelectIndex();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadSelectIndex() {
        if (currentFirstClassify == null) {
            currentFirstClassify = new Classify("1", "男装",new JSONArray());
        }
        initDataFromCache(CategoryAll);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initDataFromServer(null, "categoryAll", !FileInfo.IsAtUserString(getFileKey(),ClassifyActivity.this), new JSONObject(), null, CategoryAll);
            }
        }, 1);
    }

    public void showSelect(Classify classify) {
        JSONArray data = classify.cat_id;
        ArrayList<Object> classifies = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            classifies.add(new Classify(JsonClass.sj(JsonClass.jjrr(data, i), "id"), JsonClass.sj(JsonClass.jjrr(data, i), "name"),JsonClass.jrrj(JsonClass.jjrr(data, i), "cat_id")));
        }
        itemAdapter.clearData();
        itemAdapter.addData(classifies);
        if (classifies.size() == 0) {
            noDataView.setVisibility(View.VISIBLE);
        } else {
            noDataView.setVisibility(View.GONE);
        }
    }

    private class IndexAdapter extends NomalAdapter {

        IndexAdapter(Context c) {
            super(c);
        }

        void flushItem(String id) {
            for (int i = 0; i < data.size(); i++) {
                Classify classify = (Classify) data.get(i);
                classify.focus = classify.id.equals(id);
                if (classify.id.equals(id)) {
                    showSelect(classify);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView tv = new TextView(c);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tv.setTextColor(ContextCompat.getColor(c, R.color.tv_Black));
                tv.setTextSize(Default.dip2px(5f, c));
                int space = Default.dip2px(9f, c);
                tv.setPadding(0, space, 0, space);
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(lp);
                tv.setBackground(ContextCompat.getDrawable(c, R.drawable.button_pressed_gray_pink));
                convertView = tv;
            }
            Classify classify = (Classify) getItem(position);
            if (classify.focus) {
                convertView.setBackground(ContextCompat.getDrawable(c, R.drawable.button_pressed_pink_gray));
            } else {
                convertView.setBackground(ContextCompat.getDrawable(c, R.drawable.button_pressed_gray_pink));
            }
            ((TextView) convertView).setText(classify.name);
            convertView.setTag(classify.id);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = (String) v.getTag();
                    flushItem(id);
                }
            });
            return convertView;
        }
    }

    private class ItemAdapter extends NomalAdapter {

        ItemAdapter(Context c) {
            super(c);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ClassifyItemView(c);
            }
            ((ClassifyItemView) convertView).setInfo((Classify) getItem(position));
            return convertView;
        }
    }

}
