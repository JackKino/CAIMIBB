package com.cmbb.app.entity.financial;

/**
 * Created by Storm on 2015/12/3.
 * DES:
 */
public class FinacialHistoryItemEntity {
//    mobile:"136****1368",   //投资者
//    money: 4500,             //金额
//    buy_time:"2105/11/04 14:20"    //购买时间

    private String mobile;
    private String money;
    private String buy_time;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }
}
