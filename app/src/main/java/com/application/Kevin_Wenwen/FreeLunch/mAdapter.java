package com.application.Kevin_Wenwen.FreeLunch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by wenwen on 12/9/15.
 */
public class mAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> names;

    public mAdapter(Context c, ArrayList<String> imageURLs) {
        mContext = c;
        this.names = imageURLs;
    }
    public int getCount() {
        return names.size();
    }
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View tmp = convertView;
        return tmp;
    }
}
