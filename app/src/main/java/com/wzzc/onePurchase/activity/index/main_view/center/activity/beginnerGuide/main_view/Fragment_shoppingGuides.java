package com.wzzc.onePurchase.activity.index.main_view.center.activity.beginnerGuide.main_view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/3/27.
 */

public class Fragment_shoppingGuides extends Fragment {
    public static String TEXT = "333";

    private TextView tv_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.textview_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_layout = (TextView) getActivity().findViewById(R.id.tv_fragment);
        Bundle bundle = getArguments();
        tv_layout.setText(bundle.getString(TEXT));
    }
}
