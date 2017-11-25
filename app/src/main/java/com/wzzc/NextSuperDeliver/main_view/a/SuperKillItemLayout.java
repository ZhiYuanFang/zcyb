package com.wzzc.NextSuperDeliver.main_view.a;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.action.ClickBean;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/8/21.
 * 超值送首页的限量秒杀布局
 */
@ContentView(R.layout.super_kill_item_layout)
public class SuperKillItemLayout extends BaseView{
    SuperKillItemDelegate superKillItemDelegate;
    //region Time
    @ViewInject(R.id.tv_hour)
    TextView tv_hour;
    @ViewInject(R.id.tv_min)
    TextView tv_min;
    @ViewInject(R.id.tv_sec)
    TextView tv_sec;

    boolean hasTimer;//保证只有一次
    //endregion
    //region 图片
    @ViewInject(R.id.ssk_left)
    ExtendImageView ssk_left;
    @ViewInject(R.id.ssk_right_0)
    ExtendImageView ssk_right_0;
    @ViewInject(R.id.ssk_right_1)
    ExtendImageView ssk_right_1;
    @ViewInject(R.id.ssk_right_2)
    ExtendImageView ssk_right_2;
    @ViewInject(R.id.ssk_right_3)
    ExtendImageView ssk_right_3;

    ArrayList<ExtendImageView> imageViewArrayList;
    //endregion

    public SuperKillItemLayout(Context context) {
        super(context);
        init();
    }

    public SuperKillItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init () {
        //region 初始化图片组件
        imageViewArrayList = new ArrayList<ExtendImageView>(){{
            add(ssk_left);
            add(ssk_right_0);
            add(ssk_right_1);
            add(ssk_right_2);
            add(ssk_right_3);
        }};

        for (ExtendImageView eiv : imageViewArrayList) {
            eiv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickBean clickBean = (ClickBean) v.getTag();
                    if (superKillItemDelegate != null) {
                        superKillItemDelegate.itemClick(clickBean);
                    }
                }
            });
        }
        //endregion
    }

    public void setInfo (SuperKillItemDelegate superKillItemDelegate , JSONObject jsonData) {
        this.superKillItemDelegate = superKillItemDelegate;
        showTime(JsonClass.lj(jsonData,"countdown"));
        JSONArray jrr_right = JsonClass.jrrj(jsonData,"superSeckill");
        JSONArray jrr_left = JsonClass.jrrj(jsonData,"superSeckill_right4");
        initImageView(JsonClass.jjrr(jrr_right,0),ssk_left);
        for (int i = 0 ; i < jrr_left.length() ; i ++){
            //region left image
            JSONObject json_left = JsonClass.jjrr(jrr_left,i);
            switch (i){
                case 0:
                    initImageView(json_left,ssk_right_0);
                    break;
                case 1:
                    initImageView(json_left,ssk_right_1);
                    break;
                case 2:
                    initImageView(json_left,ssk_right_2);
                    break;
                case 3:
                    initImageView(json_left,ssk_right_3);
                    break;
                default:
            }
            //endregion
        }
    }

    private void initImageView (JSONObject json , ExtendImageView eiv) {
        String ad_link = JsonClass.sj(json,"ad_link");
        String ad_code = JsonClass.sj(json,"ad_code");
        String data_type = JsonClass.sj(json,"data_type");
        String num_iid = JsonClass.sj(json,"num_iid");
        ClickBean clickBean = new ClickBean(ad_link,ad_code,data_type,num_iid);
        eiv.setTag(clickBean);
        eiv.setPath(ad_code);
    }

    protected void showTime (Long seconds) {
        if (!hasTimer) {
            //region 时间
            Message msg = new Message();
            msg.obj = seconds;
            msg.what = TIMER;
            handler.handleMessage(msg);
            //endregion
            hasTimer = true;
        }
    }

    //region time

    //region Handler
    private static final int TIMER = 1001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER:
                    long seconds = (long) msg.obj;
                    Long[] times = convertSecondsToDate(seconds);
                    String hour = String.valueOf(times[0]);
                    if (hour.length() == 1) {
                        hour = "0" + hour;
                    }
                    tv_hour.setText(hour);
                    String minutes = String.valueOf(times[1]);
                    if (minutes.length() == 1) {
                        minutes = "0" + minutes;
                    }
                    tv_min.setText(minutes);
                    String second = String.valueOf(times[2]);
                    if (second.length() == 1) {
                        second = "0" + second;
                    }
                    tv_sec.setText(second);

                    long csd = convertDateToSeconds(times);
                    if (csd > 0) {
                        Message message = new Message();
                        message.what = TIMER;
                        message.obj = --csd;
                        sendMessageDelayed(message, 1000);//每秒更新一次时间
                    }
                    break;
                default:
            }
            super.handleMessage(msg);
        }
    };
    //endregion

    //region time
    private Long[] convertSecondsToDate(Long seconds) {
        Long[] date = new Long[]{1l, 1l, 1l};
        if (seconds > 0) {
            Double hour_double = Math.floor(seconds / 3600);
            Long hour = hour_double.longValue();/*Long.valueOf(String.valueOf(Math.floor(seconds/3600)));*/
            Double minute_double = Math.floor((seconds - hour * 3600) / 60);
            Long minute = minute_double.longValue();
            Long second = seconds - hour * 3600 - minute * 60;
            date = new Long[]{hour, minute, second};
        } else {
            date = new Long[]{0L, 0L, 0L};
        }
        return date;
    }

    private Long convertDateToSeconds(Long[] date) {
        if (date == null || date.length < 3) {
            return 0L;
        }
        Long hour = date[0];
        Long minute = date[1];
        Long second = date[2];
        return hour * 3600 + minute * 60 + second;
    }

    //endregion
    //endregion
}
