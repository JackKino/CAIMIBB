package com.cmbb.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.emum.StepType;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.EditLayout;

/**
 * Created by Storm on 2015/11/22.
 */
public class FunStep2 extends BaseActivity implements View.OnClickListener, RequestCallback {
    private EditLayout editCode;
    private int type;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd_step2);
    }

    @Override
    public void setUpViews() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type", StepType.TYPE_REGIST);
        mobile = bundle.getString("mobile");
        setTitle();
        TextView tip = (TextView) findViewById(R.id.mobile_code_tip);
        tip.setText(getString(R.string.forget_mobile_title, Tools.getHideString(mobile, 3, 4)));
        editCode = (EditLayout) findViewById(R.id.edit_code);
        editCode.setMaxLengh(6);
        editCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        editCode.drawableLeft(R.mipmap.icon_mobile_code);
        editCode.setHide(R.string.forget_mobile_code_hide);
        findViewById(R.id.next_step).setOnClickListener(this);
        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Tools.hideKeyBoard(FunStep2.this, editCode.getEditText());
                return false;
            }
        });
    }

    private void setTitle() {
        switch (type) {
            case StepType.TYPE_FORGET_PWD:
                initTitle(R.string.login_forget_pwd_code_input, true);
                break;
            case StepType.TYPE_REGIST:
                initTitle(R.string.login_forget_pwd_code_input, true);
                break;
            case StepType.TYPE_UPDATE_PWD:
                initTitle(R.string.login_forget_pwd_code_input, true);
                break;
            default:
                break;
        }
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

        showLoadingDialog(null, false);
        switch (type) {
            case StepType.TYPE_FORGET_PWD:
                httpRequest(Params.forgetPwdStep2(this, mobile, code, 0), Methods.METHOD_FORGET_PWD_STEP2, Environments.PV1, BaseEntity.class, this);
                break;
            case StepType.TYPE_REGIST:
                httpRequest(Params.getRegistStep2(this, mobile, code), Methods.METHOD_REGEDIT_STEP2, Environments.PV1, BaseEntity.class, this);
                break;
            case StepType.TYPE_UPDATE_PWD:
                httpRequest(Params.forgetPwdStep2(this, mobile, code, 1), Methods.METHOD_FORGET_PWD_STEP2, Environments.PV1, BaseEntity.class, this);
                break;
        }
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        closeLoadingDialog();
        if (result.isSucess()) {
            Intent intent = new Intent(this, FunStep3.class);
            intent.putExtra("type", type);
            intent.putExtra("mobile", mobile);
            startActivityForResult(intent, 100);
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
