package com.wzzc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzzc.zcyb365.R;

/**
 * Created by by Administrator on 2017/7/31.
 */

public class NormalRecyclerViewAdapter extends RecyclerView.Adapter {
    Context c;
    String title;
    String[] strs;
    LayoutInflater inflater;
    private static final int Type_search = -1;
    private static final int Type_head = 0;
    private static final int Type_body = 1;

    public NormalRecyclerViewAdapter (Context c , String title , String[] strs) {
        inflater = LayoutInflater.from(c);
        this.title = title;
        this.c = c;
        this.strs = strs;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType){
            case Type_head:
                v = inflater.inflate(R.layout.layout_normalrecycler_head,parent,false);
                return new HeadHolder(v);
            case Type_body:
                v = inflater.inflate(R.layout.layout_normalrecycler_item,parent,false);
                return new NormalRecyclerHolder(v);
            case Type_search:
                v = inflater.inflate(R.layout.layout_normalrecycler_search,parent,false);
                return new SearchHolder(v);
            default:
        }
        return new NormalRecyclerHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder){
            ((HeadHolder)holder).tv.setText(title);
        } else if (holder instanceof NormalRecyclerHolder) {
            ((NormalRecyclerHolder)holder).tv.setText(strs[position - 1]);
        }
    }

    @Override
    public int getItemCount() {
        return strs.length + 2;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return Type_head;
            default:return Type_body;
        }
    }

    private class NormalRecyclerHolder extends RecyclerView.ViewHolder {
        TextView tv;
        NormalRecyclerHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_nomal);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getLayoutPosition());
                }
            });
        }
    }

    private class HeadHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public HeadHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_head);
        }
    }

    private class SearchHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public SearchHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_head);
        }
    }
}
