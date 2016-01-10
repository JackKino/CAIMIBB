package com.cmbb.app.adapter.myticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.entity.financial.PayProductBuyCouponEntity;

import java.util.List;

/**
 * Created by Storm on 2015/12/9.
 * DES:
 */
public class CouponAdapter extends BaseAdapter {
    private Context mContext;
    private List<PayProductBuyCouponEntity> coupon;
    private int checkPosition = -1;

    public CouponAdapter(Context context, List<PayProductBuyCouponEntity> coupon, int defaultPosition) {
        this.mContext = context;
        this.coupon = coupon;
        this.checkPosition = defaultPosition;
    }

    public int getCheckedPosition() {
        return checkPosition;
    }

    @Override
    public int getCount() {
        return this.coupon == null ? 0 : this.coupon.size();
    }

    @Override
    public PayProductBuyCouponEntity getItem(int position) {
        return this.coupon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(this.mContext).inflate(R.layout.coupon_dialog_item, parent, false);
        PayProductBuyCouponEntity group = getItem(position);
        ImageView ivStatu = (ImageView) convertView.findViewById(R.id.iv_statu);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
        TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
        TextView union = (TextView) convertView.findViewById(R.id.union);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
        if (position == checkPosition) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkPosition = position;
                } else {
                    checkPosition = -1;
                }
                notifyDataSetChanged();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkPosition = -1;
                } else {
                    checkPosition = position;
                }
                notifyDataSetChanged();
            }
        });
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
            tvMoney.setText(String.valueOf(Tools.formatPercent(group.getRate())));
            union.setText("%");
        }

        tvName.setText(group.getName());

        tvDate.setText(mContext.getString(R.string.my_ticket_from_to, group.getStart_time(), group.getExpire_time()));

        return convertView;
    }
}
