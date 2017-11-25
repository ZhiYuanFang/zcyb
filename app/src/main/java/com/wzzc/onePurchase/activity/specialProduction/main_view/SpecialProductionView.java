package com.wzzc.onePurchase.activity.specialProduction.main_view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseActivity;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.allProduction.OnePurchaseAllProductionActivity;
import com.wzzc.onePurchase.activity.index.main_view.limiteRevealed.LimitedRevealedActivity;
import com.wzzc.onePurchase.item.CommentsItem;
import com.wzzc.onePurchase.item.OnePurchaseLatestAnnouncementItem;
import com.wzzc.onePurchase.item.OnePurchaseShowOrderItem;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/3/30.
 */

public class SpecialProductionView extends BaseView implements View.OnClickListener {
    //region 组件
    @ViewInject(R.id.tv_home)
    private TextView tv_home;
    @ViewInject(R.id.tv_allProduction)
    private TextView tv_allProduction;
    @ViewInject(R.id.tv_lastPublished)
    private TextView tv_lastPublished;
    @ViewInject(R.id.tv_showOrder)
    private TextView tv_showOrder;
    @ViewInject(R.id.tv_shortTime)
    private TextView tv_shortTime;
    @ViewInject(R.id.tv_newsOrder)
    private TextView tv_newsOrder;
    @ViewInject(R.id.tv_line_newsOrder)
    private TextView tv_line_newsOrder;
    @ViewInject(R.id.tv_popularOrder)
    private TextView tv_popularOrder;
    @ViewInject(R.id.tv_line_popularOrder)
    private TextView tv_line_popularOrder;
    @ViewInject(R.id.tv_comment)
    private TextView tv_comment;
    @ViewInject(R.id.tv_line_comment)
    private TextView tv_line_comment;

    @ViewInject(R.id.listView_lastPublished)
    private ListView listView_lastPublished;
    @ViewInject(R.id.listView_showOrder)
    private ListView listView_showOrder;

    @ViewInject(R.id.layout_lastPublished)
    private RelativeLayout layout_lastPublished;
    @ViewInject(R.id.layout_showOrder)
    private RelativeLayout layout_showOrder;

    //endregion

    public static final String PUBLISHED = "published", SHOWORDER = "showOrder";
    public static final Integer ORDER_NEWS = 0, ORDER_POPULAR = 1, ORDER_COMMENT = 2;
    /**
     * 上方组件
     */
    private ArrayList<TextView> arrList_topView;
    /**
     * 晒单组件{名字，下划线}
     */
    private ArrayList<TextView[]> arrList_showOrderView;
    private String currentItem, keyWords;
    private int currentShowOrderItem;

    private ShowOrderAdapter showOrderAdapter;
    private PublishedAdapter publishedAdapter;
    private int pager_now;
    private int count_pager;

    public SpecialProductionView(Context context) {
        super(context);
        init();
    }

    private void init() {

        arrList_topView = new ArrayList<TextView>() {{
            add(tv_home);
            add(tv_allProduction);
            add(tv_lastPublished);
            add(tv_showOrder);
            add(tv_shortTime);

        }};

        arrList_showOrderView = new ArrayList<TextView[]>() {{
            add(new TextView[]{tv_newsOrder, tv_line_newsOrder});
            add(new TextView[]{tv_popularOrder, tv_line_popularOrder});
            add(new TextView[]{tv_comment, tv_line_comment});

        }};

        for (int i = 0; i < arrList_topView.size(); i++) {
            arrList_topView.get(i).setOnClickListener(this);
        }

        for (int i = 0; i < arrList_showOrderView.size(); i++) {
            arrList_showOrderView.get(i)[0].setOnClickListener(showOrderClickListener(i));
        }

    }

    public void setInfo(String currentItem, String keyWords) {
        this.currentItem = currentItem;
        this.keyWords = keyWords;
        initialized();
    }

    private void initialized() {
        resetPager();
        switch (currentItem) {
            case PUBLISHED:
                tv_lastPublished.callOnClick();
                break;
            case SHOWORDER:
                tv_showOrder.callOnClick();
                break;
            default:showPublished();
        }
    }

    //region Method
    private void showPublished() {
        layout_lastPublished.setVisibility(VISIBLE);
        layout_showOrder.setVisibility(GONE);
        getServerInfoForPublished();

    }

    private void showOrder(boolean next) {
        layout_lastPublished.setVisibility(GONE);
        layout_showOrder.setVisibility(VISIBLE);
        getServerInfoForShowOrder(next);
    }
    //endregion

    //region adapter
    private class PublishedAdapter extends BaseAdapter {

        private Context c;
        private ArrayList<JSONObject> data;

        public PublishedAdapter(Context c) {
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData(ArrayList<JSONObject> data) {
            this.data = data;
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
                convertView = new OnePurchaseLatestAnnouncementItem(getContext());
            }
            OnePurchaseLatestAnnouncementItem onePurchaseLatestAnnouncementItem = (OnePurchaseLatestAnnouncementItem) convertView;
            JSONObject json = data.get(position);

            String goods_id = /*json.getString("goods_id")*/"5434";
            String imgPath = /*json.getString("img")*/"http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
            Integer hasEnteredNumber = /*json.getInt("hasEnteredNumber")*/23;
            String announcement_time = /*json.getString("announcement_time")*/"2015/11/30 21:52:52";
            String userIconPath = /*json.getString("userIconPath")*/"http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
            String userName = /*json.getString("userName")*/"JJ";
            String luckyCode = /*json.getString("luckyCode")*/"LK231";

            onePurchaseLatestAnnouncementItem.setInfo(goods_id, imgPath, userIconPath, userName, hasEnteredNumber, luckyCode, announcement_time);

            return convertView;
        }
    }

