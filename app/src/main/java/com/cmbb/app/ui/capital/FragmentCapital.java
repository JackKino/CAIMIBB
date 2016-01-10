package com.cmbb.app.ui.capital;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.login.LoginAccountEntity;
import com.cmbb.app.entity.login.UserAssetEntity;
import com.cmbb.app.entity.mine.SafetyAccount;
import com.cmbb.app.entity.mine.SafetyEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseFragment;
import com.cmbb.app.ui.certification.ActivityCertificationStep1;
import com.cmbb.app.ui.recharge.ActivityRecharge;
import com.cmbb.app.user.UserManager;

import java.util.logging.SocketHandler;

/**
 * 资产TAB页
 * Created by Storm on 2015/11/21.
 */
public class FragmentCapital extends BaseFragment implements View.OnClickListener, RequestCallback {
    private TextView userName;
    private ImageView headIcon;
    private LoginAccountEntity userAccount;
    private TextView ticketCount, income, certification;
    private boolean viewLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initFragmentView(inflater, container, R.layout.activity_mymoney_fragment);
    }

    @Override
    public void setUpViews() {
        userAccount = ShareConfig.getUserAccount(this.getActivity());
        findViewById(R.id.my_invest_flow).setOnClickListener(this);
        findViewById(R.id.my_invest_history).setOnClickListener(this);
        findViewById(R.id.my_recharge).setOnClickListener(this);
        certification = (TextView) findViewById(R.id.my_add_to);
        certification.setOnClickListener(this);
        findViewById(R.id.my_ticket_icon).setOnClickListener(this);
        findViewById(R.id.my_ticket_value).setOnClickListener(this);
        findViewById(R.id.my_income_icon).setOnClickListener(this);
        findViewById(R.id.my_income_value).setOnClickListener(this);
        userName = (TextView) findViewById(R.id.user_name);
        headIcon = (ImageView) findViewById(R.id.iv_headicon);
        ticketCount = (TextView) findViewById(R.id.my_ticket_count);
        income = (TextView) findViewById(R.id.my_income);
        userName.setOnClickListener(this);
        headIcon.setOnClickListener(this);
        setUserAccountStatu();
        viewLoaded = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewLoaded) {
            refreshData();
        }
    }

    @Override
    protected void visibleChanged(boolean isVisible) {
        super.visibleChanged(isVisible);
        if (isVisible && viewLoaded) {
            refreshData();
        }
    }

    @Override
    public void setUpData() {
        showBasicValue();
    }

    private void refreshData() {
        if (UserManager.isLogin(this.getActivity())) {
            httpRequest(Params.getCapitalParams(this.getActivity()), Methods.METHOD_USER_ASSETS, Environments.PV1, UserAssetEntity.class, this);
        }
    }

    private void showBasicValue() {
        LoginAccountEntity loginAccountEntity = ShareConfig.getUserAccount(this.getActivity());
        if (loginAccountEntity != null) {
            ticketCount.setText(getString(R.string.my_ticket_count, loginAccountEntity.getCoupon()));
            income.setText(getString(R.string.my_income, loginAccountEntity.getBalance()));
            String name = loginAccountEntity.getName();
            if (!Tools.isEmpty(name)) {
                certification.setText(name);
            } else {
                certification.setText(R.string.main_product_add);
            }
        } else {
            ticketCount.setText(getString(R.string.my_ticket_count, 0));
            income.setText(getString(R.string.my_income, 0));
            certification.setText(R.string.main_product_add);
        }
    }

    @Override
    public void onUserAccountChanged(LoginAccountEntity account) {
        super.onUserAccountChanged(account);
        if (isLoaded) {
            userAccount = account;
            setUserAccountStatu();
            showBasicValue();
        }
    }

    private void setUserAccountStatu() {
        if (userAccount != null) {
            headIcon.setImageResource(R.mipmap.icon_default_light);
            userName.setText(Tools.getHideString(userAccount.getMobile(), 3, 4));
        } else {
            headIcon.setImageResource(R.mipmap.icon_default_gray);
            userName.setText(R.string.my_money_login);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_ticket_icon:
            case R.id.my_ticket_value:
                toWallet(0);
                break;
            case R.id.my_income_icon:
            case R.id.my_income_value:
                toWallet(1);
                break;
            case R.id.my_invest_history:
                toActivity(ActivityInvestList.class);
                break;
            case R.id.my_invest_flow:
                toActivity(ActivityCapitalFlow.class);
                break;
            case R.id.my_recharge:
                toRecharge();
                break;
            case R.id.my_add_to:
                addTo();
                break;
            case R.id.user_name:
            case R.id.iv_headicon:
                checkToLogin();
                break;
        }
    }

    private void toRecharge() {
        getNewData();
    }

    private void toActivity(Class cls) {
        if (UserManager.isLogin(this.getActivity())) {
            startActivity(cls);
        } else {
            toLogin();
        }
    }

    private void checkToLogin() {
        //跳转到用户登录界面

        if (!UserManager.isLogin(this.getActivity())) {
            toLogin();
        } else {
            //已经登录，跳转到安全中心界面
            startActivity(ActivityUserInfo.class);
        }
    }

    private void addTo() {
        if (UserManager.isLogin(this.getActivity())) {
            startActivity(ActivityUserInfo.class);
        } else {
            toLogin();
        }
    }

    private void toWallet(int index) {
        if (UserManager.isLogin(this.getActivity())) {
            Intent intent = new Intent(this.getActivity(), ActivityMyWallet.class);
            intent.putExtra("index", index);
            startActivity(intent);
        } else {
            toLogin();
        }
    }

    @Override
    public void onRequestFailed(String msg) {

    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        if (result.isSucess()) {
            if (result instanceof UserAssetEntity) {
                UserAssetEntity asset = (UserAssetEntity) result;
                if (asset != null) {
                    LoginAccountEntity account = ShareConfig.getUserAccount(this.getActivity());
                    account.setBalance(asset.getBalance());
                    account.setIncome(asset.getIncome());
                    account.setCoupon(asset.getCoupon());
                    ShareConfig.saveUserAccount(this.getActivity(), account);
                    showBasicValue();
                }
            }
        }
    }


    private void getNewData() {
        showLoadingDialog(getString(R.string.user_info_get), false);
        httpRequest(Params.getSafetyCenter(this.getActivity()), Methods.METHOD_SAFETY_CENTER, Environments.PV1, SafetyEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                closeLoadingDialog();
                if (result.isSucess()) {
                    SafetyEntity safety = (SafetyEntity) result;
                    if (safety != null && safety.getAccount() != null) {
                        SafetyAccount account = safety.getAccount();
                        if (account.hasBindBank()) {
                            toRechargePage(safety);
                        } else {
                            startActivity(ActivityCertificationStep1.class);
                        }
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
        });
    }

    private void toRechargePage(SafetyEntity safety) {
        LoginAccountEntity loginAccountEntity = ShareConfig.getUserAccount(this.getActivity());
        Intent intent = new Intent(this.getActivity(), ActivityRecharge.class);
        intent.putExtra("type", 1);
        intent.putExtra("balance", Double.valueOf(loginAccountEntity.getBalance()));
        intent.putExtra("data", safety);
        startActivity(intent);
    }
}
