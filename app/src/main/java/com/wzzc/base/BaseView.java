package com.wzzc.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.wzzc.base.annotation.BaseEvent;
import com.wzzc.base.annotation.BaseEventHandler;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by mypxq on 16-7-15.
 */
public abstract class BaseView extends RelativeLayout {
    private boolean isload = false;

    public BaseView(Context context) {
        super(context);
        readAnnotation(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAnnotation(context);
    }

    /**
     * 读取注解
     */
    protected void readAnnotation(Context context) {
        if (isInEditMode()) {
            return;
        }
        //界面加载
        ContentView contentView = this.getClass().getAnnotation(ContentView.class);
        int layoutid = 0;
        if (contentView != null) {
            layoutid = contentView.value();
        }
        if (layoutid == 0) {
            String classname = this.getClass().getName();
            classname = classname.substring(classname.lastIndexOf(".") + 1);
            classname = classname.substring(0, classname.length() - 4).toLowerCase();
            int index = classname.indexOf("$");
            if (index > 0) {
                classname = classname.substring(index + 1);
            }
            try {
                layoutid = R.layout.class.getDeclaredField("view_" + classname).getInt(R.layout.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LayoutInflater.from(context).inflate(layoutid, this);

        //属性加载
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                ViewInject viewInject = field.getAnnotation(ViewInject.class);
                if (viewInject != null) {
                    int viewid = 0;
                    if (viewInject.value() == 0) {
                        viewid = findViewId(field.getName().toLowerCase());
                    } else {
                        viewid = viewInject.value();
                    }
                    field.setAccessible(true);
                    field.set(this, findViewById(viewid));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //方法加载
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                BaseEvent eventBaseAnnotation = annotationType.getAnnotation(BaseEvent.class);
                if (eventBaseAnnotation != null) {
                    String name = eventBaseAnnotation.name();
                    Class<?> type = eventBaseAnnotation.type();
                    String methodname = eventBaseAnnotation.methodname();
                    try {
                        int[] viewIds = (int[]) annotationType.getDeclaredMethod("value").invoke(annotation, new Object[]{});
                        if(viewIds.length == 0) {
                            continue;
                        }
                        BaseEventHandler handler = new BaseEventHandler(method, this, methodname);
                        Object listener = Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
                        for (int viewId : viewIds) {
                            View view = findViewById(viewId);
                            Method setEventListenerMethod = view.getClass().getMethod(name, type);
                            setEventListenerMethod.invoke(view, listener);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 绑定事件
     */
    protected void bindEvent(View view, String eventname) {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (!eventname.equals(method.getName())) {
                continue;
            }
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                BaseEvent eventBaseAnnotation = annotationType.getAnnotation(BaseEvent.class);
                if (eventBaseAnnotation != null) {
                    String name = eventBaseAnnotation.name();
                    Class<?> type = eventBaseAnnotation.type();
                    String methodname = eventBaseAnnotation.methodname();
                    try {
                        BaseEventHandler handler = new BaseEventHandler(method, this, methodname);
                        Object listener = Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
                        Method setEventListenerMethod = view.getClass().getMethod(name, type);
                        setEventListenerMethod.invoke(view, listener);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    private int findViewId(String name) {
        try {
            return R.id.class.getDeclaredField(name).getInt(R.id.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    protected View findViewById(String name) {
        return super.findViewById(findViewId(name));
    }


    /*@Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isload) {
            isload = true;
            viewFirstLoad();
        }
    }*/

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!isload) {
            isload = true;
            viewFirstLoad();
        }
    }

    protected void viewFirstLoad() {

    }

    private void addMainView(Context context) {
        if (isInEditMode()) {
            return;
        }
        String classname = this.getClass().getName();
        classname = classname.substring(classname.lastIndexOf(".") + 1);
        classname = classname.substring(0, classname.length() - 4).toLowerCase();
        int index = classname.indexOf("$");
        if (index > 0) {
            classname = classname.substring(index + 1);
        }
        try {
            int layoutvalue = R.layout.class.getDeclaredField("view_" + classname).getInt(R.layout.class);
            LayoutInflater.from(context).inflate(layoutvalue, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected BaseActivity GetBaseActivity() {
        return (BaseActivity) this.getContext();

    }


}
