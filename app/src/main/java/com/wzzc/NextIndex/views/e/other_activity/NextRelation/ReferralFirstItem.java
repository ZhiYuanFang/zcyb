package com.wzzc.NextIndex.views.e.other_activity.NextRelation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.ImageHelper;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/8/16.
 */
@ContentView(R.layout.referral_first_item)
public class ReferralFirstItem extends BaseView{
    RelationDetailItemDelegate relationDetailItemDelegate;
    @ViewInject(R.id.img_head)
    ImageView img_head;
    @ViewInject(R.id.tv_userName)
    TextView tv_userName;
    @ViewInject(R.id.layout_detail)
    RelativeLayout layout_detail;
    @ViewInject(R.id.img_dot)
    ImageView img_dot;

    private int position;//当前item在listView中的位置
    private ReferralPerson referralPerson;//用户
    public ReferralFirstItem(Context context) {
        super(context);
        init();
    }

    public ReferralFirstItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    protected void init () {
        layout_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relationDetailItemDelegate != null) {
                    relationDetailItemDelegate.lookShopDetail(position,referralPerson);
                }
            }
        });
    }

    public void setInfo (RelationDetailItemDelegate relationDetailItemDelegate,ReferralPerson referralPerson , int position) {
        this.relationDetailItemDelegate = relationDetailItemDelegate;
        this.position = position;
        this.referralPerson = referralPerson;
        ImageHelper.handlerImage(getContext(), referralPerson.getHeadUrl(), img_head.getMeasuredWidth(), img_head.getMeasuredHeight(), new ImageHelper.HandlerImage() {
            @Override
            protected void imageBack(Bitmap bitmap) {
                setHead(bitmap);
            }
        });
        tv_userName.setText(referralPerson.getName());
        if (referralPerson.getDirectReferralNumber() > 0) {
            img_dot.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_circle_solid_red));
        } else {
            img_dot.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_circle_solid_gray));
        }
    }

    public void setHead(Bitmap bitmap) {
        if (bitmap == null) {
            img_head.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.headimg));
            return;
        }
        float length = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() / 2 : bitmap.getWidth() / 2;
        Bitmap bmp = null;
        try {
            bmp = ImageHelper.tailorBitmapToCircle(bitmap, new PointF(bitmap.getWidth() / 2, bitmap.getHeight() / 2), length);
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }
        if (bmp == null) {
            img_head.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.headimg));
        } else {
            img_head.setImageBitmap(bmp);
        }
    }
}
