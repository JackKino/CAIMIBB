package com.cmbb.app.ui.financial;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.MD5;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.action.mgr.ImageManager;
import com.cmbb.app.adapter.myticket.CouponAdapter;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.financial.FinancialItemEntity;
import com.cmbb.app.entity.financial.PayProductBuyBank;
import com.cmbb.app.entity.financial.PayProductBuyCouponEntity;
import com.cmbb.app.entity.financial.PayProductBuyEntity;
import com.cmbb.app.entity.financial.PayProductBuyPayment;
import com.cmbb.app.entity.financial.PayProductBuyProduct;
import com.cmbb.app.entity.login.LoginAccountEntity;
import com.cmbb.app.entity.mine.SafetyAccount;
import com.cmbb.app.entity.mine.SafetyEntity;
import com.cmbb.app.iface.OnEditFinishListener;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.certification.ActivityCertificationStep1;
import com.cmbb.app.ui.more.ActivityAeestsHtml;
import com.cmbb.app.ui.more.ActivityMoreWebView;
import com.cmbb.app.views.widget.PasswordEditLayout;

import java.util.List;

/**
 * Created by Storm on 2015/11/29.
 * DES:立即支付
 */
public class FinacialPay extends BaseActivity implements RequestCallback, View.OnClickListener {
    private TextView projectName;
    private TextView bankName;
    private FinancialItemEntity item;
    private String money;
    private TextView tv_money;
    private LoginAccountEntity accountEntity;
    private TextView tv_balance, tv_coupon, tv_pay_left, tv_end_time, tv_income;
    private PayProductBuyEntity data;
    private String income;
    private Dialog dialog;
    private CouponAdapter adapter;
    private long real_payment = 0;
    private ImageView bankLogo;
    private SafetyEntity safetyEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finacial_pay);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.finacial_pay, true);
        safetyEntity = (SafetyEntity) getIntent().getExtras().get("safety");
        item = (FinancialItemEntity) getIntent().getExtras().get("item");
        money = getIntent().getExtras().getString("value");
        income = getIntent().getExtras().getString("income");
        projectName = (TextView) findViewById(R.id.tv_project_name);
        bankName = (TextView) findViewById(R.id.tv_bank_name);
        tv_money = (TextView) findViewById(R.id.pay_money);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_coupon = (TextView) findViewById(R.id.tv_coupon);
        tv_pay_left = (TextView) findViewById(R.id.pay_money_left);
        accountEntity = ShareConfig.getUserAccount(this);
        tv_end_time = (TextView) findViewById(R.id.end_time);
        tv_income = (TextView) findViewById(R.id.tv_income);
        bankLogo = (ImageView) findViewById(R.id.bank_logo);
        findViewById(R.id.coupon_view).setOnClickListener(this);
        findViewById(R.id.commit).setOnClickListener(this);
    }

    @Override
    public void setUpData() {
        bankName.setText(getString(R.string.bank_bind_, safetyEntity.getAccount().getBank_no()));
        bankLogo.setImageResource(R.mipmap.bank_logo);
        getData();
    }

    private void showMessage() {
        if (data != null) {
            PayProductBuyProduct product = data.getProduct();
            PayProductBuyPayment payment = data.getPayment();
            if (product != null) {
                projectName.setText(getString(R.string.finacial_pay_p_name, product.getP_name()));
                tv_money.setText(getString(R.string.my_income, Tools.formatMoney(Long.valueOf(money))));
                tv_end_time.setText(product.getP_expire_time());
                tv_income.setText(Html.fromHtml("<html>" + getString(R.string.my_income, "<font color='#F97373'>" + income /*Tools.formatMoney(Double.valueOf(product.getP_expect_return()))*/ + "</font>") + "</html>"));
            }

            setCouponCount(payment);

            long yet = 0;
            if (payment != null) {
                yet = payment.getYe();
            }

            setPayleft(yet, 0);
        }
    }

    private void setPayleft(long yet, long coupon) {
        long payment = Long.valueOf(money);
        long pay_left = payment - coupon;
        long account_pay = pay_left - yet;
        real_payment = pay_left;

        if (account_pay >= 0) {
            //账户余额支付
            tv_balance.setText(Html.fromHtml("<html>" + getString(R.string.my_income, "<font color='#F97373'>" + Tools.formatMoney(yet) + "</font>") + "</html>"));

            //银行卡支付
            tv_pay_left.setText(Html.fromHtml("<html>" + getString(R.string.my_income, "<font color='#F97373'>" + Tools.formatMoney(account_pay) + "</font>") + "</html>"));
        } else {
            //账户余额支付
            tv_balance.setText(Html.fromHtml("<html>" + getString(R.string.my_income, "<font color='#F97373'>" + Tools.formatMoney(pay_left) + "</font>") + "</html>"));

            //银行卡支付
            tv_pay_left.setText(Html.fromHtml("<html>" + getString(R.string.my_income, "<font color='#F97373'>" + 0 + "</font>") + "</html>"));
        }
    }

    private void setCouponCount(PayProductBuyPayment payment) {
        String count = "0";
        if (payment != null) {
            count = payment.getCoupon_count();
        }
        tv_coupon.setText(Html.fromHtml("<html>" + getString(R.string.my_ticket_count, "<font color='#F97373'>" + count + "</font>") + "</html>"));
    }

    private void getData() {
        showLoadingDialog(getString(R.string.waller_loading), false);
        httpRequest(Params.productBuyParams(this, item.getP_id(), money), Methods.MEHHOD_PAY_STEP1, Environments.PV1, PayProductBuyEntity.class, this);
    }

    @Override
    public void onRequestFailed(String msg) {
        closeLoadingDialog();
        doToast(msg);
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        closeLoadingDialog();
        if (result.isSucess()) {
            data = (PayProductBuyEntity) result;
            showMessage();
        } else {
            doToast(result.getMsg());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                showEditPwdDialog();
                break;
            case R.id.coupon_view:
                if (data != null) {
                    showCoupon(data.getCoupon());
                }
                break;
            case R.id.btn_ok:
                showUsedCoupon();
                break;
        }
    }

    /**
     * 密码输入框
     */
    private PasswordEditLayout editLayout;

    private void showEditPwdDialog() {
        dialog = new Dialog(this, R.style.Loading_Dialog_WithBackGround_Style);
        dialog.setContentView(R.layout.pwd_edit_dialog);
        editLayout = (PasswordEditLayout) dialog.findViewById(R.id.edit_view);
        editLayout.setOnPasswordEidtOverListener(new OnEditFinishListener() {
            @Override
            public void onPasswordEditOver(String value) {
                Tools.hideKeyBoard(FinacialPay.this, editLayout.getEditText());
                startpay(value);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Tools.hideKeyBoard(FinacialPay.this, editLayout.getEditText());
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Tools.showKeyBoard(FinacialPay.this, editLayout.getEditText());
            }
        }, 100);
    }

    /**
     * 开始支付
     */
    private void startpay(final String pwd) {
        showLoadingDialog(getString(R.string.pay_business_paying), false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String coupon = null;
                if (currentCoupon != null) {
                    coupon = currentCoupon.getCoupon_id();
                }
                httpRequest(Params.getPayParams(FinacialPay.this, item.getP_id(), MD5.MD5(pwd), money, String.valueOf(real_payment), coupon), Methods.METHOD_BUY_COMMIT, Environments.PV1, BaseEntity.class, new RequestCallback() {
                    @Override
                    public void onRequestSucess(BaseEntity result, String jsonData) {
                        if (result.isSucess()) {
                            dialog.dismiss();
                            showLoadingSucessDialog(getString(R.string.pay_business_pay_sucessed), false, new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    finish();
                                }
                            });
                        } else {
                            showLoadingFailedDialog(result.getMsg(), false, null);
//                            doToast(result.getMsg());
                        }
                    }

                    @Override
                    public void onRequestFailed(String msg) {
//                closeLoadingDialog();
                        doToast(msg);
                    }
                });
            }
        }, 1000);
    }

    private int couponPosition = -1;
    private PayProductBuyCouponEntity currentCoupon = null;

    private void showUsedCoupon() {
        couponPosition = adapter.getCheckedPosition();
        dialog.dismiss();
        if (couponPosition != -1) {
            currentCoupon = adapter.getItem(couponPosition);
            if (item != null) {
                if ("0".equals(currentCoupon.getType_id())) {
                    tv_coupon.setText(currentCoupon.getMoney() + getString(R.string.recharge_acount_yuan) + getString(R.string.my_ticket_type_0));
                    setPayleft(data.getPayment().getYe(), currentCoupon.getMoney());
                } else if ("1".equals(currentCoupon.getType_id())) {
                    tv_coupon.setText(currentCoupon.getRate() + "%" + getString(R.string.my_ticket_type_1));
                    setPayleft(data.getPayment().getYe(), 0);
                }
            }
        } else {
            currentCoupon = null;
            setCouponCount(data.getPayment());
            setPayleft(data.getPayment().getYe(), 0);
        }
    }

    /**
     * 优惠券列表
     */

    private void showCoupon(List<PayProductBuyCouponEntity> dataList) {
        if (dataList != null && dataList.size() > 0) {
            dialog = new Dialog(this, R.style.Loading_Dialog_WithBackGround_Style);
            dialog.setContentView(R.layout.coupon_dialog_layout);
            dialog.findViewById(R.id.coupon_des).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toActivity(Environments.ABOUT_COUPONS, getString(R.string.wallet_ticket_user_detail));
                }
            });
            ListView list = (ListView) dialog.findViewById(R.id.list_view);
            dialog.findViewById(R.id.btn_ok).setOnClickListener(this);
            list.setAdapter(adapter = new CouponAdapter(this, dataList, couponPosition));
            dialog.show();
        } else {
            doToast(R.string.coupon_null);
        }
    }

    /**
     * 优惠券使用说明
     */
    private void toActivity(String url, String title) {
        Intent webIntent = new Intent(this, ActivityMoreWebView.class);
        webIntent.putExtra("url", url);
        webIntent.putExtra("title", title);
        this.startActivity(webIntent);
    }
}
