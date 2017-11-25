package com.wzzc.other_view.editSelect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wzzc.other_function.JsonClass;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;

/**
 * Created by by Administrator on 2017/7/7.
 *
 */

@SuppressLint("AppCompatCustomView")
public class EditSelectText extends EditText {
    public SelectDelegate selectDelegate;

    public EditSelectText(Context context) {
        super(context);
        init();
    }

    public EditSelectText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("selectDelegate : " + selectDelegate);
                System.out.println(" onTextChanged --- start : " + start + " before : " + before + " count : " + count);
                if (bool  &&  selectDelegate != null) {
                    selectDelegate.changeSelectList(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        System.out.println("focus : " + focused);
        bool = focused;
        if (selectDelegate != null) {
            selectDelegate.hasFocus(focused);
        }
    }

    boolean bool;
    @Override
    public void setText(CharSequence text, BufferType type) {
        clearFocus();
        super.setText(text, type);
        if (selectDelegate != null) {
            selectDelegate.closeSelect();
        }
        Selection.setSelection(getText(), text.length());
    }

    public static class SelectFragmentList extends Fragment {
        public static final int Type_1 = 1001;//淘宝返利搜索首页
        public static final int Type_2 = 1002;//淘宝返利搜索列表页
        public static final int Type_3 = 1003;
        public static final int Type_4 = 1004;
        public static final String Type = "type";
        private Bundle bundle;
        private int type;
        private MyAdapter_Search myAdapter_search;
        private SelectDelegate selectDelegate;
        private ListView searchListView;
        private TextView searchClose;
        private LinearLayout layout_bg;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            bundle = getArguments();
            type = bundle.getInt(Type);
            View v;
            switch (type) {
                case Type_1:
                    selectDelegate = (SelectDelegate) bundle.getParcelable("sd");
                    myAdapter_search = new MyAdapter_Search(getActivity(), selectDelegate);
                    v = LayoutInflater.from(getContext()).inflate(R.layout.layout_searchkey, null);
                    searchListView = (ListView) v.findViewById(R.id.search_listView);
                    searchListView.setAdapter(myAdapter_search);
                    searchClose = (TextView) v.findViewById(R.id.search_close);
                    searchClose.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectDelegate.closeSelect();
                        }
                    });
                    layout_bg = (LinearLayout) v.findViewById(R.id.layout_bg);
                    layout_bg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_trangle_solid_orange));
                    break;
                case Type_2:
                    selectDelegate = (SelectDelegate) bundle.getParcelable("sd");
                    myAdapter_search = new MyAdapter_Search(getActivity(), selectDelegate);
                    v = LayoutInflater.from(getContext()).inflate(R.layout.layout_searchkey, null);
                    searchListView = (ListView) v.findViewById(R.id.search_listView);
                    searchListView.setAdapter(myAdapter_search);
                    searchClose = (TextView) v.findViewById(R.id.search_close);
                    searchClose.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectDelegate.closeSelect();
                        }
                    });
                    layout_bg = (LinearLayout) v.findViewById(R.id.layout_bg);
                    layout_bg.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_trangle_solid_gray));
                    break;
                default:
                    v = LayoutInflater.from(getContext()).inflate(R.layout.layout_fragment_dialog_select, null);
            }
            return v;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            switch (type) {
                case Type_1: {
                    String str_jrr = bundle.getString("jrr");
                    try {
                        JSONArray jrr = new JSONArray(str_jrr);
                        myAdapter_search.setData(jrr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case Type_2: {
                    String str_jrr = bundle.getString("jrr");
                    try {
                        JSONArray jrr = new JSONArray(str_jrr);
                        myAdapter_search.setData(jrr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default:
            }
        }
    }


    private static class MyAdapter_Search extends BaseAdapter {
        SelectDelegate selectDelegate;
        JSONArray jrr;
        Context c;

        MyAdapter_Search(Context c, SelectDelegate selectDelegate) {
            this.selectDelegate = selectDelegate;
            this.c = c;
            jrr = new JSONArray();
        }

        public void setData(JSONArray jrr) {
            this.jrr = jrr;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return jrr.length();
        }

        @Override
        public JSONArray getItem(int position) {
            return JsonClass.jrrjrr(jrr, position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.layout_searchkey_item, null);
                vh = new ViewHolder();
                vh.tv_search_item = (TextView) convertView.findViewById(R.id.tv_search_item);
                vh.tv_search_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (selectDelegate != null) {
                            selectDelegate.itemClick(((TextView) v).getText().toString());
                        }
                    }
                });
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv_search_item.setText(JsonClass.sjrr(getItem(position), 0));
            return convertView;
        }

        class ViewHolder {
            TextView tv_search_item;
        }
    }

    private static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Log.d("el", "get status bar height fail");
            e1.printStackTrace();
            return 75;
        }
    }
}
