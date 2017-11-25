package com.wzzc.other_view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/12.
 *
 * 提示组件
 */

public class WarnFragment extends Fragment{
    public static final String ProInfo = "pro_info";
    TextView tv_info ;
    RelativeLayout lauout_close;
    RelativeLayout layout_out;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment_warn_goods,null);
        layout_out = (RelativeLayout) v.findViewById(R.id.layout_out);
        tv_info = (TextView) v.findViewById(R.id.tv_info);
        lauout_close = (RelativeLayout) v.findViewById(R.id.lauout_close);
        lauout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String info = bundle.getString(ProInfo) ;
        tv_info.setText(info);

    }
}
