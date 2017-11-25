package com.wzzc.base;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzzc.base.annotation.BaseEvent;
import com.wzzc.base.annotation.BaseEventHandler;
import com.wzzc.base.annotation.ContentView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_function.LocationClass;
import com.wzzc.zcyb365.R;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * Created by zcyb365 on 16-7-15.
 */
public class BaseActivity extends FragmentActivity {
    private boolean isfirstload = false;
    private boolean isgotoview = false;
    private boolean isbackview = false;
    private int AnimAction = -1;
    protected ViewGroup view;

    public static ArrayList<BaseActivity> AllBaseActivity = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        AllBaseActivity.add(this);
        readAnnotation();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("AnimAction")) {
                AnimAction = bundle.getInt("AnimAction");
            }
        }
        view = (ViewGroup) getWindow().getDecorView();
        AddBack();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        AllBaseActivity.add(this);
    }

    protected float distanceX = 100;
    protected float distanceTime = 300;
    protected float downX;
    protected long downTime;
    protected void initBackTouch () {
        View basicLayout = findViewById("touchLayout");
        if (basicLayout != null){
            basicLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN :
                            downX = event.getX();
                            downTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            float upX = event.getX();
                            long upTime = System.currentTimeMillis();
                            if ((upX - downX) > distanceX && (upTime-downTime) < distanceTime) {
                                onBackPressed();
                            }
                            break;
                        default:
                    }
                    return true;
                }
            });
        }
    }

    /**
     * 读取注解
     */
    private void readAnnotation() {
        //界面加载
        ContentView contentView = this.getClass().getAnnotation(ContentView.class);
        int layoutid = 0;
        if (contentView != null) {
            layoutid = contentView.value();
        }
        if (layoutid == 0) {
            String classname = this.getClass().getName();
            classname = classname.substring(classname.lastIndexOf(".") + 1);
            classname = classname.substring(0, classname.length() - 8).toLowerCase();
            try {
                layoutid = R.layout.class.getDeclaredField("activity_" + classname).getInt(R.layout.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setContentView(layoutid);

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
                        if (viewIds.length == 0) {
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
     * 绑定事件 *****************************************************************
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (!isfirstload) {
                isfirstload = true;
                viewFirstLoad();
            }
        }
    }

    /**
     * 第一次载入界面
     */
    protected void viewFirstLoad() {
//        AddActivity(WellcomeActivity.class);
    }

    /**
     * 获取传递参数
     */
    public Object GetIntentData(String name) {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(name)) {
                return bundle.get(name);
            }
        }
        return null;
    }

    /**
     * 获取View的ID
     */
    private int findViewId(String name) {
        try {
            return R.id.class.getDeclaredField(name).getInt(R.id.class);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据ID获取View
     */
    protected View findViewById(String name) {
        return super.findViewById(findViewId(name));
    }

    /**
     * 获取Anim的ID
     */
    private int findAnimId(String name) {
        try {
            return R.anim.class.getDeclaredField(name).getInt(R.anim.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取Drawable
     */
    public Drawable GetDrawable(String name) {
        try {
            int id = R.drawable.class.getDeclaredField(name).getInt(R.drawable.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return this.getDrawable(id);
            } else {
                return getResources().getDrawable(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void finish() {
        super.finish();
        AllBaseActivity.remove(this);
    }

    public boolean hasFragment;
    protected void outFragment () {
        getSupportFragmentManager().popBackStack();
        hasFragment = false;
    }
    public void onBackPressed() {
        super.onBackPressed();
        if (!hasFragment) {
            BackActivity();
        } else {
            outFragment();
        }
    }

    /**
     * 添加返回
     */
    public void AddBack() {
        if (AnimAction == 0) {
            AddBack("btn_back");
        } else {
            AddBack("btn_close");
        }
    }

    /**
     * 添加返回
     */
    public void AddBack(String name) {
        View btn_back = findViewById("btn_back");
        if (btn_back == null) {
            return;
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseActivity.this.AnimAction != -1) {
                    BaseActivity.this.BackActivity();
                    System.out.println("just go to back function");
                } else {
                    System.out.println("can't go to back function");
                }
            }
        });
        if (btn_back.getBackground() == null && btn_back instanceof ImageView) {
            if (name == null) {
                ((ImageView) btn_back).setImageDrawable(GetDrawable("btn_back"));
            } else {
                ((ImageView) btn_back).setImageDrawable(GetDrawable(name));
            }
        }

    }

    /**
     * 返回Activity
     */
    public void BackActivity() {
        BackActivity(null);
    }

    /**
     * 返回Activity
     */
    public void BackActivity(Intent intent) {
        if (isbackview) {
            return;
        }
        if (intent != null) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_OK);
        }
        this.finish();
        if (AllBaseActivity.size() != 0) {
            if (AnimAction == 0) {
                overridePendingTransition(findAnimId("activity_exit_in"), findAnimId("activity_exit_out"));
            } else if (AnimAction == 1) {
                overridePendingTransition(findAnimId("zoom_exit_in"), findAnimId("zoom_exit_out"));
            }
        }
        isbackview = true;
    }

    /**
     * 返回Activity
     */
    public void BackActivity(int index, Intent intent) {
        if (AllBaseActivity.size() - 1 <= index || index < 0) {
            return;
        }
        BackToActivity(index, intent);
    }

    /**
     * 返回Activity
     */
    private void BackToActivity(int index, Intent intent) {
        if (AllBaseActivity.size() - 1 > index) {
            if (intent == null) {
                intent = new Intent();
            }
            intent.putExtra("backactivitytoindex", index);
            setResult(RESULT_OK, intent);
            this.finish();
        } else {
            overridePendingTransition(findAnimId("activity_exit_in"), findAnimId("activity_exit_out"));
        }
    }

    /**
     * 添加Activity
     */
    public void AddActivity(Class<?> cls) {
        AddActivity(cls, 0, null);
    }

    public void AddActivity (int type,Intent mainIntent) {
        if (isgotoview) {
            return;
        }
        mainIntent.putExtra("AnimAction", type);
        this.startActivityForResult(mainIntent, 0);
        if (type == 0) {
            overridePendingTransition(findAnimId("activity_load_in"), findAnimId("activity_load_out"));
        } else if (type == 1) {
            overridePendingTransition(findAnimId("zoom_load_in"), findAnimId("zoom_load_out"));
        }
        isgotoview = true;
    }
    /**
     * 添加Activity
     */
    public void AddActivity(Class<?> cls, int type, Intent mainIntent) {
        if (isgotoview) {
            Default.showToast("isgotoview");
            return;
        }
        if (mainIntent == null) {
            mainIntent = new Intent(this, cls);
        } else {
            mainIntent.setClass(this, cls);
        }
        mainIntent.putExtra("AnimAction", type);
        this.startActivityForResult(mainIntent, 0);
        if (type == 0) {
            overridePendingTransition(findAnimId("activity_load_in"), findAnimId("activity_load_out"));
        } else if (type == 1) {
            overridePendingTransition(findAnimId("zoom_load_in"), findAnimId("zoom_load_out"));
        }
        isgotoview = true;
    }

    /**
     * 添加修改标题
     */
    public void AddTitle(String title) {
        ((TextView) findViewById("lab_title")).setText(title);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("result --- requestCode : " + resultCode + " resultCode : " + resultCode );
        isgotoview = false;
        switch (resultCode) {
            case RESULT_OK:
                if (data == null) {
                    onActivityResult(null);
                    return;
                }
                Bundle bundle = data.getExtras();
                onActivityResult(resultCode , data);
                if (bundle != null) {
                    if (bundle.containsKey("backactivitytoindex")) {
                        int index = bundle.getInt("backactivitytoindex");
                        Intent intent = new Intent();
                        if (bundle.size() > 1) {
                            intent.putExtras(bundle);
                            intent.removeExtra("backactivitytoindex");
                        }
                        BackToActivity(index, intent);
                        if (AllBaseActivity.size() - 1 == index) {
                            onActivityResult(bundle);
                        }
                    } else {
                        onActivityResult(bundle);
                    }
                } else {
                    onActivityResult(null);
                }
                break;
            default:
                onActivityResult(null);
                onActivityResult(resultCode , data);
                break;
        }
    }

    protected void onActivityResult(Bundle bundle) {

    }

    protected void onActivityResult(int resultCode , Intent data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationClass.stopConnection();
    }

    /** 刷新*/
    public void refresh() {
        super.recreate();
        try {
            this.onCreate(null);
        } catch (IllegalStateException e){
            e.printStackTrace();
        }

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        this.onCreate(null);
//    }
    //    public boolean CheckUserIsLogin(Boolean tologin) {
//        String userid = Default.GetUserID();
//
//        if (userid == null) {
//            if (tologin) {
//                Intent intent = new Intent();
//                intent.putExtra("info", "autologin");
//                AddActivity(LoginActivity.class, 1, intent);
//            }
//            return false;
//        }
//        return true;
//    }

}
