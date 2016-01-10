package com.cmbb.app.ui.capital;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.capital.InComeEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;

/**
 * Created by Storm on 2015/11/22.
 * 累积收入
 */
public class PageIncome implements View.OnClickListener {
    private View view;
    private BaseActivity mContext;
    private TextView asset, income_already, income_wait;

    public PageIncome(BaseActivity context) {
        this.mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.page_income, null);
        setUpView();
        getData();
    }

    private void setUpView() {
        view.findViewById(R.id.income_detail).setOnClickListener(this);
        asset = (TextView) view.findViewById(R.id.tv_asset);
        income_already = (TextView) view.findViewById(R.id.tv_income_already);
        income_wait = (TextView) view.findViewById(R.id.tv_income_wait);
        view.findViewById(R.id.income_detail).setOnClickListener(this);
    }

    public View getView() {
        return view;
    }

    private void getData() {
        this.mContext.httpRequest(Params.getMyPurseList(mContext), Methods.METHOD_MY_PURSE_INCOME, Environments.PV1, InComeEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                if (result.isSucess() && result instanceof InComeEntity) {
                    InComeEntity entity = (InComeEntity) result;
                    asset.setText(Tools.formatMoney(entity.getImcome_already() + entity.getImcome_wait()));
                    income_already.setText(Tools.formatMoney(entity.getImcome_already()));
                    income_wait.setText(Tools.formatMoney(entity.getImcome_wait()));
                }
            }

            @Override
            public void onRequestFailed(String msg) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.income_detail:
                mContext.startActivity(ActivityCapitalFlow.class);
                break;
        }
    }
}
