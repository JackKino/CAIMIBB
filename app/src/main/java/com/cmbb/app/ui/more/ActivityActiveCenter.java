package com.cmbb.app.ui.more;

import android.os.Bundle;
import android.view.View;

import com.cmbb.app.R;
import com.cmbb.app.adapter.more.ActiveAdapter;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.main.MainFragmentActivity;
import com.cmbb.app.views.xlist.XListView;

/**
 * Created by Storm on 2015/11/21.
 */
public class ActivityActiveCenter extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    private XListView listView;
    private ActiveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_active_center);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.more_message_center, true);
        findViewById(R.id.active_add_to).setOnClickListener(this);
        listView = (XListView) findViewById(R.id.list_view);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this);
        listView.setAdapter(adapter = new ActiveAdapter(this));
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.active_add_to:
                MainFragmentActivity.getInstance().toFinancial();
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        listView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        listView.stopLoadMore();
    }
}
