package com.cmbb.app.adapter.more;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmbb.app.R;
import com.cmbb.app.adapter.base.MyBaseAdapter;

/**
 * Created by Storm on 2015/11/21.
 */
public class ActiveAdapter extends MyBaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;

    public ActiveAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(this.mContext);
    }

    public void updateList() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.more_active_item, parent, false);
        }
        return convertView;
    }
}
