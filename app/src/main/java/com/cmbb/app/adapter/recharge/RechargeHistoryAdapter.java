package com.cmbb.app.adapter.recharge;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.entity.recharge.RechargeHistoryItemEntity;

import java.util.List;

/**
 * Created by Storm on 2015/11/22.
 */
public class RechargeHistoryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<RechargeHistoryItemEntity> topup;
    private int type;

    public RechargeHistoryAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
        inflater = LayoutInflater.from(this.context);
    }

    public void updateList(List<RechargeHistoryItemEntity> topup) {
        this.topup = topup;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return topup == null ? 0 : topup.size();
    }

    @Override
    public RechargeHistoryItemEntity getItem(int position) {
        return topup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recharge_history_item, parent, false);
            holder = new ViewHolder();
            holder.value = (TextView) convertView.findViewById(R.id.recharge_value);
            holder.statu = (TextView) convertView.findViewById(R.id.recharge_statu);
            holder.time = (TextView) convertView.findViewById(R.id.recharge_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RechargeHistoryItemEntity item = getItem(position);
        holder.value.setText(Html.fromHtml("<html><font color='#EE5447'>+" + Tools.formatMoney(Double.valueOf(item.getMoney())) + "</font><font color='#333333'>" + context.getString(R.string.recharge_acount_yuan) + "<font></html>"));
        if ("0".equals(item.getStatus())) {
            if (type == 1) {
                holder.statu.setText(R.string.recharge_recharge_failed);
            } else {
                holder.statu.setText(R.string.wallet_take_out_failed);
            }
        } else {
            if (type == 1) {
                holder.statu.setText(R.string.recharge_recharge_sucess);
            } else {
                holder.statu.setText(R.string.wallet_take_out_sucess);
            }
        }
        holder.time.setText(item.getAction_time());
        return convertView;
    }

    class ViewHolder {
        TextView value;
        TextView statu;
        TextView time;
    }
}
