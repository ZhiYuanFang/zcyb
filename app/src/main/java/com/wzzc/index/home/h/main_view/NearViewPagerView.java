package com.wzzc.index.home.h.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.index.home.h.NearbyDelegate;
import com.wzzc.other_view.SlideView.slidePager.SlidePagerCountView;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/5/11.
 *
 */

public class NearViewPagerView extends BaseView {
    public NearbyDelegate nd;
    @ViewInject(R.id.viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.countView)
    SlidePagerCountView countView;
    @ViewInject(R.id.et_search)
    EditText et_search;
    @ViewInject(R.id.layout_search)
    RelativeLayout layout_search;
    private ArrayList<View> arr_items;
    ViewPagerAdapter viewPagerAdapter;
    public NearViewPagerView(Context context) {
        super(context);
        init();
    }

    public NearViewPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
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
                if (nd != null) {
                    nd.changeSearch(s.toString() , false);
                }
            }
        });
        layout_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        arr_items = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void search () {
       if (nd != null) {
           nd.changeSearch(et_search.getText().toString() , true);
       }
   }
    private class ViewPagerAdapter extends PagerAdapter {

        Context c;
        ArrayList<View> data;

        private ViewPagerAdapter (Context c) {
            this.c = c;
            data = new ArrayList<>();
        }

        public void initData () {
            data = new ArrayList<>();
        }
        public void addData (View view) {
            data.add(view);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(data.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(data.get(position));
            return data.get(position);
        }
    }

    boolean hasInit;
    public void setInfo (JSONArray jrr_cats , String type , int selectPage , String key) throws JSONException {
        if (key != null) {
            et_search.setText(key);
        }
        if (jrr_cats != null && !hasInit) {
            hasInit = true;
            int number = 0;
            int lineNumber = 4;
            for (int i = 0 ; i < jrr_cats.length() ; i += lineNumber * 2) {
                LinearLayout.LayoutParams params_0 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout layout_0 = new LinearLayout(getContext());
                layout_0.setWeightSum(2);
                layout_0.setLayoutParams(params_0);
                layout_0.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
                params_1.setMargins(0,5,0,5);
                params_1.weight = 1;
                LinearLayout layout_1_1 = new LinearLayout(getContext());
                layout_1_1.setWeightSum(lineNumber);
                layout_1_1.setLayoutParams(params_1);
                layout_1_1.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout layout_1_2 = new LinearLayout(getContext());
                layout_1_2.setWeightSum(lineNumber);
                layout_1_2.setLayoutParams(params_1);
                layout_1_2.setOrientation(LinearLayout.HORIZONTAL);

                Integer spaceOfItem = 10;

                for (int j = 0 ; j < lineNumber * 2; j ++) {
                    int index = i + j;
                    LinearLayout layout_2 = new LinearLayout(getContext());
                    LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
                    params_2.weight = 1;
                    params_2.setMarginEnd(spaceOfItem);
                    params_2.setMarginStart(spaceOfItem);
                    layout_2.setOrientation(LinearLayout.VERTICAL);
                    layout_2.setLayoutParams(params_2);
                    if (jrr_cats.length() > index) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
                        params.weight = 0.6f;
                        RelativeLayout layout = new RelativeLayout(getContext());
                        layout.setLayoutParams(params);
                        ExtendImageView eiv = new ExtendImageView(getContext());
                        RelativeLayout.LayoutParams params_eiv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT);
                        eiv.setLayoutParams(params_eiv);
                        eiv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        String url = jrr_cats.getJSONObject(index).getString("cat_pic");
                        eiv.setPath(url);
                        String str_id = jrr_cats.getJSONObject(index).getString("str_id");
                        layout.setGravity(RelativeLayout.CENTER_IN_PARENT);
                        layout.addView(eiv);

                        layout_2.addView(layout);

                        TextView textView = new TextView(getContext());
                        String str_name = jrr_cats.getJSONObject(index).getString("str_name");
                        textView.setText(str_name);
                        textView.setGravity(Gravity.CENTER);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        params1.weight = 1.4F;
                        textView.setLayoutParams(params1);

                        layout_2.addView(textView);
                        layout_2.setTag(str_id);
                        arr_items.add(layout_2);
                        if (str_id.equals(type)){
                            changeItemsColor(layout_2);
                        }
                        layout_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                et_search.setText("");
                                String strID = String.valueOf(v.getTag());
                                if (nd != null) {
                                    nd.clickItem(strID , selectPosition);
                                }
                                changeItemsColor (v) ;
                            }
                        });

                    }

                    if (j < lineNumber) {
                        layout_1_1.addView(layout_2);
                    } else {
                        layout_1_2.addView(layout_2);
                    }

                }

                layout_0.addView(layout_1_1);

                layout_0.addView(layout_1_2);

                viewPagerAdapter.addData(layout_0);
                number ++;
            }

            countView.select_img =  R.drawable.img_android_round21;
            countView.noselect_img = R.drawable.img_android_round22;
            countView.setCount(number);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    selectPosition = position;
                    countView.setIndex(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            viewPager.setCurrentItem(selectPage);
        }
    }

    int selectPosition;
    private void changeItemsColor (View view) {
        String str_rec = (String) view.getTag();
        for (int i  = 0 ; i < arr_items.size() ; i ++) {
            if (arr_items.get(i).getTag().toString().equals(str_rec)) {
                arr_items.get(i).setBackground(ContextCompat.getDrawable(getContext() , R.drawable.bg_circle_red));
            } else {
                arr_items.get(i).setBackground(null);
            }
        }
    }
}
