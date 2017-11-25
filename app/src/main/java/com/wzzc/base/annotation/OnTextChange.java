package com.wzzc.base.annotation;

import android.text.TextWatcher;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mypxq on 16-7-16.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@BaseEvent(type = TextWatcher.class, name = "addTextChangedListener",methodname = "onTextChanged")
public @interface OnTextChange {
    int[] value() default {};
}
