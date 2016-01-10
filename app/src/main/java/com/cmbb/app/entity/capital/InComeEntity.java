package com.cmbb.app.entity.capital;

import com.cmbb.app.entity.base.BaseEntity;

/**
 * Created by Storm on 2015/12/1.
 * DES:
 */
public class InComeEntity extends BaseEntity {
    private double asset;
    private double balance;
    private double withdraw;

    private double imcome_already;
    private double imcome_wait;

    public double getAsset() {
        return asset;
    }

    public void setAsset(double asset) {
        this.asset = asset;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(double withdraw) {
        this.withdraw = withdraw;
    }

    public double getImcome_already() {
        return imcome_already;
    }

    public void setImcome_already(double imcome_already) {
        this.imcome_already = imcome_already;
    }

    public double getImcome_wait() {
        return imcome_wait;
    }

    public void setImcome_wait(double imcome_wait) {
        this.imcome_wait = imcome_wait;
    }
}
