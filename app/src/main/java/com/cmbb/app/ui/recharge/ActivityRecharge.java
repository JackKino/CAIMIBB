package com.cmbb.app.ui.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.action.mgr.ImageManager;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.entity.capital.ActivityRechargeStep2;
import com.cmbb.app.entity.capital.RechargeEntity;
import com.cmbb.app.entity.mine.SafetyAccount;
import com.cmbb.app.entity.mine.SafetyEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.more.ActivityMoreWebView;
import com.cmbb.app.views.widget.EditLayout;

/**
 * Created by Storm on 2015/11/22.
 * 充值或提现界面
 */
public class ActivityRecharge extends BaseActivity implements View.OnClickListener, RequestCallback {
    private TextView accountLeft;
    private TextView bankName;
    private EditLayout rechargeEdit;
    private int type;
    private double balance;
    private Button commitBtn;
    private ImageView bankLogo;
    private SafetyEntity data;
    private String moneyStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
    }

    @Override
    public void setUpViews() {
        type = getIntent().getExtras().getInt("type");
        balance = getIntent().getExtras().getDouble("balance", 0);
        data = (SafetyEntity) getIntent().getExtras().get("data");
        TextView t = (TextView) findViewById(R.id.tv_recharge);
        rechargeEdit = (EditLayout) findViewById(R.id.recharge);
        commitBtn = (Button) findViewById(R.id.recharge_commit);
        commitBtn.setOnClickListener(this);

        if (type == 1) {
            rechargeEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            initTitleWithRightText(getString(R.string.my_money_recharge), getString(R.string.my_money_recharge_history), true);
            t.setText(R.string.recharge_recharge_);
            rechargeEdit.setHide(R.string.recharge_recharge_value);
            commitBtn.setText(R.string.recharge_recharge_commit);
        } else {
            rechargeEdit.setInputType(InputType.TYPE_CLASS_PHONE);
            rechargeEdit.setDegit("0123456789.");
            initTitleWithRightText(getString(R.string.wallet_take_out), getString(R.string.my_money_recharge_history), true);
            t.setText(R.string.recharge_cash);
            rechargeEdit.setHide(R.string.recharge_cash_value);
            setPricePoint(rechargeEdit.getEditText());
            commitBtn.setText(R.string.recharge_recharge_cash_commit);
        }

        accountLeft = (TextView) findViewById(R.id.tv_account_left);
        bankName = (TextView) findViewById(R.id.tv_bank_name);
        bankLogo = (ImageView) findViewById(R.id.bank_logo);


        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Tools.hideKeyBoard(ActivityRecharge.this, rechargeEdit.getEditText());
                }
                return false;
            }
        });
        findViewById(R.id.limit_detail).setOnClickListener(this);
    }

    @Override
    protected void onRightPressed(View view) {
        super.onRightPressed(view);
        Intent intent = new Intent(this, ActivityRechargeHistory.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    public void setUpData() {
        accountLeft.setText(Html.fromHtml(getAccountHtml(balance)));
        if (data != null) {
            SafetyAccount safetyAccount = data.getAccount();
            bankName.setText(safetyAccount.getBank_name());
            bankName.setText(getString(R.string.bank_bind_, safetyAccount.getBank_no()));
            //loadImage(bankLogo, safetyAccount.getBank_logo(), ImageManager.getImageDefaultOptions());
            bankLogo.setImageResource(R.mipmap.bank_logo);
        }
    }

    private String getAccountHtml(double money) {
        return "<html><font color='#333333'>" + getString(R.string.recharge_acount_left) + "  </font><font color='#EE5447'>" + Tools.formatMoney(money) + "</font><font color='#333333'>" + getString(R.string.recharge_acount_yuan) + "</font><html>";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.limit_detail:
//                toWebView(Environments.URL_ABOUT, getString(R.string.recharge_recharge_limit_detail));
                startActivity(ActivityRechargeLimit.class);
                break;
            case R.id.recharge_commit:
                commit();
                break;
        }
    }

    private void commit() {
        String money = rechargeEdit.getText();
        if (Tools.isEmpty(money)) {
            if (type == 1) {
                doToast(R.string.recharge_recharge_value);
            } else {
                doToast(R.string.recharge_cash_value);
            }
            return;
        }


        if (type == 2) {
            if (money.endsWith(".")) {
                money += "00";
            }
            double currentMoney = Double.valueOf(money);
            if (currentMoney > balance) {
                doToast(R.string.recharge_cash_value_error);
                return;
            }
        }

        double t_m = Double.valueOf(money);
        if (t_m <= 0) {
            if (type == 2) {
                doToast(R.string.recharge_cash_value_error2);
            } else {
                doToast(R.string.recharge_cash_value_error3);
            }
            return;
        }

        moneyStr = money;

        Tools.hideKeyBoard(this, rechargeEdit.getEditText());
        if (type == 1) {
            showLoadingDialog(getString(R.string.recharge_ing), false);
            httpRequest(Params.rechargeParams(this, money), Methods.METHOD_RECHARGE, Environments.PV1, RechargeEntity.class, this);
        } else {
            showLoadingDialog(getString(R.string.cash_ing), false);
            httpRequest(Params.rechargeParams(this, money), Methods.METHOD_WITHDRAW, Environments.PV1, BaseEntity.class, this);
        }
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        closeLoadingDialog();

        if (result.isSucess()) {
            if (type == 1) {
//                doToast(R.string.recharge_recharge_sucess);
                toRechargeStep2(result);
            } else {
                doToast(R.string.recharge_recharge_cash_sucess);
                finish();
            }
        } else {
            doToast(result.getMsg());
        }
    }

    private void toRechargeStep2(BaseEntity base) {
        RechargeEntity entity = (RechargeEntity) base;
        Intent intent = new Intent(this, ActivityRechargeStep2.class);
        intent.putExtra("money", moneyStr);
        intent.putExtra("related", entity.getRelated().toString());
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onRequestFailed(String msg) {
        closeLoadingDialog();
        doToast(msg);
    }


    public void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().contains(".") && s.toString().endsWith("..")) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 1);
                    editText.setText(s);
                    editText.setSelection(s.length());
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }
}
