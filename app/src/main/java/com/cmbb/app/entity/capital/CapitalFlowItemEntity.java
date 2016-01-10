package com.cmbb.app.entity.capital;

/**
 * Created by Storm on 2015/12/2.
 * DES:
 */
public class CapitalFlowItemEntity {
//    type_id:1,                    //资金类型
//    money:1000,                   //操作金额元
//    time:"2015/12/31 15:29",      //操作时间
//    balance:1000,                 //余额元
//    note:"来自易宝第三方支付",    //备注

    private int type_id;
    private String money;
    private String time;
    private String balance;
    private String note;

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
