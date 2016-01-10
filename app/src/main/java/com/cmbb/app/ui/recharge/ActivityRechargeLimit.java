package com.cmbb.app.ui.recharge;

import android.os.Bundle;

import com.cmbb.app.R;
import com.cmbb.app.ui.base.BaseActivity;

/**
 * Created by admin on 2015/12/11.
 */
public class ActivityRechargeLimit extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_limit);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.recharge_recharge_limit_detail, true);
    }

    @Override
    public void setUpData() {

    }
}
