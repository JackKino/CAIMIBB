package com.cmbb.app.entity.login;

import com.cmbb.app.entity.base.BaseEntity;

/**
 * Created by admin on 2015/12/23.
 */
public class UserAssetEntity extends BaseEntity {
    private String balance;
    private String coupon;
    private String income;
    private String name;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
