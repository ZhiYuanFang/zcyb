package com.wzzc.onePurchase.activity.index.main_view.center.activity.beginnerGuide.main_view;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.onePurchase.activity.index.main_view.center.activity.beginnerGuide.BeginnerGuideActivity;
import com.wzzc.onePurchase.view.AnotherItemLayoutView;
import com.wzzc.zcyb365.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by toutou on 2017/3/27.
 * <p>
 * 新手指南
 */

public class BeginnerGuideView extends BaseView implements View.OnClickListener {

    @ViewInject(R.id.shopping_guides)
    private AnotherItemLayoutView shopping_guides;
    @ViewInject(R.id.frequently_questions)
    private AnotherItemLayoutView frequently_questions;
    @ViewInject(R.id.feedback)
    private AnotherItemLayoutView feedback;
    private FragmentTransaction fragmentTransaction;
    private String str_shopGuide, str_question, str_feedback;
    private String rec_id;

    public BeginnerGuideView(Context context) {
        super(context);
        init();
    }


    private void init() {

        shopping_guides.setOnClickListener(this);
        frequently_questions.setOnClickListener(this);
        feedback.setOnClickListener(this);

    }

    public void setInfo(JSONObject sender) {
        if (sender != null) {
            try {
                rec_id = sender.getString(BeginnerGuideActivity.RECID);
            } catch (JSONException e) {
                e.printStackTrace();
                nullInit();
            }
        } else nullInit();


        initialized();
    }

    private void nullInit() {
        rec_id = "";
    }

    protected void initialized() {
        //region 假数据
        str_shopGuide = getContext().getString(R.string.testLongText_0);
        str_question = getContext().getString(R.string.testLongText_1);
        str_feedback = getContext().getString(R.string.testLongText_2);

        //endregion
    }

    @Override
    public void onClick(View v) {

        AnotherItemLayoutView anotherItemLayoutView = (AnotherItemLayoutView) v;
        TextView arrow = anotherItemLayoutView.getArrowText();
        Integer tag = Integer.valueOf(arrow.getTag().toString());
        if (tag == 0) {
            startRotateAnimation(arrow);
        } else {
            endRotateAnimation(arrow);
        }

        fragmentTransaction = ((Activity) getContext()).getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.shopping_guides: {

                Fragment_shoppingGuides fragment_shoppingGuides_0 = new Fragment_shoppingGuides();
                Bundle bundle = new Bundle();
                bundle.putString(Fragment_shoppingGuides.TEXT, str_shopGuide);
                fragment_shoppingGuides_0.setArguments(bundle);
                fragmentTransaction.add(R.id.rv_shopguids, fragment_shoppingGuides_0);
                break;
            }
            case R.id.frequently_questions: {
                Fragment_shoppingGuides fragment_shoppingGuides_1 = new Fragment_shoppingGuides();
                Bundle bundle = new Bundle();
                bundle.putString(Fragment_shoppingGuides.TEXT, str_question);
                fragment_shoppingGuides_1.setArguments(bundle);
                fragmentTransaction.add(R.id.rv_question, fragment_shoppingGuides_1);
                break;
            }
            case R.id.feedback: {
                Fragment_shoppingGuides fragment_shoppingGuides_2 = new Fragment_shoppingGuides();
                Bundle bundle = new Bundle();
                bundle.putString(Fragment_shoppingGuides.TEXT, str_feedback);
                fragment_shoppingGuides_2.setArguments(bundle);
                fragmentTransaction.add(R.id.rv_feedback, fragment_shoppingGuides_2);
                break;
            }
            default:
                fragmentTransaction.commit();
        }

    }

    //region Animation
    private void startRotateAnimation(final View view) {

        view.setPivotY(view.getMeasuredHeight() / 2);
        view.setPivotX(view.getMeasuredWidth() / 2);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fragmentTransaction.setCustomAnimations(R.animator.shop_guid_load_in, R.animator.shop_guid_load_out);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setTag(1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(rotateAnimation);
    }

    private void endRotateAnimation(final View view) {
        GetBaseActivity().onBackPressed();
        view.setPivotX(view.getMeasuredWidth() / 2);
        view.setPivotY(view.getMeasuredHeight() / 2);
        RotateAnimation rotateAnimation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setTag(0);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(rotateAnimation);
    }
    //endregion
}
