package com.wzzc.NextSuperDeliver.search;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.MessageBox;
import com.wzzc.other_view.production.detail.main_view.FlowLayout;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by by Administrator on 2017/8/7.
 *
 */

@ContentView(R.layout.super_deliver_search)
public class SuperDeliverSearch extends BaseActivity{
    public static final String SuperDeliverSearchDelegate = "sdsd";
    @ViewInject(R.id.btn_back)
    RelativeLayout btn_back;
    @ViewInject(R.id.tv_search)
    TextView tv_search;
    @ViewInject(R.id.et_search)
    EditText et_search;
    @ViewInject(R.id.imb_delete)
    ImageButton imb_delete;
    @ViewInject(R.id.flowLayout)
    FlowLayout flowLayout;
    @ViewInject(R.id.iv_cancel)
    ImageView iv_cancel;
    SuperDeliverSearchDelegate searchDelegate;
    LayoutInflater inflater;
    ArrayList<String> searchKeyArrayList;//历史搜索集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackTouch();
        init();
    }

    protected String getFileKey (){
        return "SuperDeliverSearch";
    }

    protected void init () {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey(SuperDeliverSearchDelegate)) {
            searchDelegate = bundle.getParcelable(SuperDeliverSearchDelegate);
        }
        inflater = LayoutInflater.from(this);
        searchKeyArrayList = new ArrayList<>();
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                   tv_search.callOnClick();
                return false;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    iv_cancel.setVisibility(View.VISIBLE);
                } else {
                    iv_cancel.setVisibility(View.GONE);
                }
            }
        });
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchKeyArrayList.contains(et_search.getText().toString())) {
                    addSingleItemToFlowLayout(et_search.getText().toString());
                }
                putKeyToFileString(et_search.getText().toString());
                searchKey(et_search.getText().toString());
            }
        });
        imb_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllSearch(new ClearSearchKeysOK() {
                    @Override
                    public void clearOK() {
                        Default.showToast("清除历史纪录成功");
                        searchKeyArrayList.clear();
                        flowLayout.removeAllViews();
                    }
                });
            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackActivity();
            }
        });
        addSearchItemToFlowLayout(getSearchKeyArrayList());
    }

    //region FlowLayout
    private void addSearchItemToFlowLayout (ArrayList<String> items) {
        flowLayout.removeAllViews();
        for (String item : items) {
            addSingleItemToFlowLayout(item);
        }
    }

    private void addSingleItemToFlowLayout (String item) {
        View v = inflater.inflate(R.layout.search_item_layout,null);
        TextView tv_searchItem = (TextView) v.findViewById(R.id.tv_searchItem);
        tv_searchItem.setText(item);
        tv_searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText(((TextView)v).getText().toString());
                tv_search.callOnClick();
            }
        });
        tv_searchItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                MessageBox.Show(SuperDeliverSearch.this, "删除关键字", "关键字:" + ((TextView) v).getText().toString(), new String[]{"取消", "确定"}, new MessageBox.MessBtnBack() {
                    @Override
                    public void Back(int index) {
                        switch (index){
                            case 0:
                                break;
                            case 1:
                                deleteItemKey(((TextView) v).getText().toString());
                                addSearchItemToFlowLayout(searchKeyArrayList);
                                break;
                            default:
                        }
                    }
                });
                return false;
            }
        });
        flowLayout.addView(v);
    }
    //endregion

    //region Helper
    private ArrayList<String> getSearchKeyArrayList () {
        if (FileInfo.IsAtUserString(getFileKey(),this)) {
            String allKeys = FileInfo.GetUserString(getFileKey(),this);
            Log.v(getFileKey() + "allKeys",allKeys);
            if (allKeys != null && allKeys.length() > 0) {
                List<String> list =   Arrays.asList(allKeys.split(","));
                for (String str : list) {
                    searchKeyArrayList.add(str);
                }
            }
        } else {
            Log.v(getFileKey() + "allKeys","null");
        }
        return searchKeyArrayList;
    }

    private void putKeyToFileString (String keys) {
        if (!searchKeyArrayList.contains(keys)) {
            searchKeyArrayList.add(keys);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : searchKeyArrayList) {
            stringBuilder.append(str);
            stringBuilder.append(",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.subSequence(0,stringBuilder.length() - 1);
        }
        FileInfo.SetUserString(getFileKey(),stringBuilder.toString(),this);
    }

    private void deleteItemKey (String item) {
        if (searchKeyArrayList.contains(item)) {
            searchKeyArrayList.remove(item);
            FileInfo.SetUserString(getFileKey(),searchKeyArrayList.toString(),this);
        }
    }

    private void clearAllSearch (ClearSearchKeysOK clearSearchKeysOK) {
        FileInfo.RemoveStringFromCacheMap(getFileKey());
        SharedPreferences settings = getSharedPreferences("settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        if (settings.contains(getFileKey())) {
            editor.remove(getFileKey());
            editor.apply();
            clearSearchKeysOK.clearOK();
        }
    }
    //endregion

    //region Action
    private void searchKey (String keyWords) {
        if (searchDelegate != null) {
            searchDelegate.search(keyWords);
        }
    }
    //endregion

    interface ClearSearchKeysOK {
        void clearOK();
    }
}