    private class ShowOrderAdapter extends BaseAdapter {

        private Context c;
        private ArrayList<JSONObject> data;

        public ShowOrderAdapter(Context c) {
            this.c = c;
            data = new ArrayList<>();
        }

        public void addData(ArrayList<JSONObject> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public void clearData() {
            this.data = new ArrayList<>();
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
                convertView = new CommentsItem(getContext());
            }
            CommentsItem commentsItem = (CommentsItem) convertView;
            JSONObject json = data.get(position);

            String rec_id = /*json.getString("rec_id")*/"5434";
            String avatarPath = /*json.getString("avatarPath")*/"http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png";
            String userName = /*json.getString("userName")*/"JJ";
            String showTime = /*json.getString("showTime")*/"2015/11/30 21:52:52";
            String comments_0 = "这里是0阶评论"/*json.getString("comments_0")*/;
            String comments_1 = "这里是1阶评论" /*json.getString("comments_1")*/;
            ArrayList<String> arrayListCommentImages = new ArrayList<>();
            for (int i = 0 ; i < position + 1 ; i ++) {

                arrayListCommentImages.add("http://test.zcgj168.com/data/afficheimg/images/zones/banner01.png");
            }
            commentsItem.setInfo(rec_id, avatarPath,userName,showTime,comments_0,comments_1,arrayListCommentImages);

            return convertView;
        }
    }
    //endregion

    //region Action
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home:
                GetBaseActivity().finish();
                break;
            case R.id.tv_allProduction:
                GetBaseActivity().AddActivity(OnePurchaseAllProductionActivity.class);
                break;
            case R.id.tv_lastPublished:
                changeTopViewFocus((TextView) v);
                showPublished();
                break;
            case R.id.tv_showOrder:
                changeTopViewFocus((TextView) v);
                tv_newsOrder.callOnClick();
                break;
            case R.id.tv_shortTime:
                // 限时抢购
                GetBaseActivity().AddActivity(LimitedRevealedActivity.class);
                break;
            default:
        }
    }

    private void changeTopViewFocus (TextView tv) {
        for (int i = 0 ; i < arrList_topView.size() ; i ++) {
            arrList_topView.get(i).setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
        }
        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
    }
    protected OnClickListener showOrderClickListener(final int index) {
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {

                for (int i = 0 ; i < arrList_showOrderView.size() ; i ++) {
                    arrList_showOrderView.get(i)[0].setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Gray));
                    arrList_showOrderView.get(i)[1].setTextColor(ContextCompat.getColor(getContext(),R.color.tv_line));
                }

                arrList_showOrderView.get(index)[0].setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));
                arrList_showOrderView.get(index)[1].setTextColor(ContextCompat.getColor(getContext(),R.color.tv_Red));

                switch (v.getId()) {
                    case R.id.tv_newsOrder:
                        if (currentShowOrderItem == ORDER_NEWS) {
                            showOrder(true);
                        } else showOrder(false);

                        currentShowOrderItem = ORDER_NEWS;

                        break;
                    case R.id.tv_popularOrder:
                        if (currentShowOrderItem == ORDER_POPULAR) {
                            showOrder(true);
                        } else showOrder(false);

                        currentShowOrderItem = ORDER_POPULAR;

                        break;
                    case R.id.tv_comment:
                        if (currentShowOrderItem == ORDER_COMMENT) {
                            showOrder(true);
                        } else showOrder(false);

                        currentShowOrderItem = ORDER_COMMENT;

                        break;
                    default:
                }
            }
        };
        return listener;
    }
    //endregion

    private void resetPager() {
        pager_now = 0;
        count_pager = 0;
        publishedAdapter = new PublishedAdapter(getContext());
        listView_lastPublished.setAdapter(publishedAdapter);
        showOrderAdapter = new ShowOrderAdapter(getContext());
        listView_showOrder.setAdapter(showOrderAdapter);
    }

    //region 访问服务器
    private void getServerInfoForShowOrder(boolean next) {
        //访问服务器
        if (!next) {
            showOrderAdapter.clearData();

        }

        // TODO: 2017/3/30 访问服务器 currentShowOrderItem , keyWords
        ArrayList<JSONObject> data = new ArrayList<>();
        //region 假数据
        for (int i = 0; i < 10; i++) {
            JSONObject json = new JSONObject();
            data.add(json);
        }
        //endregion

        showOrderAdapter.addData(data);
    }

    private void getServerInfoForPublished() {
        ArrayList<JSONObject> data = new ArrayList<>();
        //region 假数据
        for (int i = 0; i < 10; i++) {
            JSONObject json = new JSONObject();
            data.add(json);
        }
        //endregion

        publishedAdapter.addData(data);
    }
    //endregion
}
