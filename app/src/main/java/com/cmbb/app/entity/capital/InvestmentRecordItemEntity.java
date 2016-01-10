package com.cmbb.app.entity.capital;

/**
 * Created by Storm on 2015/12/8.
 * DES:
 */
public class InvestmentRecordItemEntity {
    private String p_id;
    private String p_name;
    private String p_pay_time;
    private String p_rate;
    private String p_expire_time = "";
    private String p_money = "0";

    public String getP_expire_time() {
        return p_expire_time;
    }

    public void setP_expire_time(String p_expire_time) {
        this.p_expire_time = p_expire_time;
    }

    public String getP_money() {
        return p_money;
    }

    public void setP_money(String p_money) {
        this.p_money = p_money;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_pay_time() {
        return p_pay_time;
    }

    public void setP_pay_time(String p_pay_time) {
        this.p_pay_time = p_pay_time;
    }

    public String getP_rate() {
        return p_rate;
    }

    public void setP_rate(String p_rate) {
        this.p_rate = p_rate;
    }
}
