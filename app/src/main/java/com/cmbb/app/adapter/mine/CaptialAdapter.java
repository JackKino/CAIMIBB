package com.cmbb.app.adapter.mine;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.entity.capital.CapitalFlowItemEntity;

import java.util.List;

/**
 * Created by Storm on 2015/11/24.
 */
public class CaptialAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<CapitalFlowItemEntity> dataList;

    public CaptialAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(this.mContext);
    }

    public void updateList(List<CapitalFlowItemEntity> dataList) {
//        if (dataList.size() == 0) {
//            CapitalFlowItemEntity item = new CapitalFlowItemEntity();
//            item.setBalance("100000");
//            item.setMoney("300000");
//            item.setNote("我投资的钱");
//            item.setType_id(1);
//            item.setTime("2015/12/02 12:03");
//            dataList.add(item);
//
//            item = new CapitalFlowItemEntity();
//            item.setBalance("100000");
//            item.setMoney("300000");
//            item.setNote("我投资的钱");
//            item.setType_id(2);
//            item.setTime("2015/12/02 12:03");
//            dataList.add(item);
//
//            item = new CapitalFlowItemEntity();
//            item.setBalance("100000");
//            item.setMoney("300000");
//            item.setNote("我投资的钱");
//            item.setType_id(3);
//            item.setTime("2015/12/02 12:03");
//            dataList.add(item);
//
//            item = new CapitalFlowItemEntity();
//            item.setBalance("100000");
//            item.setMoney("300000");
//            item.setNote("我投资的钱");
//            item.setType_id(4);
//            item.setTime("2015/12/02 12:03");
//            dataList.add(item);
//
//            item = new CapitalFlowItemEntity();
//            item.setBalance("100000");
//            item.setMoney("300000");
//            item.setNote("我投资的钱");
//            item.setType_id(5);
//            item.setTime("2015/12/02 12:03");
//            dataList.add(item);
//        }
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.dataList == null ? 0 : this.dataList.size();
    }

    @Override
    public CapitalFlowItemEntity getItem(int position) {
        return this.dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.capital_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTypeValue = (TextView) convertView.findViewById(R.id.tv_type_value);
            viewHolder.tvCompany = (TextView) convertView.findViewById(R.id.tv_company);
            viewHolder.tvmoney = (TextView) convertView.findViewById(R.id.tv_money);
            viewHolder.tvbalance = (TextView) convertView.findViewById(R.id.tv_balance);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.ivTypeIcon = (ImageView) convertView.findViewById(R.id.iv_type_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CapitalFlowItemEntity item = getItem(position);
        String color = "#00AEFF";
        String mode = "+";
        switch (item.getType_id()) {
            case 1:
                viewHolder.tvTypeValue.setText(R.string.captial_item_0);
                viewHolder.ivTypeIcon.setImageResource(R.mipmap.icon_capital_0);
                break;
            case 2:
                color = "#F97373";
                mode = "—";
                viewHolder.tvTypeValue.setText(R.string.captial_item_1);
                viewHolder.ivTypeIcon.setImageResource(R.mipmap.icon_capital_1);
                break;
            case 3:
                color = "#F97373";
                mode = "—";
                viewHolder.tvTypeValue.setText(R.string.captial_item_2);
                viewHolder.ivTypeIcon.setImageResource(R.mipmap.icon_capital_2);
                break;
            case 4:
                viewHolder.tvTypeValue.setText(R.string.captial_item_3);
                viewHolder.ivTypeIcon.setImageResource(R.mipmap.icon_capital_3);
                break;
            case 5:
                viewHolder.tvTypeValue.setText(R.string.captial_item_4);
                viewHolder.ivTypeIcon.setImageResource(R.mipmap.icon_capital_4);
                break;
        }

        viewHolder.tvTime.setText(item.getTime().replace(" ", "\n"));
        viewHolder.tvCompany.setText(item.getNote());
        String money = Tools.formatMoney(Double.valueOf(item.getMoney()));
        String balance = Tools.formatMoney(Double.valueOf(item.getBalance()));

        String money_html = "<html>" + mContext.getString(R.string.my_income, "<font color='" + color + "'>" + mode + money + "</font>") + "</html>";

        viewHolder.tvmoney.setText(Html.fromHtml(money_html));
        viewHolder.tvbalance.setText(mContext.getString(R.string.my_income, balance));
        return convertView;
    }

    class ViewHolder {
        TextView tvTypeValue;
        TextView tvCompany;
        TextView tvmoney;
        TextView tvbalance;
        TextView tvTime;
        ImageView ivTypeIcon;
    }
}
