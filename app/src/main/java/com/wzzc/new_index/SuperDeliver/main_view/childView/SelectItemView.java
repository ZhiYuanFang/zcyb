package com.wzzc.new_index.SuperDeliver.main_view.childView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

/**
 * Created by toutou on 2017/4/27.
 *
 * 切换按钮布局
 */

public class SelectItemView extends BaseView{
    //region 组件
    @ViewInject(R.id.tv_category)
    private TextView tv_info;
    @ViewInject(R.id.tv_bottom)
    private TextView tv_bottom;
    //endregion
    public String type;

    public SelectItemView(Context context) {
        super(context);
        this.type = "";
        tv_info.setText("全部");
        init();
    }

    public SelectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.type = "";
        tv_info.setText("全部");
        init();
    }

    public SelectItemView(Context context,String name , String type) {
        super(context);
        this.type = type;
        tv_info.setText(name);
        init();
    }

    private void init () {

    }

    //region Method
    public String getName(){
        return tv_info.getText().toString();
    }
    public String getType () {
        if (type == null || type.length() == 0){
            type = "0";
        }
        return type;
    }
    public void setName (String name) {
        tv_info.setText(name);
    }
    public void setType (String type) {
        this.type = type;
    }
    public void focus (boolean focus) {
        int color = focus ? ContextCompat.getColor(getContext(),R.color.tv_Red) : ContextCompat.getColor(getContext(),R.color.tv_Black);
        tv_info.setTextColor(color);
        int color_line = focus ? ContextCompat.getColor(getContext(),R.color.tv_Red) : ContextCompat.getColor(getContext(),android.R.color.transparent);
        tv_bottom.setBackgroundColor(color_line);
    }
    //endregion
}
