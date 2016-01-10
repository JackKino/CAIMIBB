package com.cmbb.app.ui.login;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Logger;
import com.cmbb.app.action.common.MD5;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.emum.StepType;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.login.LoginResultEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.EditLayout;

/**
 * Created by Storm on 2015/11/22.
 */
public class FunStep3 extends BaseActivity implements View.OnClickListener, RequestCallback {
    private EditLayout pwd_edit_1, pwd_edit_2;
    private int type;
    private String mobile;
    private String md5Pwd;
    private TextView tv_pwd_title, tv_repwd_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd_step3);
    }

    @Override
    public void setUpViews() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type", StepType.TYPE_REGIST);
        mobile = bundle.getString("mobile");
        tv_pwd_title = (TextView) findViewById(R.id.tv_pwd_title);
        tv_repwd_title = (TextView) findViewById(R.id.tv_re_pwd_title);
        setTitle();
        pwd_edit_1 = (EditLayout) findViewById(R.id.edit_pwd1);
        pwd_edit_1.setHide(R.string.forget_mobile_reset_pwd_hide);
        pwd_edit_1.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        pwd_edit_1.setMaxLengh(16);
        pwd_edit_2 = (EditLayout) findViewById(R.id.edit_pwd2);
        pwd_edit_2.setHide(R.string.forget_mobile_reset_pwd_hide);
        pwd_edit_2.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        pwd_edit_2.setMaxLengh(16);
        pwd_edit_1.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        pwd_edit_2.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        findViewById(R.id.commit).setOnClickListener(this);
        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Tools.hideKeyBoard(FunStep3.this, pwd_edit_1.getEditText());
                return false;
            }
        });
    }

    private void setTitle() {
        switch (type) {
            case StepType.TYPE_FORGET_PWD:
                initTitle(R.string.forget_mobile_reset_pwd, true);
                tv_pwd_title.setText(R.string.forget_mobile_reset_pwd_1);
                break;
            case StepType.TYPE_REGIST:
                initTitle(R.string.loing_regist_pwd_title, true);
                tv_pwd_title.setText(R.string.business_pwd_step3_regist_pwd);
                tv_repwd_title.setText(R.string.business_pwd_step3_regist_re_pwd);
                break;
            case StepType.TYPE_UPDATE_PWD:
                initTitle(R.string.business_pwd_step3, true);
                tv_pwd_title.setText(R.string.business_pwd_step3_new_pwd);
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
            case R.id.commit:
                commit();
                break;
        }
    }

    private void commit() {
        String pwd = pwd_edit_1.getText();
        String rePwd = pwd_edit_2.getText();
        if (Tools.isEmpty(pwd) || Tools.isEmpty(rePwd)) {
            doToast(R.string.forget_mobile_reset_pwd_hide);
            return;
        }

        Logger.i(tag, pwd + "   " + rePwd);
        if (!pwd.equals(rePwd)) {
            doToast(R.string.forget_mobile_pwd_unequal);
            return;
        }

        if (!Tools.checkStrLenRight(pwd, 6, 16)) {
            doToast(R.string.forget_mobile_reset_pwd_hide);
            return;
        }

        if (Tools.isNumbers(pwd)) {
            doToast(R.string.forget_pwd_too_simple);
            return;
        }

        md5Pwd = MD5.MD5(pwd);
        showLoadingDialog(null, false);
        switch (type) {
            case StepType.TYPE_FORGET_PWD:
                httpRequest(Params.forgetPwdStep3(this, mobile, md5Pwd, 0), Methods.METHOD_FORGET_PWD_STEP3, Environments.PV1, BaseEntity.class, this);
                break;
            case StepType.TYPE_REGIST:
                httpRequest(Params.getRegistStep3(this, mobile, md5Pwd), Methods.METHOD_REGEDIT_STEP3, Environments.PV1, LoginResultEntity.class, this);
                break;
            case StepType.TYPE_UPDATE_PWD:
                httpRequest(Params.forgetPwdStep3(this, mobile, md5Pwd, 1), Methods.METHOD_FORGET_PWD_STEP3, Environments.PV1, BaseEntity.class, this);
                break;
        }
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        closeLoadingDialog();
        doToast(result.getMsg());
        if (result.isSucess()) {
            switch (type) {
                case StepType.TYPE_FORGET_PWD: {
                    setResult(RESULT_OK);
                    finish();
                    break;
                }
                case StepType.TYPE_REGIST:
                    if (result instanceof LoginResultEntity) {
                        registSucess((LoginResultEntity) result);
                    }
                    break;
                case StepType.TYPE_UPDATE_PWD:
                    break;
            }
        }
    }

    @Override
    public void onRequestFailed(String msg) {
        closeLoadingDialog();
        doToast(msg);
    }

    private void registSucess(LoginResultEntity entity) {
        if (entity.getAccount() != null) {
            entity.getAccount().setPasswd(md5Pwd);
            ShareConfig.saveUserAccount(this, entity.getAccount());
            setResult(RESULT_OK);
            finish();
        }
    }
}
