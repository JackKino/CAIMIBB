package com.cmbb.app.adapter.myticket;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.adapter.base.MyBaseAdapter;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.capital.InvestmentRecordItemEntity;
import com.cmbb.app.entity.financial.FinancialItemEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseFragment;
import com.cmbb.app.ui.capital.ActivityProjectDetail;

import java.util.List;

/**
 * Created by Storm on 2015/11/23.
 */
public class InvestAdapter extends MyBaseAdapter {
    private BaseFragment mContext;
    private List<InvestmentRecordItemEntity> dataList;

    public InvestAdapter(BaseFragment context) {
        this.mContext = context;
    }

    public void updateList(List<InvestmentRecordItemEntity> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.dataList == null ? 0 : this.dataList.size();
    }

    @Override
    public InvestmentRecordItemEntity getItem(int position) {
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
            convertView = LayoutInflater.from(this.mContext.getActivity()).inflate(R.layout.invest_item_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.p_startTime = (TextView) convertView.findViewById(R.id.tv_project_time_start);
            viewHolder.p_endTime = (TextView) convertView.findViewById(R.id.tv_project_time_end);
            viewHolder.p_name = (TextView) convertView.findViewById(R.id.tv_project_name);
            viewHolder.p_money = (TextView) convertView.findViewById(R.id.tv_project_money);
            viewHolder.p_rate = (TextView) convertView.findViewById(R.id.tv_project_rate);
            viewHolder.tv_to_detail = convertView.findViewById(R.id.tv_to_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InvestmentRecordItemEntity item = getItem(position);
        viewHolder.p_name.setText(item.getP_name());
        viewHolder.p_money.setText(mContext.getString(R.string.my_income, item.getP_money()));
        viewHolder.p_rate.setText(mContext.getString(R.string.investing_rate, item.getP_rate() + "%"));
        viewHolder.p_startTime.setText(mContext.getString(R.string.investing_rate, item.getP_rate()));
        viewHolder.p_startTime.setText(mContext.getString(R.string.investing_start_time, item.getP_pay_time()));
        viewHolder.p_endTime.setText(mContext.getString(R.string.investing_end_time, item.getP_expire_time()));
        viewHolder.tv_to_detail.setTag(item);

        viewHolder.tv_to_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProjectDetail(v);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView p_name;
        TextView p_money;
        TextView p_rate;
        TextView p_startTime;
        TextView p_endTime;
        View tv_to_detail;
    }


    private void getProjectDetail(View v) {
        InvestmentRecordItemEntity item = (InvestmentRecordItemEntity) v.getTag();
        this.mContext.showLoadingDialog(mContext.getString(R.string.common_loading), true);
        this.mContext.httpRequest(Params.getProjectDetailParams(this.mContext.getActivity(), item.getP_id()), Methods.METHOD_PRODUCT_DETAIL, Environments.PV1, FinancialItemEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                mContext.closeLoadingDialog();
                FinancialItemEntity item = (FinancialItemEntity) result;
                Intent intent = new Intent(mContext.getActivity(), ActivityProjectDetail.class);
                intent.putExtra("item", item);
                mContext.startActivity(intent);
            }

            @Override
            public void onRequestFailed(String msg) {
                mContext.closeLoadingDialog();
            }
        });
    }
}
