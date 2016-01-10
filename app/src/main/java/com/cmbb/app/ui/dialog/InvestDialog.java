package com.cmbb.app.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.entity.financial.FinancialItemEntity;
import com.cmbb.app.entity.mine.SafetyEntity;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.financial.FinacialPay;
import com.cmbb.app.ui.login.ActivityLogin;
import com.cmbb.app.user.UserManager;

import java.math.BigInteger;

/**
 * Created by Storm on 2015/11/29.
 * DES:投资金额输入对话框
 */
public class InvestDialog extends BaseActivity {
    private TextView tv_income;
    private TextView tv_multiple;
    private View background_view;
    private View contentView;
    private boolean animationOver = true;
    private EditText et_edit;
    private FinancialItemEntity item;
    private View etView;
    private String rateMul = "0";
    private String inCome = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invest_dialog);
    }

    @Override
    public void setUpViews() {
        item = (FinancialItemEntity) getIntent().getExtras().get("item");
        rateMul = Tools.formatPercent(item.getP_rate() / Environments.RATE_IN_BANK);
        tv_income = (TextView) findViewById(R.id.tv_income);
        tv_multiple = (TextView) findViewById(R.id.tv_multiple);
        background_view = findViewById(R.id.background_view);
        contentView = findViewById(R.id.content_view);
        et_edit = (EditText) findViewById(R.id.et_edit);
        etView = findViewById(R.id.et_view);
        background_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && animationOver) {
                    showOutAnimation();
                }
                return false;
            }
        });
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        showInAnimation();

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
        et_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dealData(s.toString());
            }
        });
    }

    private void dealData(String value) {
        if (Tools.isEmpty(value)) {
            etView.setBackgroundResource(R.drawable.shape_edit_red_rect);
            setValue("0", rateMul);
        } else {
            if (new BigInteger(value).compareTo(new BigInteger(String.valueOf(item.getP_min_price()))) < 0) {
                etView.setBackgroundResource(R.drawable.shape_edit_red_rect);
            } else {
                etView.setBackgroundResource(R.drawable.shape_edit_rect);
            }
            String result = Tools.getIncome(value, item.getP_rate() / 100, item.getP_date());
            setValue(result, rateMul);
        }
    }

    private void commit() {
        String value = et_edit.getText().toString().trim();
        if (!Tools.isEmpty(value)) {
            //系统自带的类
            BigInteger p = new BigInteger(value);
            if (!"0".equals(value) && p.remainder(new BigInteger("100")).intValue() == 0) {
                if (p.compareTo(new BigInteger(String.valueOf(item.getP_min_price()))) < 0) {
                    doToast(getString(R.string.common_price_money_error, item.getP_min_price()));
                } else if (p.compareTo(new BigInteger(String.valueOf(item.getP_remain_price()))) > 0) {
                    doToast(getString(R.string.common_price_money_error1, item.getP_min_price()));
                } else if (!UserManager.isLogin(this)) {
                    doToast(R.string.common_unlogin);
                    startActivity(ActivityLogin.class);
                } else {
                    Tools.hideKeyBoard(InvestDialog.this, et_edit);
                    Intent intent = new Intent(InvestDialog.this, FinacialPay.class);
                    intent.putExtra("value", value);
                    intent.putExtra("item", item);
                    intent.putExtra("income", inCome);
                    SafetyEntity safetyEntity = (SafetyEntity)getIntent().getExtras().get("safety");
                    intent.putExtra("safety",safetyEntity);
                    startActivity(intent);
                    finish();
                }
            } else {
                doToast(R.string.common_price_format_error);
            }
        } else {
            doToast(R.string.common_price_format0);
        }
    }

    private void showInAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        background_view.startAnimation(animation);

        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.amin_in);
        inAnimation.setInterpolator(new OvershootInterpolator());
        contentView.startAnimation(inAnimation);
        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Tools.showKeyBoard(InvestDialog.this, et_edit);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showOutAnimation() {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        background_view.startAnimation(animation);
        animationOver = false;

        Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.amin_out);
        inAnimation.setInterpolator(new AnticipateInterpolator());
        contentView.startAnimation(inAnimation);
        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Tools.hideKeyBoard(InvestDialog.this, et_edit);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                background_view.setVisibility(View.GONE);
                contentView.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (animationOver) {
                showOutAnimation();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setUpData() {
        if (item != null) {
            String minPrice = String.valueOf(item.getP_min_price());
            et_edit.setText(minPrice);
            et_edit.setSelection(minPrice.length());
        } else {
            setValue("0", rateMul);
        }
    }

    private void setValue(String income, String multiple) {
        this.inCome = income;
        tv_income.setText(Html.fromHtml("<html>" + getString(R.string.invest_income, "<font color='#F97373'>" + income + "</font>") + "</html>"));
        tv_multiple.setText(Html.fromHtml("<html>" + getString(R.string.invest_multiple, "<font color='#F97373'>" + multiple + "</font>") + "</html>"));
    }
}
