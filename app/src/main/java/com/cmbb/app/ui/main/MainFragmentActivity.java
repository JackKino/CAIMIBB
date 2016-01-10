package com.cmbb.app.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cmbb.app.R;
import com.cmbb.app.action.common.MD5;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.login.LoginAccountEntity;
import com.cmbb.app.entity.login.LoginResultEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseFragmentActivity;
import com.cmbb.app.ui.capital.FragmentCapital;
import com.cmbb.app.ui.dialog.DialogMgr;
import com.cmbb.app.ui.financial.FragmentFinacial;
import com.cmbb.app.ui.home.FragmentHome;
import com.cmbb.app.ui.more.FragmentMore;
import com.umeng.update.UmengUpdateAgent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liman on 2015/11/16.
 * 主界面喽
 */
public class MainFragmentActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener, RequestCallback {
    private final String MENU_HOME = "MENU_HOME", MENU_FINACIAL = "MENU_FINACIAL", MENU_MONEY = "MENU_MONEY", MENU_MORE = "MENU_MORE";
    private RadioGroup radioGroup;
    private RadioButton menuBtn1, menuBtn2, menuBtn3, menuBtn4;
    private static MainFragmentActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main_fragment);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMessageDialog();
            }
        }, 400);
        checkUpdate();
        autoLogin();
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
    }

    public void showMessageDialog() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            String message = bundle.getString("extra");
            if (!Tools.isEmpty(message)) {
                DialogMgr.showMessageDialog(this, message, false);
            }
        }
    }

    public static MainFragmentActivity getInstance() {
        return instance;
    }

    @Override
    public void setUpViews() {
        radioGroup = (RadioGroup) findViewById(R.id.btn_group);
        menuBtn1 = (RadioButton) findViewById(R.id.menu_btn_1);
        menuBtn2 = (RadioButton) findViewById(R.id.menu_btn_2);
        menuBtn3 = (RadioButton) findViewById(R.id.menu_btn_3);
        menuBtn4 = (RadioButton) findViewById(R.id.menu_btn_4);
        menuBtn1.setChecked(true);
        radioGroup.setOnCheckedChangeListener(this);
        initFragment();
    }

    @Override
    public void setUpData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initFragment() {
        addFragment(R.id.fragment_container, new FragmentHome(), MENU_HOME);
        addFragment(R.id.fragment_container, new FragmentFinacial(), MENU_FINACIAL);
        addFragment(R.id.fragment_container, new FragmentCapital(), MENU_MONEY);
        addFragment(R.id.fragment_container, new FragmentMore(), MENU_MORE);
        commit();
        setCurrentFramentByTag(MENU_HOME);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.menu_btn_1:
                setCurrentFramentByTag(MENU_HOME);
                break;
            case R.id.menu_btn_2:
                setCurrentFramentByTag(MENU_FINACIAL);
                break;
            case R.id.menu_btn_3:
                setCurrentFramentByTag(MENU_MONEY);
                break;
            case R.id.menu_btn_4:
                setCurrentFramentByTag(MENU_MORE);
                break;
        }
    }

    public void toFinancial() {
        menuBtn2.setChecked(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            doToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 1500); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    private void autoLogin() {
        LoginAccountEntity login = ShareConfig.getUserAccount(this);
        if (login != null && !Tools.isEmpty(login.getMobile()) && !Tools.isEmpty(login.getPasswd())) {
            httpRequest(Params.getLoginParams(this, login.getMobile(), login.getPasswd()), Methods.METHOD_LOGIN, Environments.PV1, LoginResultEntity.class, this);
        }
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        if (!result.isSucess()) {
            ShareConfig.saveUserAccount(this, null);
        }
    }

    @Override
    public void onRequestFailed(String msg) {

    }
}