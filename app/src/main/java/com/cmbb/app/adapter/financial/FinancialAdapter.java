package com.cmbb.app.adapter.financial;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.entity.financial.FinancialItemEntity;
import com.cmbb.app.views.widget.CycleProgressView;

import java.util.List;

/**
 * Created by Storm on 2015/11/26.
 * DES:
 */
public class FinancialAdapter extends BaseAdapter {
    private Context mContext;
    private List<FinancialItemEntity> dataList;
    private LayoutInflater inflater;

    public FinancialAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(this.mContext);
    }

    public void updateList(List<FinancialItemEntity> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.dataList == null ? 0 : this.dataList.size();
    }

    @Override
    public FinancialItemEntity getItem(int position) {
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
            convertView = inflater.inflate(R.layout.finacial_child_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.p_name = (TextView) convertView.findViewById(R.id.tv_project_name);
            viewHolder.p_rate = (TextView) convertView.findViewById(R.id.c1_value);
            viewHolder.p_days = (TextView) convertView.findViewById(R.id.c2_value);
            viewHolder.min_price = (TextView) convertView.findViewById(R.id.c3_value);
            viewHolder.progress = (CycleProgressView) convertView.findViewById(R.id.progress);
            viewHolder.tag1 = (TextView) convertView.findViewById(R.id.btn_tag1);
            viewHolder.tag2 = (TextView) convertView.findViewById(R.id.btn_tag2);
            viewHolder.p_type = (ImageView) convertView.findViewById(R.id.project_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FinancialItemEntity item = getItem(position);
        String tag = item.getP_tags();
        if (!Tools.isEmpty(tag)) {
            String[] tags = tag.split(",");
            if (tags.length == 0) {
                viewHolder.tag1.setVisibility(View.INVISIBLE);
                viewHolder.tag2.setVisibility(View.INVISIBLE);
            } else if (tags.length == 1) {
                viewHolder.tag1.setText(tags[0]);
                viewHolder.tag1.setVisibility(View.VISIBLE);
                viewHolder.tag2.setVisibility(View.INVISIBLE);
            } else if (tags.length == 2) {
                viewHolder.tag1.setText(tags[0]);
                viewHolder.tag2.setText(tags[1]);
                viewHolder.tag1.setVisibility(View.VISIBLE);
                viewHolder.tag2.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.tag1.setVisibility(View.INVISIBLE);
            viewHolder.tag2.setVisibility(View.INVISIBLE);
        }
        viewHolder.p_name.setText(item.getP_name());
        viewHolder.p_rate.setText(String.valueOf(item.getP_rate()));
        viewHolder.p_days.setText(item.getP_date());
        viewHolder.min_price.setText(Tools.formatMoney(item.getP_min_price()));
//        item.setPercent(100);
        viewHolder.progress.setProgress(item.getP_percent());
        if (item.getP_percent() >= 0 && item.getP_percent() < 100) {
            viewHolder.p_rate.setTextColor(mContext.getResources().getColor(R.color.color_ee5447));
            viewHolder.p_days.setTextColor(mContext.getResources().getColor(R.color.color_ee5447));
            viewHolder.min_price.setTextColor(mContext.getResources().getColor(R.color.color_ee5447));
            switch (item.getP_sign()) {
                case 1:
                    viewHolder.p_type.setImageResource(R.mipmap.icon_newer_light);
                    break;
                case 2:
                    viewHolder.p_type.setImageResource(R.mipmap.icon_hot_light);
                    break;
                case 3:
                    viewHolder.p_type.setImageResource(R.mipmap.icon_activity_light);
                    break;
                default:
                    viewHolder.p_type.setImageDrawable(null);
                    break;
            }
        } else {
            viewHolder.p_rate.setTextColor(Color.BLACK);
            viewHolder.p_days.setTextColor(Color.BLACK);
            viewHolder.min_price.setTextColor(Color.BLACK);

            switch (item.getP_sign()) {
                case 1:
                    viewHolder.p_type.setImageResource(R.mipmap.icon_newer_gray);
                    break;
                case 2:
                    viewHolder.p_type.setImageResource(R.mipmap.icon_hot_gray);
                    break;
                case 3:
                    viewHolder.p_type.setImageResource(R.mipmap.icon_activity_gray);
                    break;
                default:
                    viewHolder.p_type.setImageDrawable(null);
                    break;
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView p_name;
        TextView p_rate;
        TextView p_days;
        TextView min_price;
        CycleProgressView progress;
        TextView tag1, tag2;
        ImageView p_type;
    }
}
