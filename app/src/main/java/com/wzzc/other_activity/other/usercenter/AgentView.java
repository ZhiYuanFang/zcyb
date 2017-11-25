package com.wzzc.other_activity.other.usercenter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wzzc.base.BaseView;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.zcyb365.R;

import org.json.JSONObject;

/**
 * Created by zcyb365 on 2016/11/23.
 */
public class AgentView extends BaseView {

    @ViewInject(R.id.lab_area)
    private TextView lab_area;
    @ViewInject(R.id.lab_state)
    private TextView lab_state;
    @ViewInject(R.id.lab_money)
    private TextView lab_money;

    public AgentView(Context context) {
        super(context);
    }
    public AgentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInfo(JSONObject[] data) {

    }

}
