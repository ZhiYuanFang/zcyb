package com.wzzc.onePurchase.item;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzzc.base.Default;
import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.onePurchase.action.LayoutTouchListener;
import com.wzzc.onePurchase.childview.info.HorizontalScrollViewForMuchIconInfo;
import com.wzzc.zcyb365.R;

import java.util.ArrayList;

/**
 * Created by toutou on 2017/3/20.
 * <p>
 * 晒单/评论
 * <p>
 * 当使用该item时，需要在载体（listView）外添加scrollerView 同时初始化ListView高度。
 */

public class CommentsItem extends RelativeLayout {

    private Integer ICONWIDTH;
    /**
     * 用户ID
     */
    private String rec_id;
    /**
     * 头像
     */
    private String avatarPath;
    private String userName, showTime;
    private String comments_0, comments_1;
    private ArrayList<String> commentImages;
    private boolean hasInit;
    Integer space;
    ExtendImageView eiv_avatar;
    TextView tv_userName;
    TextView tv_time;
    TextView tv_comment_0;
    TextView tv_comment_1;
    HorizontalScrollViewForMuchIconInfo sv_commentImages;

    public CommentsItem(Context context) {
        super(context);
        init();
    }
    public CommentsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void setInfo(String rec_id, String avatarPath, String userName, String showTime, String comments_0, String comments_1,
                        ArrayList<String> commentImages) {
        this.rec_id = rec_id;
        this.avatarPath = avatarPath;
        this.userName = userName;
        this.showTime = showTime;
        this.comments_0 = comments_0;
        this.comments_1 = comments_1;
        if (commentImages == null) {
            this.commentImages = new ArrayList<>();
        } else {
            this.commentImages = commentImages;
        }
        initialized();
    }

    private void init() {
        setOnTouchListener(new LayoutTouchListener(getContext()));

        ICONWIDTH = Default.dip2px(45, getContext());
        space = Default.dip2px(5, getContext());
        Integer smallTextSize = Default.dip2px(8, getContext());

        LinearLayout linearLayoutHORIZONTALAllParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_allParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //region Set allParentLayout Weight_Sum = 1
        linearLayoutHORIZONTALAllParent.setLayoutParams(lp_allParent);
        linearLayoutHORIZONTALAllParent.setWeightSum(1);
        //endregion

        //region Add ImgLayout to allParent , WEIGHT = 0 , HEIGHT == WIDTH = ICONWIDTH
        RelativeLayout relativeLayoutIMG = new RelativeLayout(getContext());
        LayoutParams lp_avatarLayout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayoutIMG.setLayoutParams(lp_avatarLayout);
        relativeLayoutIMG.setGravity(Gravity.TOP);
        linearLayoutHORIZONTALAllParent.addView(relativeLayoutIMG);

        eiv_avatar = new ExtendImageView(getContext());
        LayoutParams lp_avatar = new LayoutParams(ICONWIDTH, ICONWIDTH);
        eiv_avatar.setLayoutParams(lp_avatar);
        eiv_avatar.setOnClickListener(showUserDetail);
        relativeLayoutIMG.addView(eiv_avatar);
        //endregion

        //region Add InfoLayout to allParent , WEIGHT = 1
        LinearLayout layout_info = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp_info = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp_info.weight = 1;
        layout_info.setLayoutParams(lp_info);
        layout_info.setOrientation(LinearLayout.VERTICAL);

        //region add username & showTime to layout_info
        RelativeLayout layout_uName_Time = new RelativeLayout(getContext());
        LayoutParams lp_uName_Time_layout = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_uName_Time_layout.setMargins(0, 0, 0, space * 2);
        layout_uName_Time.setLayoutParams(lp_uName_Time_layout);
        //region add userName SIZE = Default.dip2px(16,getContext())
        tv_userName = new TextView(getContext());
        LayoutParams lp_userName = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_userName.setLayoutParams(lp_userName);
        tv_userName.setTextSize(smallTextSize * 1.2f);
        tv_userName.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Red));
        tv_userName.setGravity(ALIGN_PARENT_START);
        layout_info.addView(tv_userName);
        //endregion

        //region add time SIZE = Default.dip2px(14,getContext())
        tv_time = new TextView(getContext());
        LayoutParams lp_time = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_time.setLayoutParams(lp_time);
        tv_time.setTextSize(smallTextSize);
        tv_time.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));
        tv_time.setGravity(ALIGN_PARENT_END);
        layout_info.addView(tv_time);
        //endregion

        layout_info.addView(layout_uName_Time);
        //endregion

        //region add comments_0 to layout_info
        tv_comment_0 = new TextView(getContext());
        LayoutParams lp_comment_0 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_comment_0.setMargins(0, space * 2, 0, space);
        tv_comment_0.setLayoutParams(lp_comment_0);
        tv_comment_0.setSingleLine(true);
        tv_comment_0.setPadding(0, 0, space, 0);
        tv_comment_0.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Black));
        tv_comment_0.setTextSize(smallTextSize);
        layout_info.addView(tv_comment_0);
        //endregion

        //region add comments_1 to layout_info
        tv_comment_1 = new TextView(getContext());
        LayoutParams lp_comment_1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_comment_1.setMargins(0, space, 0, space);
        tv_comment_1.setMaxLines(2);
        tv_comment_1.setPadding(0, 0, space, 0);
        tv_comment_1.setTextColor(ContextCompat.getColor(getContext(), R.color.tv_Gray));
        tv_comment_1.setTextSize(smallTextSize);
        layout_info.addView(tv_comment_1);
        //endregion

        //region add commentImages to layout_info
        sv_commentImages = new HorizontalScrollViewForMuchIconInfo(getContext());
        LayoutParams lp_sv_commentImages = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp_sv_commentImages.setMargins(0, space, 0, space);
        sv_commentImages.setLayoutParams(lp_sv_commentImages);

        layout_info.addView(sv_commentImages);
        //endregion

        linearLayoutHORIZONTALAllParent.addView(layout_info);
        //endregion

        addView(linearLayoutHORIZONTALAllParent);

    }

    private void initialized() {
        eiv_avatar.setPath(avatarPath);
        tv_userName.setText(userName);
        tv_time.setText(showTime);
        tv_comment_0.setText(comments_0);
        tv_comment_1.setText(comments_1);
        sv_commentImages.setInfo(commentImages);
    }

    //region Action
    private OnClickListener showUserDetail = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 2017/3/20 进入用户详情页
            Default.showToast("用户id" + rec_id, Toast.LENGTH_LONG);

        }
    };

    //endregion
}
