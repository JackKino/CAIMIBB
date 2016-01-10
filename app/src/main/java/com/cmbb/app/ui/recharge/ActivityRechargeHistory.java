package com.cmbb.app.ui.recharge;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.adapter.recharge.RechargeHistoryAdapter;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.recharge.RechargeHistoryEntity;
import com.cmbb.app.entity.recharge.RechargeHistoryItemEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;

import java.util.List;

/**
 * Created by Storm on 2015/11/22.
 */
public class ActivityRechargeHistory extends BaseActivity {
    private ListView listView;
    private RechargeHistoryAdapter adapter;
    private int type;//1充值记录   2提现记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_history);
    }

    @Override
    public void setUpViews() {
        this.type = getIntent().getExtras().getInt("type");
        if (type == 1) {
            initTitleWithRightText(getString(R.string.recharge_recharge_history), getString(R.string.recharge_recharge_refresh), true);
            findViewById(R.id.history_back_to).setVisibility(View.VISIBLE);
        } else {
            initTitleWithRightText(getString(R.string.wallet_take_out_history), getString(R.string.recharge_recharge_refresh), true);
            findViewById(R.id.history_back_to).setVisibility(View.GONE);
        }
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter = new RechargeHistoryAdapter(this, type));
        findViewById(R.id.history_back_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onRightPressed(View view) {
        super.onRightPressed(view);
        setUpData();
    }

    @Override
    public void setUpData() {
        showLoadingDialog(null, true);
        String method = Methods.METHOD_RECHARG_HISTORY;
        if (type == 2) {
            method = Methods.METHOD_WITHDRAW_HISTORY;
        }
        httpRequest(Params.getRechargeHistory(this), method, Environments.PV1, RechargeHistoryEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                closeLoadingDialog();
                if (result.isSucess()) {
                    if (result instanceof RechargeHistoryEntity) {
                        refreshList(((RechargeHistoryEntity) result).getTopup());
                        listNull();
                    }
                }
            }

            @Override
            public void onRequestFailed(String msg) {
                closeLoadingDialog();
            }
        });
    }

    private void refreshList(List<RechargeHistoryItemEntity> topup) {
        adapter.updateList(topup);
    }

    private void listNull() {
        if (adapter.getCount() == 0) {
            findViewById(R.id.null_tip).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.null_tip).setVisibility(View.GONE);
        }
    }
}
