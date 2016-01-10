package com.cmbb.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.MD5;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.emum.StepType;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.login.LoginAccountEntity;
import com.cmbb.app.entity.login.LoginResultEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.EditLayout;

/**
 * Created by Storm on 2015/11/22.
 */
public class ActivityLogin extends BaseActivity implements View.OnClickListener, View.OnTouchListener, RequestCallback {
    private EditLayout et_mobile, et_pwd;
    private ImageView hideShow;
    private boolean showPwd = true;
    private String md5Pwd;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.login_title, true);
        findViewById(R.id.login_forget_pwd).setOnClickListener(this);
        findViewById(R.id.btn_regist).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        et_mobile = (EditLayout) findViewById(R.id.edit_mobile);
        et_mobile.setMaxLengh(11);
        et_pwd = (EditLayout) findViewById(R.id.edit_pwd);
        et_mobile.setHide(R.string.login_mobile_hide);
        et_mobile.drawableLeft(R.mipmap.login_mobile);
        et_mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_pwd.setHide(R.string.login_pwd_hide);
        et_pwd.setMaxLengh(16);
        et_pwd.drawableLeft(R.mipmap.login_pwd);
        et_pwd.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        et_pwd.setDegit(getString(R.string.common_degit));
        hideShow = (ImageView) findViewById(R.id.hide_pwd);
        hideShow.setOnClickListener(this);
        findViewById(R.id.main).setOnTouchListener(this);


       /* et_mobile.setText("13248984056");
        et_pwd.setText("sy123456");*/

        setPwdShow();
    }

    private void setPwdShow() {
        if (showPwd) {
            hideShow.setImageResource(R.mipmap.icon_pwd_hide);
        } else {
            hideShow.setImageResource(R.mipmap.icon_pwd_show);
        }
        et_pwd.setIsPassword(showPwd);
        showPwd = !showPwd;
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_forget_pwd:
                toSteps(StepType.TYPE_FORGET_PWD);
                break;
            case R.id.hide_pwd:
                setPwdShow();
                break;
            case R.id.btn_regist:
                toSteps(StepType.TYPE_REGIST);
                break;
            case R.id.btn_login:
                doLogin();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.main && event.getAction() == MotionEvent.ACTION_DOWN) {
            Tools.hideKeyBoard(this, et_mobile.getEditText());
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            finish();
        }
    }

    private void toSteps(int type) {
        Intent intent = new Intent(this, FunStep1.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, 100);
    }

    private void doLogin() {
        mobile = et_mobile.getText().trim();
        String pwd = et_pwd.getText().trim();
        if (Tools.isEmpty(mobile)) {
            doToast(R.string.login_mobile_hide);
            return;
        }

        if (Tools.isEmpty(pwd)) {
            doToast(R.string.forget_mobile_reset_pwd_hide);
            return;
        }

        if (!Tools.checkStrLenRight(pwd, 6, 16)) {
            doToast(R.string.forget_mobile_reset_pwd_hide);
            return;
        }

        showLoadingDialog(getString(R.string.common_logining), false);
        httpRequest(Params.getLoginParams(this, mobile, md5Pwd = MD5.MD5(pwd)), Methods.METHOD_LOGIN, Environments.PV1, LoginResultEntity.class, this);
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        closeLoadingDialog();
        if (result.isSucess()) {
            if (result instanceof LoginResultEntity) {
                registSucess((LoginResultEntity) result);
            }
        } else {
            doToast(result.getMsg());
        }
    }

    @Override
    public void onRequestFailed(String msg) {
        closeLoadingDialog();
        doToast(msg);
    }

    private void registSucess(LoginResultEntity entity) {
        LoginAccountEntity accont = entity.getAccount();
        if (accont != null) {
            accont.setPasswd(md5Pwd);
            accont.setMobile(mobile);
            ShareConfig.saveUserAccount(this, accont);
            setResult(RESULT_OK);
            finish();
        } else {
            doToast(entity.getMsg());
        }
    }
}
