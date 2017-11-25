package com.wzzc.new_index;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by by Administrator on 2017/6/24.
 */

public abstract class NomalAdapter extends BaseAdapter {
    public ArrayList<Object> data;
    public Context c;

    public NomalAdapter(Context c) {
        this.c = c;
        data = new ArrayList<>();
    }

    @Override
    public void notifyDataSetChanged() {
        try {
            super.notifyDataSetChanged();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void addData(ArrayList<Object> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (data != null) {
            this.data.clear();
        }
    }

    public void removeDataFromFirst() {
    }

    public void removeDataFromSecond() {
    }

    public void removeDataFromThird() {
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
