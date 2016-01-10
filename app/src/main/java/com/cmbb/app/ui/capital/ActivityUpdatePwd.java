package com.cmbb.app.ui.capital;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.MD5;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.emum.StepType;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.EditLayout;

/**
 * Created by Storm on 2015/11/22.
 * 修改密码
 */
public class ActivityUpdatePwd extends BaseActivity implements View.OnClickListener {
    private EditLayout oldPwdEdit, newPwdEdit, reNewPwdEdit;
    private boolean newPwdShow = true, reNewPwdShow = true;
    private ImageView newPwdShowImage, reNewPwdShowImage;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
    }

    @Override
    public void setUpViews() {
        type = getIntent().getExtras().getInt("type");
        if (type == StepType.TYPE_UPDATE_LOGIN_PWD) {
            initTitle(R.string.user_info_pwd_udpate, true);
        } else {
            initTitle(R.string.user_info_business_pwd_udpate, true);
        }
        oldPwdEdit = (EditLayout) findViewById(R.id.old_pwd_edit);
        newPwdEdit = (EditLayout) findViewById(R.id.new_pwd_edit);
        reNewPwdEdit = (EditLayout) findViewById(R.id.re_new_pwd_edit);

        if (type == StepType.TYPE_UPDATE_LOGIN_PWD) {
//            oldPwdEdit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
//            newPwdEdit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
//            reNewPwdEdit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            oldPwdEdit.setMaxLengh(16);
            newPwdEdit.setMaxLengh(16);
            reNewPwdEdit.setMaxLengh(16);
            oldPwdEdit.setHide(R.string.forget_mobile_reset_pwd_hide);
            newPwdEdit.setHide(R.string.forget_mobile_reset_new_pwd_hide);
            reNewPwdEdit.setHide(R.string.forget_mobile_reset_re_new_pwd_hide);
            oldPwdEdit.setDegit(getString(R.string.common_degit));
            newPwdEdit.setDegit(getString(R.string.common_degit));
            reNewPwdEdit.setDegit(getString(R.string.common_degit));
        } else {
            oldPwdEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            newPwdEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            reNewPwdEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            oldPwdEdit.setDegit(getString(R.string.common_degit_number));
            newPwdEdit.setDegit(getString(R.string.common_degit_number));
            reNewPwdEdit.setDegit(getString(R.string.common_degit_number));
            oldPwdEdit.setMaxLengh(6);
            newPwdEdit.setMaxLengh(6);
            reNewPwdEdit.setMaxLengh(6);
            oldPwdEdit.setHide(R.string.forget_mobile_reset_pwd_hide1);
            newPwdEdit.setHide(R.string.forget_mobile_reset_new_pwd_hide1);
            reNewPwdEdit.setHide(R.string.forget_mobile_reset_re_new_pwd_hide1);
            oldPwdEdit.setIsPassword(true);
            newPwdEdit.setIsPassword(true);
            reNewPwdEdit.setIsPassword(true);
        }


        findViewById(R.id.hide_new_pwd).setOnClickListener(this);
        findViewById(R.id.hide_re_new_pwd).setOnClickListener(this);
        findViewById(R.id.update_commit).setOnClickListener(this);
        newPwdShowImage = (ImageView) findViewById(R.id.hide_new_pwd);
        reNewPwdShowImage = (ImageView) findViewById(R.id.hide_re_new_pwd);
        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Tools.hideKeyBoard(ActivityUpdatePwd.this, oldPwdEdit.getEditText());
                return false;
            }
        });
        setPwdShow(newPwdEdit);
        setPwdShow(reNewPwdEdit);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_commit:
                update();
                break;
            case R.id.hide_new_pwd:
                setPwdShow(newPwdEdit);
                break;
            case R.id.hide_re_new_pwd:
                setPwdShow(reNewPwdEdit);
                break;
        }
    }

    private void update() {
        Tools.hideKeyBoard(ActivityUpdatePwd.this, oldPwdEdit.getEditText());
        String oldPwd = oldPwdEdit.getText();
        final String newPwd = newPwdEdit.getText();
        String reNewPwd = reNewPwdEdit.getText();
        if (Tools.isEmpty(oldPwd) || !Tools.checkStrLenRight(oldPwd,6,16)) {
            doToast(R.string.forget_mobile_reset_pwd_hide);
            return;
        }
        if (Tools.isEmpty(newPwd) || !Tools.checkStrLenRight(newPwd,6,16)) {
            doToast(R.string.forget_mobile_reset_new_pwd_hide);
            return;
        }
        if (Tools.isEmpty(reNewPwd)|| !Tools.checkStrLenRight(reNewPwd,6,16)) {
            doToast(R.string.forget_mobile_reset_re_new_pwd_hide);
            return;
        }

        if (!newPwd.equals(reNewPwd)) {
            doToast(R.string.forget_mobile_pwd_unequal);
            return;
        }

        showLoadingDialog(getString(R.string.user_info_update_pwd), false);
        String mobile = ShareConfig.getUserAccount(this).getMobile();
        int type_id = 0;
        if (this.type == StepType.TYPE_UPDATE_BUSINESS_PWD) {
            type_id = 1;
        }
        httpRequest(Params.updatePwdParams(this, mobile, type_id, MD5.MD5(oldPwd), MD5.MD5(newPwd)), Methods.METHOD_RESET_PWD, Environments.PV1, BaseEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                closeLoadingDialog();
                doToast(result.getMsg());
                if (result.isSucess()) {
                    ShareConfig.getUserAccount(ActivityUpdatePwd.this).setPasswd(MD5.MD5(newPwd));
                    finish();
                }
            }

            @Override
            public void onRequestFailed(String msg) {
                closeLoadingDialog();
            }
        });
    }

    private void setPwdShow(EditLayout edit) {
        if (edit == newPwdEdit) {
            if (!newPwdShow) {
                newPwdShowImage.setImageResource(R.mipmap.icon_pwd_show);
            } else {
                newPwdShowImage.setImageResource(R.mipmap.icon_pwd_hide);
            }
            edit.setIsPassword(newPwdShow);
            newPwdShow = !newPwdShow;
        } else {
            if (!reNewPwdShow) {
                reNewPwdShowImage.setImageResource(R.mipmap.icon_pwd_show);
            } else {
                reNewPwdShowImage.setImageResource(R.mipmap.icon_pwd_hide);
            }
            edit.setIsPassword(reNewPwdShow);
            reNewPwdShow = !reNewPwdShow;
        }
    }
}
