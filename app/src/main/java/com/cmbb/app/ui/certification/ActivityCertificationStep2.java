package com.cmbb.app.ui.certification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Logger;
import com.cmbb.app.action.common.MD5;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.entity.capital.CertificationEntity;
import com.cmbb.app.iface.OnEditFinishListener;
import com.cmbb.app.iface.TextChangeAfterCallback;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.EditLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Storm on 2015/12/5.
 * DES:实名认证第二步
 */
public class ActivityCertificationStep2 extends BaseActivity implements View.OnClickListener, TextChangeAfterCallback {
    private String pwd;
    private EditLayout tvName, tvId, tvCard, tvMobile, tvCode;
    private Button btnCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_stop2);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.certification_step1, true);
        pwd = getIntent().getExtras().getString("pwd");
        tvName = (EditLayout) findViewById(R.id.tv_name);
        tvName.setHide(R.string.certification_name_hide);
        tvId = (EditLayout) findViewById(R.id.tv_id);
        tvId.setHide(R.string.certification_id_hide);
        tvId.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        tvId.setDegit(getString(R.string.common_degit).replace("_", ""));
        tvId.setMaxLengh(18);
        tvCard = (EditLayout) findViewById(R.id.tv_card);
        tvCard.setHide(R.string.certification_card_hide);
        tvCard.setDegit("1234567890 ");
        tvCard.setInputType(InputType.TYPE_CLASS_NUMBER);
        tvCard.setMaxLengh(19);
        tvMobile = (EditLayout) findViewById(R.id.tv_mobile);
        tvMobile.setHide(R.string.certification_mobile_hide);
        tvMobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        tvMobile.setMaxLengh(11);

//        tvCode = (EditLayout) findViewById(R.id.tv_code);
//        tvCode.setHide(R.string.certification_code_get_hide);
//        tvCode.setInputType(InputType.TYPE_CLASS_NUMBER);
//        tvCode.setMaxLengh(6);
//        btnCode = (Button) findViewById(R.id.send_code);
//        btnCode.setOnClickListener(this);
        findViewById(R.id.commit).setOnClickListener(this);

        tvCard.setOnTextChangeAfter(this);
    }

    @Override
    public void textChangeAfter(Editable arg0) {
//        String content = arg0.toString().trim().replace(" ", "");
//        int len = content.length();
//        if (len % 4 == 0) {
//            StringBuffer buffer = new StringBuffer();
//            for (int i = 0; i < len / 4; i++) {
//                String c_char = content.substring(0, 4);
//                content = content.substring(4);
//                buffer.append(c_char).append(" ");
//            }
//            if (content.length() > 0) {
//                buffer.append(content);
//            }
//            Logger.i("银行卡输入:",buffer.toString());
//            tvCard.setText(buffer.toString());
//        }
    }

    private void checkData() {
        String name = tvName.getText().trim();
        String id = tvId.getText().trim();
        String card = tvCard.getText().trim();
        String mobile = tvMobile.getText().trim();
//        String code = tvCode.getText();
        if (Tools.isEmpty(name)) {
            doToast(R.string.certification_name_hide);
            return;
        }

        if (Tools.isEmpty(id)) {
            doToast(R.string.certification_id_hide);
            return;
        }

        if (Tools.isEmpty(card)) {
            doToast(R.string.certification_card_hide);
            return;
        }

        if (Tools.isEmpty(mobile)) {
            doToast(R.string.certification_mobile_hide);
            return;
        }

        if (!Tools.checkBankCard(card)) {
            doToast(R.string.certification_card_error);
            return;
        }

//        if (Tools.isEmpty(code)) {
//            doToast(R.string.certification_code_get_hide);
//            return;
//        }

        if (!Tools.checkMoile(mobile)) {
            doToast(R.string.login_mobile_error);
            return;
        }

        showLoadingDialog(getString(R.string.certification_checking), false);
        httpRequest(Params.CertificationParams(this, name, id, card, mobile, MD5.MD5(pwd)), Methods.METHOD_CERFIFICATION, Environments.PV1, CertificationEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                closeLoadingDialog();
                if (result.isSucess()) {
                    toEditCode((CertificationEntity) result);
                } else {
                    doToast(result.getMsg());
                }
            }

            @Override
            public void onRequestFailed(String msg) {
                doToast(msg);
                closeLoadingDialog();
            }
        });
    }

    private void toEditCode(CertificationEntity entity) {
        Intent intent = new Intent(this, ActivityCertificationStep3.class);
        String related = entity.getRelated().toString();
        String name = tvName.getText().trim();
        String id = tvId.getText().trim();
        String card = tvCard.getText().trim();
        String mobile = tvMobile.getText().trim();
        intent.putExtra("related", related);
        intent.putExtra("name", name);
        intent.putExtra("pid", id);
        intent.putExtra("card_no", card);
        intent.putExtra("mobile", mobile);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code:
                sendCode();
                break;
            case R.id.commit:
                checkData();
                break;
        }
    }

    private void sendCode() {
        String mobile = tvMobile.getText().trim();
        if (Tools.isEmpty(mobile)) {
            doToast(R.string.certification_mobile_hide);
            return;
        }

        if (!Tools.checkMoile(mobile)) {
            doToast(R.string.login_mobile_error);
            return;
        }
        showLoadingDialog(getString(R.string.common_loading_wait), false);
        httpRequest(Params.getCodeParams(this, mobile), Methods.METHOD_GET_CODE, Environments.PV1, BaseEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                closeLoadingDialog();
                if (result.isSucess()) {
                    startTimer();
                }
                doToast(result.getMsg());
            }

            @Override
            public void onRequestFailed(String msg) {
                closeLoadingDialog();
                doToast(msg);
            }
        });
    }

    private final int TIME_DELAY = 45;
    private int currentTime = TIME_DELAY;
    private Timer timer;

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (currentTime <= 0) {
                        currentTime = TIME_DELAY;
                        updateHandler.sendEmptyMessage(-1);
                    } else {
                        updateHandler.sendEmptyMessage(currentTime);
                        currentTime--;
                    }
                }
            }, 0, 1000);
        }
    }

    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == -1) {
                btnCode.setEnabled(true);
                btnCode.setText(R.string.certification_code_get);
                stopTimer();
            } else {
                btnCode.setEnabled(false);
                btnCode.setText(msg.what + "s");
            }
        }
    };

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}

