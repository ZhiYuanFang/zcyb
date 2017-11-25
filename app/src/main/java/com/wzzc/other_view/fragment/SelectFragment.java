package com.wzzc.other_view.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wzzc.base.Default;
import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/13.
 */

public class SelectFragment extends Fragment {
    public static final String ITEM_INFOS = "item_infos";
    public static final String ITEM_CallBack = "call_back";
    RelativeLayout layout_out;
    ListView fragment_listView;
    Button bt_cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_select_fragment,null);
        layout_out = (RelativeLayout) v.findViewById(R.id.layout_out);
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissMissFragment();
            }
        });
        fragment_listView = (ListView) v.findViewById(R.id.fragment_listView);
        bt_cancel = (Button) v.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissMissFragment();
            }
        });
        return v;
    }

    private void dissMissFragment () {
        Default.getActivity().onBackPressed();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String[] selectItemInfos = bundle.getStringArray(ITEM_INFOS);
        SelectCallBack item_call_back = bundle.getParcelable(ITEM_CallBack);
        fragment_listView.setAdapter(new MyAdapter(selectItemInfos,item_call_back));
    }

    private class MyAdapter extends BaseAdapter{
        String[] strs;
        SelectCallBack callBack;
        MyAdapter(String[] strs,SelectCallBack callBack){
            this.callBack = callBack;
            this.strs = strs;
        }
        @Override
        public int getCount() {
            return strs.length;
        }

        @Override
        public String getItem(int position) {
            return strs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Button bt = new Button(getContext());
                bt.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                bt.setGravity(Gravity.CENTER);
                bt.setTextColor(ContextCompat.getColor(getContext(),R.color.tv_White));
                bt.setBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.transparent));
                bt.setTag(position);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dissMissFragment();
                        callBack.call((Integer) v.getTag());
                    }
                });
                convertView = bt;
            }
            ((Button)convertView).setText(getItem(position));
            return convertView;
        }
    }

    public interface SelectCallBack extends Parcelable{
        void call(int index);
    }

}
