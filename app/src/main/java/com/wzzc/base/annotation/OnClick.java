package com.wzzc.base.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mypxq on 16-7-15.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@BaseEvent(type = View.OnClickListener.class, name = "setOnClickListener")
public @interface OnClick {
    int[] value() default {};
}
