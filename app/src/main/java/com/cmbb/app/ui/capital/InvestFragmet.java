package com.cmbb.app.ui.capital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.adapter.myticket.InvestAdapter;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.capital.InvestmentRecordEntity;
import com.cmbb.app.entity.capital.InvestmentRecordItemEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseFragment;
import com.cmbb.app.views.xlist.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Storm on 2015/11/28.
 * DES:自己的投资记录的两个ViewPage
 */
public class InvestFragmet extends BaseFragment implements XListView.IXListViewListener {
    private XListView listView;
    private TextView pageName, investMoney;
    private int type;
    private List<InvestmentRecordItemEntity> dataList;
    private InvestAdapter adapter;
    private InvestmentRecordEntity dataEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initFragmentView(inflater, container, R.layout.page_invest_view);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews();
        setUpData();
    }

    @Override
    public void setUpViews() {
        type = getArguments().getInt("type");
        listView = (XListView) findViewById(R.id.list_view);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setAdapter(adapter = new InvestAdapter(this));
        addListTitle();
    }

    /**
     * 加头控件
     */
    private void addListTitle() {
        View titleView = LayoutInflater.from(this.getActivity()).inflate(R.layout.invest_title_view, null);
        pageName = (TextView) titleView.findViewById(R.id.invest_name);
        investMoney = (TextView) titleView.findViewById(R.id.tv_money);
        listView.addHeaderView(titleView);
    }

    @Override
    public void setUpData() {
        if (type == 0) {
            pageName.setText(R.string.investing_value);
        } else if (type == 1) {
            pageName.setText(R.string.investing_back_value);
        }

        showLoadingAnimation();
        getData(0);
    }


    private void getData(int up_down) {
        httpRequest(Params.getInvestListParams(this.getActivity(), this.type, 1, 10), Methods.METHOD_USER_INVESTMENT_LIST, Environments.PV1, InvestmentRecordEntity.class, new DataReuqestCallback(up_down));
    }

    class DataReuqestCallback implements RequestCallback {
        private int type;

        public DataReuqestCallback(int up_down) {
            this.type = up_down;
        }

        @Override
        public void onRequestFailed(String msg) {
            stopLoad();
            closeLoadingAnimation();
        }

        @Override
        public void onRequestSucess(BaseEntity result, String jsonData) {
            stopLoad();
            closeLoadingAnimation();
            if (result.isSucess()) {
                if (result instanceof InvestmentRecordEntity) {
                    if (dataList == null) {
                        dataList = new ArrayList<>();
                    }
                    dataEntity = (InvestmentRecordEntity) result;
                    List<InvestmentRecordItemEntity> tempList = dataEntity.getList();
                    if (type == 0) {
                        dataList = tempList;
                    } else if (type == 1) {
                        if (tempList != null) {
                            dataList.addAll(tempList);
                        }

                        if (tempList == null && tempList.size() < 10) {
                            listView.setPullLoadEnable(false);
                        } else {
                            listView.setPullLoadEnable(true);
                        }
                    }

                    adapter.updateList(dataList);

                    showMoney();
                }
            }
        }

        private void stopLoad() {
            if (type == 0) {
                listView.stopRefresh();
            } else {
                listView.stopLoadMore();
            }
        }
    }

    private void showMoney() {
        if (dataEntity != null) {
            investMoney.setText(String.valueOf(dataEntity.getMoney()));
        }
    }

    @Override
    public void onRefresh() {
        getData(0);
    }

    @Override
    public void onLoadMore() {
        getData(1);
    }
}
