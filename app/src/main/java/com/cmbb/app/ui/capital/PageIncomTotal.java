package com.cmbb.app.ui.capital;

import android.content.Intent;
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
import com.cmbb.app.entity.mine.SafetyAccount;
import com.cmbb.app.entity.mine.SafetyEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.certification.ActivityCertificationStep1;
import com.cmbb.app.ui.recharge.ActivityRecharge;

/**
 * Created by Storm on 2015/11/22.
 * 总资产
 */
public class PageIncomTotal implements View.OnClickListener {
    private View view;
    private BaseActivity mContext;
    private TextView tvAsset, tvBalance, tvWithdraw;
    private InComeEntity data;

    public PageIncomTotal(BaseActivity context) {
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.page_income_total, null);
        view.findViewById(R.id.reCharge).setOnClickListener(this);
        view.findViewById(R.id.to_cash).setOnClickListener(this);
        initViews();
    }

    private void initViews() {
        tvAsset = (TextView) view.findViewById(R.id.tv_asset);
        tvBalance = (TextView) view.findViewById(R.id.tv_balance);
        tvWithdraw = (TextView) view.findViewById(R.id.tv_withdraw);
        getData();
    }

    public View getView() {
        return view;
    }


    private void getData() {
        this.mContext.httpRequest(Params.getMyPurseList(mContext), Methods.METHOD_MY_PURSE_ASSET, Environments.PV1, InComeEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                if (result.isSucess() && result instanceof InComeEntity) {
                    data = (InComeEntity) result;
                    tvAsset.setText(Tools.formatMoney(data.getAsset()));
                    tvBalance.setText(Tools.formatMoney(data.getBalance()));
                    tvWithdraw.setText(Tools.formatMoney(data.getWithdraw()));
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
            case R.id.reCharge:
                getNewData(1);
                break;
            case R.id.to_cash:
                getNewData(2);
                break;
        }
    }

    private void toRecharge(int type, SafetyEntity safety) {
        if (data != null) {
            Intent intent = new Intent(mContext, ActivityRecharge.class);
            intent.putExtra("type", type);
            intent.putExtra("balance", data.getBalance());
            intent.putExtra("data",safety);
            mContext.startActivity(intent);
        }
    }

    private void getNewData(final int type) {
        mContext.showLoadingDialog(mContext.getString(R.string.user_info_get), false);
        mContext.httpRequest(Params.getSafetyCenter(mContext), Methods.METHOD_SAFETY_CENTER, Environments.PV1, SafetyEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                mContext.closeLoadingDialog();
                if (result.isSucess()) {
                    SafetyEntity safety = (SafetyEntity) result;
                    if (safety != null && safety.getAccount() != null) {
                        SafetyAccount account = safety.getAccount();
                        if (account.hasBindBank()) {
                            toRecharge(type, safety);
                        } else {
                            mContext.startActivity(ActivityCertificationStep1.class);
                        }
                    }
                } else {
                    mContext.doToast(result.getMsg());
                }
            }

            @Override
            public void onRequestFailed(String msg) {
                mContext.closeLoadingDialog();
                mContext.doToast(msg);
            }
        });
    }
}
