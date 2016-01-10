package com.cmbb.app.ui.financial;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.adapter.financial.FinacialHistoryAdapter;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.financial.FinacialHistoryEntity;
import com.cmbb.app.entity.financial.FinacialHistoryItemEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.xlist.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Storm on 2015/11/28.
 * DES:所有投资者的投资记录
 */
public class FinacialHistory extends BaseActivity implements XListView.IXListViewListener {
    private XListView listView;
    private FinacialHistoryAdapter adapter;
    private TextView titleBuyerCount, titlePrice;
    private String p_id;
    private FinacialHistoryEntity data;
    private List<FinacialHistoryItemEntity> investors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_history);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.my_invest_history, true);
        p_id = getIntent().getExtras().getString("p_id");
        listView = (XListView) findViewById(R.id.list_view);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this);
        listView.setAdapter(adapter = new FinacialHistoryAdapter(this));
        addListTitle();
    }

    private void addListTitle() {
        View titleView = LayoutInflater.from(this).inflate(R.layout.activity_financial_history_title, null);
        titleBuyerCount = (TextView) titleView.findViewById(R.id.buyer_count);
        titlePrice = (TextView) titleView.findViewById(R.id.price);
        listView.addHeaderView(titleView);
        setTitleText("0", "0");
    }

    private void setTitleText(String buyer, String money) {
        titleBuyerCount.setText(Html.fromHtml("<html>" + getString(R.string.detail_buyer_count2, "<font color='#EE5447'>" + buyer + "</font>") + "</html>"));
        titlePrice.setText(Html.fromHtml("<html>" + getString(R.string.detail_money, "<font color='#EE5447'>" + Tools.formatMoney(Double.valueOf(money)) + "</font>") + "</html>"));
    }

    @Override
    public void setUpData() {
        showLoadingAnimation();
        getData(0);
    }

    private void getData(int type) {
        findViewById(R.id.null_tip).setVisibility(View.GONE);
        int current_page = 1;
        if (type == 1 && data != null) {
            current_page = Integer.valueOf(data.getCurrent_page()) + 1;
        }
        httpRequest(Params.getFinacialHistoryParams(this, p_id, current_page, 20), Methods.METHOD_PRODUCT_INVESTMENT_LIST, Environments.PV1, FinacialHistoryEntity.class, new ReqeustData(type));
    }

    @Override
    public void onRefresh() {
        getData(0);
    }

    @Override
    public void onLoadMore() {
        getData(1);
    }

    class ReqeustData implements RequestCallback {
        private int type;

        public ReqeustData(int type) {
            this.type = type;
        }

        @Override
        public void onRequestSucess(BaseEntity result, String jsonData) {
            closeLoadingAnimation();
            stopRefresh();
            if (result.isSucess()) {
                if (result instanceof FinacialHistoryEntity) {
                    FinacialHistoryEntity entity = (FinacialHistoryEntity) result;
                    if (entity != null) {
                        List<FinacialHistoryItemEntity> list = entity.getInvestors();
                        if (list != null && list.size() > 0) {
                            data = entity;
                            dealData(list);
                        }
                        if (list == null || list.size() < 20) {
                            listView.setPullLoadEnable(false);
                        } else {
                            listView.setPullLoadEnable(true);
                        }
                    }
                    listNull();
                }
            }
        }

        private void dealData(List<FinacialHistoryItemEntity> list) {
            if (investors == null) {
                investors = new ArrayList<FinacialHistoryItemEntity>();
            }
            if (type == 0) {
                investors = list;
            } else {
                investors.addAll(list);
            }

            setTitleText(data.getP_buyer_count(), data.getP_money());
            adapter.updateList(investors);
        }

        @Override
        public void onRequestFailed(String msg) {
            closeLoadingAnimation();
            doToast(msg);
            stopRefresh();
        }


        private void stopRefresh() {
            if (type == 0) {
                listView.stopRefresh();
            } else {
                listView.stopLoadMore();
            }
        }
    }

    private void listNull() {
        if (adapter.getCount() == 0) {
            findViewById(R.id.null_tip).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.null_tip).setVisibility(View.GONE);
        }
    }
}
