package com.cmbb.app.entity.login;

/**
 * Created by Storm on 2015/11/25.
 */
public class LoginAccountEntity {
    private String mobile;
    private String user_id;
    private String passwd;

//    coupon:3,             //优惠券数量
//    income:2000,          //我的收益
//    balance:1000,         //余额
//    name:"张小二"         //姓名,空则未绑定

    private String coupon;
    private String income;
    private String balance;
    private String name;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
