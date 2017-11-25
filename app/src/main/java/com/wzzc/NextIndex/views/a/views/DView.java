package com.wzzc.NextIndex.views.a.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wzzc.NextIndex.views.a.HomeDelegate;
import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.JsonClass;
import com.wzzc.other_function.action.ClickDelegate;
import com.wzzc.other_function.action.ItemClick;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

/**
 * Created by by Administrator on 2017/6/26.
 */

public class DView extends BaseView {

    HomeDelegate homeDelegate;
    ClickDelegate clickDelegate;
    //region ```
    @ViewInject(R.id.tv_hour)
    TextView tv_hour;
    @ViewInject(R.id.tv_min)
    TextView tv_min;
    @ViewInject(R.id.tv_sec)
    TextView tv_sec;
    @ViewInject(R.id.eiv_11)
    ExtendImageView eiv_11;
    @ViewInject(R.id.eiv_12)
    ExtendImageView eiv_12;
    @ViewInject(R.id.eiv_21)
    ExtendImageView eiv_21;
    @ViewInject(R.id.eiv_22)
    ExtendImageView eiv_22;
    @ViewInject(R.id.eiv_0)
    ExtendImageView eiv_0;
    //endregion
    public DView(Context context) {
        super(context);
        init();
    }

    public DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        eiv_11.setOnClickListener(click);
        eiv_12.setOnClickListener(click);
        eiv_21.setOnClickListener(click);
        eiv_22.setOnClickListener(click);

    }
    //region Action
    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] tags = (String[]) v.getTag();
            String da_link = tags[0];
            String data_type = tags[1];
            String num_iid = tags[2];

            if (!ItemClick.switchNormalListener(data_type, da_link)) {
                ItemClick.judgeSpecialListener(clickDelegate,v,data_type,da_link,num_iid);
            }
        }
    };
    //endregion
    public void setInfo(HomeDelegate homeDelegate, ClickDelegate clickDelegate, final JSONObject json) {
        this.homeDelegate = homeDelegate;
        this.clickDelegate = clickDelegate;
        //region ```
        if (!hasTimer) {
            //region 时间
            Long seconds = JsonClass.lj(json,"countdown");
            Message msg = new Message();
            msg.obj = seconds;
            msg.what = TIMER;
            handler.handleMessage(msg);
            //endregion
            hasTimer = true;
        }

        eiv_0.setPath(JsonClass.sj(json,"title_img"));
        eiv_11.setPath(JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),0),"ad_code"));
        eiv_12.setPath(JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),1),"ad_code"));
        eiv_21.setPath(JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),2),"ad_code"));
        eiv_22.setPath(JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),3),"ad_code"));

        eiv_11.setTag(new  String[]{JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),0),"ad_link"),
                JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),0),"data_type"),
                JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),0),"num_iid")});


        eiv_12.setTag(new  String[]{JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),1),"ad_link"),
                JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),1),"data_type"),
                JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),1),"num_iid")});


        eiv_21.setTag(new  String[]{JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),2),"ad_link"),
                JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),2),"data_type"),
                JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),2),"num_iid")});


        eiv_22.setTag(new  String[]{JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),3),"ad_link"),
                JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),3),"data_type"),
                JsonClass.sj(JsonClass.jjrr(JsonClass.jrrj(json,"data"),3),"num_iid")});

        //endregion
    }

    //region Handler
    boolean hasTimer;
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
}
