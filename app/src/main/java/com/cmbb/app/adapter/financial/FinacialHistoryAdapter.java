package com.cmbb.app.adapter.financial;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.entity.financial.FinacialHistoryItemEntity;

import java.util.List;

/**
 * Created by Storm on 2015/11/28.
 * DES:
 */
public class FinacialHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<FinacialHistoryItemEntity> investors;

    public FinacialHistoryAdapter(Context context) {
        this.mContext = context;
    }

    public void updateList(List<FinacialHistoryItemEntity> investors) {
        this.investors = investors;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return investors == null ? 0 : investors.size();
    }

    @Override
    public FinacialHistoryItemEntity getItem(int position) {
        return investors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.finacial_history_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.mobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            viewHolder.money = (TextView) convertView.findViewById(R.id.tv_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FinacialHistoryItemEntity item = getItem(position);
        viewHolder.date.setText(item.getBuy_time());
        viewHolder.mobile.setText(item.getMobile());
        viewHolder.money.setText(Html.fromHtml("<html><font color='#EE5447'>" + Tools.formatMoney(Double.valueOf(item.getMoney())) + "</font>" + mContext.getString(R.string.recharge_acount_yuan) + "</html>"));
        return convertView;
    }

    class ViewHolder {
        TextView mobile;
        TextView date;
        TextView money;
    }
}
