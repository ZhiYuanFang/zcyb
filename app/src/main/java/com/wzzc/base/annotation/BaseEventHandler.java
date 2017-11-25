package com.wzzc.base.annotation;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by mypxq on 16-7-15.
 */
public class BaseEventHandler implements InvocationHandler {

    private WeakReference<Object> handlerRef;
    private WeakReference<Method> methodRef;
    private Object handler;
    private Method method;
    private String methodname;

    public BaseEventHandler(Method method, Object handler, String methodname) {
        methodRef = new WeakReference<>(method);
        handlerRef = new WeakReference<>(handler);
        this.handler = handler;
        this.method = method;
        this.methodname = methodname;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("".equals(methodname) || method.getName().equals(methodname)) {
            Object handler = handlerRef.get();
            if (handler != null) {
                Method newmethod = this.methodRef.get();
                if (newmethod != null) {
                    try {
                        return newmethod.invoke(handler, objects);
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                        StringBuilder str = new StringBuilder();
                        str.append(method.getName()+",");
                        str.append(newmethod.getName()+":");
                        for (Object item : objects) {
                            str.append(item.getClass().getName() + ",");
                        }
                        Log.e("eventerror", str.substring(0, str.length() - 1));
                    }
                }
            }
        }
        return null;
    }
}
