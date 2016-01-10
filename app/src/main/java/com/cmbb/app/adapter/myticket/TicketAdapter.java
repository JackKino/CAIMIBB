package com.cmbb.app.adapter.myticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.entity.capital.CouponItemEntity;

import java.util.List;

/**
 * Created by Storm on 2015/11/23.
 */
public class TicketAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<CouponItemEntity> couponList;

    public TicketAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(this.mContext);
    }

    public void updateList(List<CouponItemEntity> couponList) {
        this.couponList = couponList;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return this.couponList == null ? 0 : this.couponList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public CouponItemEntity getGroup(int groupPosition) {
        return couponList.get(groupPosition);
    }

    @Override
    public CouponItemEntity getChild(int groupPosition, int childPosition) {
        return couponList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ticket_group_item, parent, false);
        }
        CouponItemEntity group = getGroup(groupPosition);
        TextView name = (TextView) convertView.findViewById(R.id.group_name);
        if ("0".equals(group.getStatus())) {
            name.setText(R.string.my_ticket_unused);
        } else if ("1".equals(group.getStatus())) {
            name.setText(R.string.my_ticket_used);
        } else if ("2".equals(group.getStatus())) {
            name.setText(R.string.my_ticket_outtime);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ticket_child_item, parent, false);
        }
        CouponItemEntity group = getChild(groupPosition, childPosition);
        ImageView ivStatu = (ImageView) convertView.findViewById(R.id.iv_statu);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
        TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
        TextView union = (TextView) convertView.findViewById(R.id.union);
        if ("0".equals(group.getStatus())) {
            ivStatu.setImageResource(R.mipmap.icon_ticket_statu_unused);
        } else if ("1".equals(group.getStatus())) {
            ivStatu.setImageResource(R.mipmap.icon_ticket_statu_used);
        } else if ("2".equals(group.getStatus())) {
            ivStatu.setImageResource(R.mipmap.icon_ticket_statu_timeout);
        }
        if ("0".equals(group.getType_id())) {
            tvType.setText(R.string.my_ticket_type_0);
            tvMoney.setText(String.valueOf(group.getMoney()));
            union.setText(R.string.recharge_acount_yuan);
        } else if ("1".equals(group.getType_id())) {
            tvType.setText(R.string.my_ticket_type_1);
            tvMoney.setText(group.getRate());
            union.setText("%");
        }

        tvName.setText(group.getName());
        tvDate.setText(mContext.getString(R.string.my_ticket_from_to, group.getStart_time(), group.getExpire_time()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
