package com.wzzc.index.home.h.main_view.main_view.main_view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzzc.other_view.extendView.ExtendImageView;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/10.
 */

public class ProfileFragment extends Fragment {

    public static final String BaseClass = "class";
    public static final String SHOPNAME = "1" , NUMBER = "2" , DISTANCE = "3" , PROFILE = "4",
    SHOPICON = "5" , INFOICON = "6" ,STARTDATE = "7";
    private TextView tv_shop_name , tv_number , tv_distance_fragment , tv_profile , tv_start_data;
    private ExtendImageView eiv_shop_icon , eiv_info;
    private ImageButton ibn_cancel;
    private RelativeLayout layout_out;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_shop_name = (TextView) getActivity().findViewById(R.id.tv_shop_name);
        tv_number = (TextView) getActivity().findViewById(R.id.tv_number);
        tv_distance_fragment = (TextView) getActivity().findViewById(R.id.tv_distance_fragment);
        tv_profile = (TextView) getActivity().findViewById(R.id.tv_profile);
        tv_start_data = (TextView) getActivity().findViewById(R.id.tv_start_data);
        eiv_shop_icon = (ExtendImageView) getActivity().findViewById(R.id.eiv_shop_icon);
        eiv_shop_icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        eiv_info = (ExtendImageView)getActivity(). findViewById(R.id.eiv_info);
        ibn_cancel = (ImageButton) getActivity().findViewById(R.id.ibn_cancel);
        layout_out = (RelativeLayout) getActivity().findViewById(R.id.layout_out);
        Bundle bundle = getArguments();
        tv_shop_name.setText(bundle.getString(SHOPNAME));
        tv_number.setText("共"+bundle.getString(NUMBER)+"件宝贝");
        tv_distance_fragment.setText(bundle.getString(DISTANCE));
        tv_start_data.setText(bundle.getString(STARTDATE));
        tv_profile.setText(bundle.getString(PROFILE));
        eiv_shop_icon.setPath(bundle.getString(SHOPICON));
        eiv_info.setPath(bundle.getString(INFOICON));
        ibn_cancel.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
