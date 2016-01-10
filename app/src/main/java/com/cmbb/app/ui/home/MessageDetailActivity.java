package com.cmbb.app.ui.home;

import android.os.Bundle;

import com.cmbb.app.action.common.Tools;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.dialog.DialogMgr;

/**
 * Created by admin on 2015/12/22.
 * 消息详情
 */
public class MessageDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMessageDialog();
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void setUpViews() {

    }

    private void showMessageDialog() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String message = bundle.getString("extra");
            if (!Tools.isEmpty(message)) {
                DialogMgr.showMessageDialog(this, message, true);
                return;
            }
        }
        finish();
    }
}
