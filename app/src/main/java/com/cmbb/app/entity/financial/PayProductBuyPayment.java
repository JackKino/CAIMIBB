package com.cmbb.app.entity.financial;

/**
 * Created by Storm on 2015/12/6.
 * DES:
 */
public class PayProductBuyPayment {
    private String coupon_count;
    private long money;
    private long ye;

    public String getCoupon_count() {
        return coupon_count;
    }

    public void setCoupon_count(String coupon_count) {
        this.coupon_count = coupon_count;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getYe() {
        return ye;
    }

    public void setYe(long ye) {
        this.ye = ye;
    }
}
