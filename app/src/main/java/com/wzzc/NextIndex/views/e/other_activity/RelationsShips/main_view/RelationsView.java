package com.wzzc.NextIndex.views.e.other_activity.RelationsShips.main_view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.NextIndex.views.e.other_activity.RelationsShips.main_view.main_view.RelationsShopDetailActivity;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/13.
 *
 */

public class RelationsView extends BaseView {
    //region ```
    @ViewInject(R.id.tv_name)
    TextView tv_userName;
    @ViewInject(R.id.tv_money)
    TextView tv_backMoney;
    @ViewInject(R.id.tv_more)
    TextView tv_more;
    @ViewInject(R.id.img_go_next)
    ImageView img_next;
    @ViewInject(R.id.basicLayout)
    LinearLayout basicLayout;
    @ViewInject(R.id.layout_contain)
    RelativeLayout layout_contain;
    @ViewInject(R.id.fra_listView)
    ListView fra_listView;
    //endregion
    @ViewInject(R.id.layout_next)
            RelativeLayout layout_next;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    String userID;
    MyAdapter myAdapter;
    public RelationsView(Context context) {
        super(context);
        init();
    }

    public RelationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    boolean hasShowSecond;
    protected void init () {
        layout_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasShowSecond) {
                    animLayout(layout_contain,0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    hasShowSecond = true;
                    toTopAnim();
                } else {
                    animLayout(layout_contain, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                    hasShowSecond = false;
                    toBottomAnim();
                }
            }
        });
        fragmentManager = GetBaseActivity().getSupportFragmentManager();
        tv_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                if (tag != null && tag instanceof String){
                    // TODO: 2017/6/3 去tag的购物详情
                    Intent intent = new Intent();
                    intent.putExtra(RelationsShopDetailActivity.USEID,tag.toString());
                    GetBaseActivity().AddActivity(RelationsShopDetailActivity.class,0,intent);
                }
            }
        });
    }

    public void setInfo (JSONObject data) {
        myAdapter = new MyAdapter();
        fra_listView.setAdapter(myAdapter);
        try {
            userID = data.getString("user_id");
            tv_more.setTag(userID);
            String user_name = data.getString("user_name");
            String total = data.getString("total");
            if (data.has("indirect_users")) {
                JSONArray jrrRelations = data.getJSONArray("indirect_users");
                ArrayList<JSONObject> arr = new ArrayList<>();
                for (int i = 0 ; i < jrrRelations.length() ; i ++) {
                    arr.add(jrrRelations.getJSONObject(i));
                }
                myAdapter.addData(arr);
                Default.fixListViewHeight(fra_listView);
            }
            tv_userName.setText(user_name);
            tv_backMoney.setText(total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLayout_color (@ColorInt int color) {
        basicLayout.setBackgroundColor(color);
    }

    //region anin
    private void animLayout (final View v, int fromHeight , int toHeight) {
        ValueAnimator oa= ObjectAnimator.ofInt(fromHeight,toHeight);
        oa.setDuration(400);
        oa.start();
        oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int f = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams lp = v.getLayoutParams();
                lp.height =  f;
                v.setLayoutParams(lp);
            }
        });
    }

    private void toTopAnim () {
        RotateAnimation r = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setFillAfter(true);
        r.setDuration(300);
        img_next.startAnimation(r);
    }

    private void toBottomAnim() {
        RotateAnimation r = new RotateAnimation(180,360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setFillAfter(true);
        r.setDuration(300);
        img_next.startAnimation(r);
    }
    //endregion

    private class MyAdapter extends BaseAdapter {
        ArrayList <JSONObject> data;
        MyAdapter () {
            data = new ArrayList<>();
        }
        public void addData (ArrayList<JSONObject> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new RelationsView(getContext());
            }
            /*if (position %2 == 0) {
                ((RelationsView)convertView).setLayout_color(Color.WHITE);
            } else {
                ((RelationsView)convertView).setLayout_color(Color.parseColor("#f4f4f4"));
            }*/
            ((RelationsView)convertView).setInfo(data.get(position));
            return convertView;
        }
    }
}
