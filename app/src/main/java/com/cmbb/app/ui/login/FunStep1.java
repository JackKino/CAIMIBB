package com.cmbb.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.emum.StepType;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.more.ActivityArgument;
import com.cmbb.app.ui.more.ActivityMoreWebView;
import com.cmbb.app.views.widget.EditLayout;

/**
 * Created by Storm on 2015/11/22.
 */
public class FunStep1 extends BaseActivity implements View.OnClickListener, RequestCallback {
    private EditLayout editMobile;
    private int type;
    private CheckBox check_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd_step1);
    }

    @Override
    public void setUpViews() {
        type = getIntent().getExtras().getInt("type", StepType.TYPE_REGIST);
        setTitle();
        editMobile = (EditLayout) findViewById(R.id.edit_mobile);
        editMobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        editMobile.setMaxLengh(11);
        editMobile.drawableLeft(R.mipmap.login_mobile);
        editMobile.setHide(R.string.login_mobile_hide);
        findViewById(R.id.next_step).setOnClickListener(this);
        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Tools.hideKeyBoard(FunStep1.this, editMobile.getEditText());
                return false;
            }
        });
        findViewById(R.id.regist_des).setOnClickListener(this);
        findViewById(R.id.persional_des).setOnClickListener(this);
        check_box = (CheckBox) findViewById(R.id.check_box);
    }

    private void setTitle() {
        switch (type) {
            case StepType.TYPE_FORGET_PWD:
                initTitle(R.string.login_forget_pwd2, true);
                findViewById(R.id.special_view).setVisibility(View.GONE);
                break;
            case StepType.TYPE_REGIST:
                initTitle(R.string.loing_regist_title, true);
                findViewById(R.id.special_view).setVisibility(View.VISIBLE);
                break;
            case StepType.TYPE_UPDATE_PWD:
                initTitle(R.string.business_pwd_step1, true);
                findViewById(R.id.special_view).setVisibility(View.GONE);
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
            case R.id.regist_des:
                toArgument(1);
                break;
            case R.id.persional_des:
                toArgument(2);
                break;
        }
    }

    private void toArgument(int type) {
        if (type == 1) {
            toWebView(Environments.ARGUMENT1, getString(R.string.forget_text_des1));
        } else if (type == 2) {
            toWebView(Environments.ARGUMENT2, getString(R.string.forget_text_des2));
        }
    }

    private void toWebView(String url, String title) {
        Intent intent = new Intent(this, ActivityMoreWebView.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
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
        switch (type) {
            case StepType.TYPE_FORGET_PWD:
            case StepType.TYPE_REGIST:
            case StepType.TYPE_UPDATE_PWD:
                toRegist();
                break;
            default:
                break;
        }
    }

    private void toRegist() {
        String mobile = editMobile.getText().toString();
        if (Tools.isEmpty(mobile)) {
            doToast(R.string.login_mobile_hide);
            return;
        }
        if (!Tools.checkMoile(mobile)) {
            doToast(R.string.login_mobile_error);
            return;
        }
        if (type == StepType.TYPE_REGIST) {
            if (!check_box.isChecked()) {
                doToast(R.string.argument);
                return;
            }
        }

        showLoadingDialog(null, false);
        switch (type) {
            case StepType.TYPE_FORGET_PWD:
                httpRequest(Params.forgetPwdStep1(this, mobile, 0), Methods.METHOD_FORGET_PWD_STEP1, Environments.PV1, BaseEntity.class, this);
                break;
            case StepType.TYPE_REGIST:
                httpRequest(Params.getRegistStep1(this, mobile), Methods.METHOD_REGEDIT_STEP1, Environments.PV1, BaseEntity.class, this);
                break;
            case StepType.TYPE_UPDATE_PWD:
                httpRequest(Params.forgetPwdStep1(this, mobile, 1), Methods.METHOD_FORGET_PWD_STEP1, Environments.PV1, BaseEntity.class, this);
                break;
        }
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        closeLoadingDialog();
        if (result.isSucess()) {
            String mobile = editMobile.getText().toString();
            Intent intent = new Intent(this, FunStep2.class);
            intent.putExtra("type", type);
            intent.putExtra("mobile", mobile);
            startActivityForResult(intent, 100);
        }
        doToast(result.getMsg());
    }

    @Override
    public void onRequestFailed(String msg) {
        closeLoadingDialog();
        doToast(msg);
    }
}
