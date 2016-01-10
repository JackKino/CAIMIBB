package com.cmbb.app.ui.capital;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.adapter.mine.CaptialAdapter;
import com.cmbb.app.application.CMBBApplication;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.capital.CapitalFlowEntity;
import com.cmbb.app.entity.capital.CapitalFlowItemEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.xlist.XListView;
import com.pulltorefresh.view.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Storm on 2015/11/24.
 * 资金流水
 */
public class ActivityCapitalFlow extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {
    private XListView listView;
    private PullableListView pullableListView;
    private CaptialAdapter captialAdapter;
    private View topView;
    private int currentType = 0;
    private CapitalFlowEntity flowData;
    private List<CapitalFlowItemEntity> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_flow);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.captial_item_all, true);
        findViewById(R.id.top_menu).setOnClickListener(this);
        topView = findViewById(R.id.top_view);
        listView = (XListView) findViewById(R.id.list_view);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(false);
        listView.setAdapter(captialAdapter = new CaptialAdapter(this));
        listView.setXListViewListener(this);
    }

    @Override
    public void setUpData() {
        showLoadingAnimation();
        getData(0);
    }

    private void getData(int up_down) {
        int current_page = 1;
        if (up_down == 1 && flowData != null) {
            current_page = flowData.getCurrent_page() + 1;
        }
        httpRequest(Params.getInvestFlowParams(this, currentType, current_page, 10), Methods.METHOD_USER_INVESTMENT_FLOW, Environments.PV1, CapitalFlowEntity.class, new DataReuqestCallback(up_down, currentType));
    }

    class DataReuqestCallback implements RequestCallback {
        private int type;
        private int dataType;

        public DataReuqestCallback(int up_down, int dataType) {
            this.type = up_down;
            this.dataType = dataType;
        }

        @Override
        public void onRequestFailed(String msg) {
            closeLoadingAnimation();
            doToast(msg);
            stopLoad();
        }

        @Override
        public void onRequestSucess(BaseEntity result, String jsonData) {
            closeLoadingAnimation();
            stopLoad();
            if (dataType == currentType) {
                if (result.isSucess()) {
                    if (result instanceof CapitalFlowEntity) {
                        CapitalFlowEntity data = (CapitalFlowEntity) result;
                        if (data != null) {
                            List<CapitalFlowItemEntity> temp = data.getList();
                            if (temp != null && temp.size() > 0) {
                                flowData = data;
                                refreshData(temp);
                            }
                            if (temp == null || temp.size() < 10) {
                                listView.setPullLoadEnable(false);
                            } else {
                                listView.setPullLoadEnable(true);
                            }
                        }

                        listNull();
                    }
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

        private void refreshData(List<CapitalFlowItemEntity> list) {
            if (list != null) {
                if (dataList == null) {
                    dataList = new ArrayList<CapitalFlowItemEntity>();
                }
                if (type == 0) {
                    dataList = list;
                } else {
                    dataList.addAll(list);
                }
                captialAdapter.updateList(dataList);
            }

            if (dataList == null || dataList.size() == 0) {
                doToast(R.string.common_no_data);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_menu:
                showMenuPop();
                break;
        }
    }

    private PopupWindow popupWindow = null;

    private void showMenuPop() {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.captial_pop_view, null);
        contentView.findViewById(R.id.type0).setOnClickListener(menuOnClicked);
        contentView.findViewById(R.id.type1).setOnClickListener(menuOnClicked);
        contentView.findViewById(R.id.type2).setOnClickListener(menuOnClicked);
        contentView.findViewById(R.id.type3).setOnClickListener(menuOnClicked);
        contentView.findViewById(R.id.type4).setOnClickListener(menuOnClicked);
        contentView.findViewById(R.id.type5).setOnClickListener(menuOnClicked);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.setWidth(getResources().getDimensionPixelSize(R.dimen.captial_pop_w));
        popupWindow.showAsDropDown(topView, (CMBBApplication.getInstance().getScreenWidth() - popupWindow.getWidth()) / 2, 0);
    }

    View.OnClickListener menuOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int currentType = 0;
            switch (v.getId()) {
                case R.id.type0:
                    currentType = 0;
                    break;
                case R.id.type1:
                    currentType = 1;
                    break;
                case R.id.type2:
                    currentType = 2;
                    break;
                case R.id.type3:
                    currentType = 3;
                    break;
                case R.id.type4:
                    currentType = 4;
                    break;
                case R.id.type5:
                    currentType = 5;
                    break;
                default:
                    break;
            }
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            if (ActivityCapitalFlow.this.currentType != currentType) {
                /**
                 * 清理数据
                 */
                if (dataList != null) {
                    dataList.clear();
                }
                captialAdapter.updateList(dataList);
                setUpData();
            }
        }
    };

    private void listNull() {
        if (captialAdapter.getCount() == 0) {
            findViewById(R.id.null_tip).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.null_tip).setVisibility(View.GONE);
        }
    }
}
