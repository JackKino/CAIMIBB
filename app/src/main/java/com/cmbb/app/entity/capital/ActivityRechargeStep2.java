package com.cmbb.app.entity.capital;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.EditLayout;

/**
 * Created by admin on 2015/12/31.
 */
public class ActivityRechargeStep2 extends BaseActivity implements View.OnClickListener, RequestCallback {
    private EditLayout editCode;
    private String money;
    private String related;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd_step2);
    }

    @Override
    public void setUpViews() {
        Bundle bundle = getIntent().getExtras();
        money = bundle.getString("money");
        related = bundle.getString("related");

        setTitle();
        TextView tip = (TextView) findViewById(R.id.mobile_code_tip);
        tip.setText(getString(R.string.forget_mobile_title, Tools.getHideString(ShareConfig.getUserAccount(this).getMobile(), 3, 4)));
        editCode = (EditLayout) findViewById(R.id.edit_code);
        editCode.setMaxLengh(6);
        editCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        editCode.drawableLeft(R.mipmap.icon_mobile_code);
        editCode.setHide(R.string.forget_mobile_code_hide);
        findViewById(R.id.next_step).setOnClickListener(this);
        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Tools.hideKeyBoard(ActivityRechargeStep2.this, editCode.getEditText());
                return false;
            }
        });
    }

    private void setTitle() {
        initTitle(R.string.login_forget_pwd_code_input, true);
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step:
                nextStep();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void nextStep() {
        String code = editCode.getText();
        if (Tools.isEmpty(code)) {
            doToast(R.string.forget_mobile_code_hide);
            return;
        }

       /* if (Tools.checkStrLenRight(code, 6, 6)) {
            doToast(R.string.forget_mobile_code_hide);
            return;
        }*/
        if(code.length()!=6){
            doToast(R.string.forget_mobile_code_hide);
            return;
        }

        showLoadingDialog(getString(R.string.recharge_ing), false);
        httpRequest(Params.rechargeStep2Params(this, money, related, code), Methods.METHOD_RECHARGE_STEP2, Environments.PV1, BaseEntity.class, this);
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        closeLoadingDialog();
        if (result.isSucess()) {
            showLoadingSucessDialog(getString(R.string.recharge_recharge_sucess), false, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            doToast(result.getMsg());
        }
    }


    @Override
    public void onRequestFailed(String msg) {
        closeLoadingDialog();
        doToast(msg);
    }
}
