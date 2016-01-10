package com.cmbb.app.ui.capital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.emum.StepType;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.login.LoginAccountEntity;
import com.cmbb.app.entity.mine.SafetyAccount;
import com.cmbb.app.entity.mine.SafetyEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.certification.ActivityCertificationStep1;
import com.cmbb.app.ui.login.FunStep1;
import com.cmbb.app.user.UserManager;

/**
 * Created by Storm on 2015/11/22.
 * 安全中心，也就是个人信息中心
 */
public class ActivityUserInfo extends BaseActivity implements View.OnClickListener {
    private TextView tvBankNo, tvId, tvNickName, tvUserName;
    private SafetyAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.my_safer_center, true);
        findViewById(R.id.user_id_view).setOnClickListener(this);
        findViewById(R.id.user_nickname_view).setOnClickListener(this);
        findViewById(R.id.user_name_view).setOnClickListener(this);
        findViewById(R.id.user_bank_view).setOnClickListener(this);
        findViewById(R.id.user_loginpwd_view).setOnClickListener(this);
        findViewById(R.id.user_business_pwd_view).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        tvBankNo = (TextView) findViewById(R.id.tv_bank_no);
        tvId = (TextView) findViewById(R.id.tv_id);
        tvNickName = (TextView) findViewById(R.id.tv_nick_name);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
    }

    @Override
    public void setUpData() {

    }

    private void getNewData() {
        showLoadingDialog(getString(R.string.user_info_refreshing), false);
        httpRequest(Params.getSafetyCenter(this), Methods.METHOD_SAFETY_CENTER, Environments.PV1, SafetyEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                closeLoadingDialog();
                if (result.isSucess()) {
                    SafetyEntity safety = (SafetyEntity) result;
                    showUserInfo(safety.getAccount());
                } else {
                    showLoadingFailedView();
                }
            }

            @Override
            public void onRequestFailed(String msg) {
                closeLoadingDialog();
                showLoadingFailedView();
            }
        });
    }

    @Override
    protected void onReloadViewClicked(View view) {
        super.onReloadViewClicked(view);
        getNewData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNewData();
    }

    private void showUserInfo(SafetyAccount account) {
        if (account != null) {
            this.account = account;
            tvBankNo.setText(account.getBank_no());
            tvId.setText(account.getPid());
            LoginAccountEntity accountEntity = ShareConfig.getUserAccount(this);
            tvNickName.setText(accountEntity.getMobile());
            if (Tools.isEmpty(account.getName())) {
                tvUserName.setText(R.string.user_info_unbind);
                tvUserName.setTextColor(getResources().getColor(R.color.color_app_base_color));
            } else {
                tvUserName.setText(account.getName());
                tvUserName.setTextColor(getResources().getColor(R.color.color_666666));
            }

            if (Tools.isEmpty(account.getPid())) {
                tvId.setText(R.string.user_info_unbind);
                tvId.setTextColor(getResources().getColor(R.color.color_app_base_color));
            } else {
                tvId.setText(account.getPid());
                tvId.setTextColor(getResources().getColor(R.color.color_666666));
            }

            if (Tools.isEmpty(account.getBank_no())) {
                tvBankNo.setText(R.string.user_info_unbind);
                tvBankNo.setTextColor(getResources().getColor(R.color.color_app_base_color));
            } else {
                tvBankNo.setText(account.getBank_no());
                tvBankNo.setTextColor(getResources().getColor(R.color.color_666666));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                ShareConfig.saveUserAccount(this, null);
                finish();
                break;
            case R.id.user_loginpwd_view:
                toUpdatePwd(StepType.TYPE_UPDATE_LOGIN_PWD);
                break;
            case R.id.user_business_pwd_view:
//                resetBusinessPwd(StepType.TYPE_UPDATE_PWD);
                if (!toBind()) {
                    toUpdatePwd(StepType.TYPE_UPDATE_BUSINESS_PWD);
                }
                break;
            case R.id.user_id_view:
            case R.id.user_bank_view:
            case R.id.user_name_view:
                toBind();
                break;
        }
    }


    private boolean toBind() {
        if (account != null) {
            if (Tools.isEmpty(account.getPid()) && Tools.isEmpty(account.getBank_no()) && Tools.isEmpty(account.getName())) {
                startActivity(ActivityCertificationStep1.class);
                return true;
            }
        }
        return false;
    }

    private void toUpdatePwd(int type) {
        if (StepType.TYPE_UPDATE_LOGIN_PWD == type || !toBind() && type == StepType.TYPE_UPDATE_BUSINESS_PWD) {
            Intent intent = new Intent(this, ActivityUpdatePwd.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    private void resetBusinessPwd(int type) {
        Intent intent = new Intent(this, FunStep1.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, 100);
    }
}
