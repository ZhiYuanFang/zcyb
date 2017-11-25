package com.wzzc.NextTBSearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.base.new_base.NewBaseActivity;
import com.wzzc.new_index.NomalAdapter;
import com.wzzc.NextTBSearch.main_view.TBrebackListActivity;
import com.wzzc.other_function.FileInfo;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_view.editSelect.EditSelectText;
import com.wzzc.other_view.editSelect.SelectDelegate;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.other_view.popDialog.PopDialog;
import com.wzzc.taobao.TaoBao;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JDrebackActivity extends NewBaseActivity implements SelectDelegate, Parcelable {
    //region ```
    @ViewInject(R.id.btn_back)
    RelativeLayout btn_back;
    @ViewInject(R.id.eiv_back)
    ExtendImageView eiv_back;
    @ViewInject(R.id.eiv_title)
    ExtendImageView eiv_title;
    @ViewInject(R.id.listView)
    ListView listView;
    //endregion
    EditSelectText.SelectFragmentList skf;
    FragmentTransaction fragmentTransaction;
    String highBack;
    String searchKey;
    JDAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        myAdapter = new JDAdapter(this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                    case 1:
                        break;
                    default:
                        showImage(position);
                }
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeSelect();
                return false;
            }
        });
        btn_back.setOnClickListener(this);
        initDataFromCache(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initDataFromServer(null, "JinDong/index",!FileInfo.IsAtUserString(getFileKey(),JDrebackActivity.this), new JSONObject(), null, null);
            }
        }, 1);
    }

    @Override
    protected String getFileKey() {
        return "JDrebackActivity";
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
        JSONObject json = (JSONObject) obj;
        highBack = JsonClass.sj(json, "footer_title");
        eiv_back.setPath(JsonClass.sj(json, "arrow_img"));
        eiv_title.setPath(JsonClass.sj(json, "title_img"));
        ArrayList<Object> data = new ArrayList<>();
        data.add(json);
        JSONArray jrr_help = JsonClass.jrrj(json, "help_imgs");
        for (int i = 0; i < jrr_help.length(); i++) {
            data.add(JsonClass.sjrr(jrr_help, i));
        }
        myAdapter.addData(data);
    }

    @Override
    protected void newServerDataFromServer(JSONObject sender, String s) {
        super.newServerDataFromServer(sender, s);
        myAdapter.clearData();
    }

    @Override
    protected void publish() {
        initDataFromCache(null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initDataFromServer(null, "JinDong/index",!FileInfo.IsAtUserString(getFileKey(),JDrebackActivity.this), new JSONObject(), null, null);
            }
        }, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            default:
        }
    }

    @Override
    public void changeSelectList(String str) {
        searchKey = str;
//        listView.setSelectionFromTop(0, -Default.dip2px(154, this));
        TaoBao.searchKeyList(str, new TaoBao.TaoBaoCallBack() {
            @Override
            public void call(final JSONObject json) {
                System.out.println("keyList : " + json.toString());
                JDrebackActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeSelect();
                        JSONArray jrr = JsonClass.jrrj(json, "result");
                        if (jrr.length() > 0) {
                            fragmentTransaction = JDrebackActivity.this.getSupportFragmentManager().beginTransaction();
                            skf = new EditSelectText.SelectFragmentList();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EditSelectText.SelectFragmentList.Type, EditSelectText.SelectFragmentList.Type_1);
                            bundle.putParcelable("sd", JDrebackActivity.this);
                            bundle.putString("jrr", JsonClass.jrrj(json, "result").toString());
                            skf.setArguments(bundle);
                            fragmentTransaction.setCustomAnimations(R.anim.push_top_in, 0);
                            fragmentTransaction.replace(R.id.contain_fragment, skf);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            hasFragment = true;
                        }
                    }
                });

            }
        });
    }

    @Override
    public void itemClick(String str) {
        jingDongSearchView.setSearchText(str);
        Intent intent = new Intent();
        intent.putExtra(TBrebackListActivity.KeyWords, str);
        intent.putExtra(TBrebackListActivity.HighBack, highBack);
        intent.putExtra(TBrebackListActivity.IsJD, true);
        AddActivity(TBrebackListActivity.class, 0, intent);
    }

    @Override
    public void closeSelect() {
        try{
            getSupportFragmentManager().popBackStack();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        hasFragment = false;
    }

    @Override
    public void hasFocus(boolean hasFocus) {
        if (hasFocus)
            listView.setSelectionFromTop(0, -Default.dip2px(154, this));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    JingDongSearchView jingDongSearchView;

    class JDAdapter extends NomalAdapter {
        private static final int SEARCH_VIEW = 1000;
        private static final int TITLE_VIEW = 1001;
        private static final int IMG_VIEW = 1002;
        ViewHolder viewHolder;

        public JDAdapter(Context c) {
            super(c);
            arrEiv = new ArrayList<>();
        }

//        public int getCount() {
//            if(0 == data.size())
//                return 0;
//            return 1;
//        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                    return SEARCH_VIEW;
                case 1:
                    return TITLE_VIEW;
                default:
                    return IMG_VIEW;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.titleView = (RelativeLayout) LayoutInflater.from(c).inflate(R.layout.layout_title, null);
                viewHolder.eiv_title = (ExtendImageView) viewHolder.titleView.findViewById(R.id.eiv);
            }
            switch (getItemViewType(position)) {
                case SEARCH_VIEW:
                    if (convertView != null && convertView instanceof JingDongSearchView) {

                    } else {
                        if (jingDongSearchView == null) {
                            jingDongSearchView = new JingDongSearchView(c);
                        }
                        convertView = jingDongSearchView;
                    }
                    ((JingDongSearchView) convertView).setInfo((JSONObject) getItem(position), searchKey, JDrebackActivity.this);
                    break;
                case TITLE_VIEW:
                    if (convertView != null && convertView instanceof RelativeLayout) {

                    } else {
                        convertView = viewHolder.titleView;
                    }
                    viewHolder.eiv_title.setPath((String) getItem(position));
                    break;
                case IMG_VIEW:
                    if (convertView != null && convertView instanceof ExtendImageView) {

                    } else {
                        convertView = new ExtendImageView(c);
                        ((ExtendImageView) convertView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        convertView.setPadding(Default.dip2px(15, c), 0, Default.dip2px(15, c), 0);
                    }
                    AbsListView.LayoutParams lp;
                    switch (position - 2) {
                        case 0: {
                            lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Default.dip2px(305, c));
                            break;
                        }
                        case 1: {
                            lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Default.dip2px(203, c));
                            break;
                        }
                        case 2: {
                            lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Default.dip2px(301, c));
                            break;
                        }
                        case 3: {
                            lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Default.dip2px(307, c));
                            break;
                        }
                        case 4: {
                            lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Default.dip2px(325, c));
                            break;
                        }
                        default:
                            lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Default.dip2px(330, c));
                    }
                    convertView.setLayoutParams(lp);
                    ((ExtendImageView) convertView).setPath((String) getItem(position));
                    addShowImage(position, (String) getItem(position), lp);
                    break;
                default:
            }
            return convertView;
        }

        class ViewHolder {
            RelativeLayout titleView;
            ExtendImageView eiv_title;
        }
    }

    ArrayList<View> arrEiv;
    PopDialog popDialog;

    private void showImage(int position) {
        if (arrEiv == null) {
            return;
        }
        popDialog = new PopDialog(this, R.style.PopDialog);
        popDialog.setInfo(arrEiv, position - 2);
        popDialog.show();
    }

    private void addShowImage(int position, String path, ViewGroup.LayoutParams lp_eiv) {
        boolean canAdd = true;
        if (arrEiv == null) {
            arrEiv = new ArrayList<>();
        }
        for (View v : arrEiv) {
            if (((int) v.getTag()) == position) {
                canAdd = false;
            }
        }
        if (canAdd) {
            RelativeLayout curLayout = new RelativeLayout(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            curLayout.setLayoutParams(lp);
            curLayout.setTag(position);
            curLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.tv_White));
            ExtendImageView eiv = new ExtendImageView(this);
            eiv.setPath(path);
            eiv.setLayoutParams(lp_eiv);
            curLayout.addView(eiv);
            curLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popDialog != null && popDialog.isShowing()) {
                        popDialog.dismiss();
                        listView.setSelection((Integer) v.getTag());
                    }
                }

            });

            RelativeLayout layout = new RelativeLayout(this);
            layout.setTag(position);
            layout.setGravity(Gravity.CENTER);
            layout.addView(curLayout);
            arrEiv.add(layout);
        }
    }
}