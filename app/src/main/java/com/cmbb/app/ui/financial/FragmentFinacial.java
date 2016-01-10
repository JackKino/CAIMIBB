package com.cmbb.app.ui.financial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.adapter.financial.FinancialAdapter;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.financial.FinancialEntity;
import com.cmbb.app.entity.financial.FinancialItemEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseFragment;
import com.cmbb.app.ui.capital.ActivityProjectDetail;
import com.cmbb.app.views.xlist.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Storm on 2015/11/21.
 * 我要理财
 */
public class FragmentFinacial extends BaseFragment implements XListView.IXListViewListener {
    private XListView listView;
    private FinancialEntity financialData;
    private List<FinancialItemEntity> dataList = new ArrayList<FinancialItemEntity>();
    private FinancialAdapter f_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initFragmentView(inflater, container, R.layout.activity_financial_fragment);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.main_menu_title3, false);
        listView = (XListView) findViewById(R.id.list_view);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setAdapter(f_adapter = new FinancialAdapter(this.getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                FinancialItemEntity item = f_adapter.getItem(position - 1);
                if (item != null) {
                    Intent intent = new Intent(FragmentFinacial.this.getActivity(), ActivityProjectDetail.class);
                    intent.putExtra("item", item);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void setUpData() {
        showLoadingAnimation();
        getData(0);
    }

    private void getData(int type) {
        int current_page = 1;
        if (type == 1 && financialData != null) {
            current_page = financialData.getCurrent_page() + 1;
        }
        httpRequest(Params.getFinancialListParams(this.getActivity(), current_page, 10), Methods.METHOD_PRODUCT_LIST, Environments.PV1, FinancialEntity.class, new ReqeustData(type));
    }

    class ReqeustData implements RequestCallback {
        private int type;

        public ReqeustData(int type) {
            this.type = type;
        }

        @Override
        public void onRequestSucess(BaseEntity result, String jsonData) {
            closeLoadingAnimation();
            if (result.isSucess()) {
                FinancialEntity data = (FinancialEntity) result;
                if (data != null) {
                    List<FinancialItemEntity> tempList = data.getList();
                    if (tempList != null && tempList.size() > 0) {
                        financialData = data;
                        dealData(tempList);
                    }
                    if (tempList == null || tempList.size() < 10) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                }
            }

            stopRefresh();
        }

        @Override
        public void onRequestFailed(String msg) {
            closeLoadingAnimation();
            doToast(msg);
            stopRefresh();
        }

        /**
         * 数据分组
         *
         * @param tempList
         */
        private void dealData(List<FinancialItemEntity> tempList) {
            if (type == 0) {
                dataList = tempList;
            } else if (type == 1) {
                dataList.addAll(tempList);
            }
            f_adapter.updateList(dataList);
        }

        private void stopRefresh() {
            if (type == 0) {
                listView.stopRefresh();
            } else {
                listView.stopLoadMore();
            }
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
