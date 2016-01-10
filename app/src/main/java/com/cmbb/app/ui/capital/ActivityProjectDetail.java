package com.cmbb.app.ui.capital;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.financial.FinancialItemEntity;
import com.cmbb.app.entity.financial.ProjectDetail_Info;
import com.cmbb.app.entity.financial.ProjectDetail_Safety;
import com.cmbb.app.entity.financial.ProjectTop_Info;
import com.cmbb.app.entity.mine.SafetyAccount;
import com.cmbb.app.entity.mine.SafetyEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.certification.ActivityCertificationStep1;
import com.cmbb.app.ui.dialog.InvestDialog;
import com.cmbb.app.ui.financial.FinacialHistory;
import com.cmbb.app.ui.more.ActivityMoreWebView;
import com.cmbb.app.ui.recharge.ActivityRecharge;
import com.cmbb.app.user.UserManager;

/**
 * Created by Storm on 2015/11/24.
 * 项目详情
 */
public class ActivityProjectDetail extends BaseActivity implements RequestCallback, View.OnClickListener {
    private ImageView newIcon;
    private TextView topMsg;
    private TextView pRate;
    private ProgressBar pProgressBar;
    private TextView pProgressValue;
    private TextView days;
    private TextView endTime;
    private TextView buyerCount;
    private FinancialItemEntity item;
    private TextView pRemain;
    private TextView pInfo, pSafety;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
    }

    @Override
    public void setUpViews() {
        Bundle bundle = getIntent().getExtras();
        item = (FinancialItemEntity) bundle.get("item");
        initTitle(item.getP_name(), true);
        newIcon = (ImageView) findViewById(R.id.news_icon);
        newIcon.setBackgroundResource(R.anim.speaker_animaion);
        AnimationDrawable animationDrawable = (AnimationDrawable) newIcon.getBackground();
        animationDrawable.start();//开始
        topMsg = (TextView) findViewById(R.id.top_msg_value);
        pRate = (TextView) findViewById(R.id.p_rate);
        pProgressBar = (ProgressBar) findViewById(R.id.p_progress);
        pProgressValue = (TextView) findViewById(R.id.p_progress_value);
        days = (TextView) findViewById(R.id.days);
        endTime = (TextView) findViewById(R.id.end_time);
        buyerCount = (TextView) findViewById(R.id.buyer_count);
        pRemain = (TextView) findViewById(R.id.p_remain);
        pInfo = (TextView) findViewById(R.id.p_info);
        pSafety = (TextView) findViewById(R.id.p_safety);
        findViewById(R.id.project_info_view).setOnClickListener(this);
        findViewById(R.id.project_safety_view).setOnClickListener(this);
        findViewById(R.id.buyer_count).setOnClickListener(this);
        findViewById(R.id.invest_now).setOnClickListener(this);
    }

    @Override
    public void setUpData() {
        showMessage();
    }

    /**
     * 显示信息
     */
    private void showMessage() {
        if (item != null) {
            pRemain.setText(getString(R.string.detail_remain, item.getP_remain_price()));
            buyerCount.setText(getString(R.string.detail_buyer_count, item.getP_buyer_count()));
            ProjectTop_Info pTop = item.getP_top();
            if (pTop != null && !Tools.isEmpty(pTop.getInfo())) {
                topMsg.setText(pTop.getInfo());
                topMsg.setOnClickListener(this);
            } else {
                findViewById(R.id.top_msg).setVisibility(View.INVISIBLE);
            }
            initTitle(item.getP_name(), true);
            pRate.setText(String.valueOf(item.getP_rate()));
            if (item.getP_percent() >= 0 && item.getP_percent() < 100) {
                pProgressBar.setProgress(item.getP_percent());
                pProgressValue.setText(item.getP_percent() + "%");
                endTime.setText(getString(R.string.detail_end_time, item.getP_expire_time()));
                findViewById(R.id.invest_now).setEnabled(true);
            } else {
                pProgressBar.setProgress(100);
                pProgressValue.setText(100 + "%");
                if (item.getP_percent() == 100) {
                    endTime.setText(R.string.detail_unfinish);
                } else if (item.getP_percent() == 200) {
                    endTime.setText(R.string.detail_finished);
                }
                findViewById(R.id.invest_now).setEnabled(false);
            }
            days.setText(getString(R.string.detail_days, item.getP_min_price(), item.getP_date(), 1));
            buyerCount.setText(getString(R.string.detail_buyer_count, item.getP_buyer_count()));

            ProjectDetail_Info info = item.getP_info();
            if (info != null) {
                pInfo.setText(info.getInfo());
            }

            ProjectDetail_Safety safety = item.getP_safety();
            if (safety != null) {
                pSafety.setText(safety.getInfo());
            }
        }
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
    }

    @Override
    public void onRequestFailed(String msg) {
        closeLoadingDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.project_info_view:
                toWebView(1);
                break;
            case R.id.project_safety_view:
                toWebView(2);
                break;
            case R.id.buyer_count: {
                if (item != null) {
                    Intent intent = new Intent(this, FinacialHistory.class);
                    intent.putExtra("p_id", item.getP_id());
                    startActivity(intent);
                }
                break;
            }
            case R.id.invest_now: {
                if (UserManager.isLogin(this)) {
                    if (item != null) {
                        getNewData();
                    }
                } else {
                    toLogin();
                }
                break;
            }
            case R.id.top_msg_value:
                toWebView(3);
                break;
        }
    }

    private void toWebView(int type) {
        String title = "", url = "";
        if (type == 1) {
            ProjectDetail_Info pInfo = item.getP_info();
            title = getString(R.string.detail_project_p);
            if (pInfo != null) {
                url = pInfo.getUrl();
            }
        } else if (type == 2) {
            ProjectDetail_Safety pInfo = item.getP_safety();
            title = getString(R.string.detail_project_safer);
            if (pInfo != null) {
                url = pInfo.getUrl();
            }
        } else if (type == 3) {
            ProjectTop_Info top = item.getP_top();
            title = top.getInfo();
            url = top.getUrl();
        }
        Intent intent = new Intent(this, ActivityMoreWebView.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }


    private void getNewData() {
        showLoadingDialog(getString(R.string.user_info_get), false);
        httpRequest(Params.getSafetyCenter(this), Methods.METHOD_SAFETY_CENTER, Environments.PV1, SafetyEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                closeLoadingDialog();
                if (result.isSucess()) {
                    SafetyEntity safety = (SafetyEntity) result;
                    if (safety != null && safety.getAccount() != null) {
                        SafetyAccount account = safety.getAccount();
                        if (account.hasBindBank()) {
                            Intent intent = new Intent(ActivityProjectDetail.this, InvestDialog.class);
                            intent.putExtra("item", item);
                            intent.putExtra("safety",safety);
                            startActivity(intent);
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
}
