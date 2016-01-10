package com.cmbb.app.entity.financial;

import com.cmbb.app.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * Created by Storm on 2015/11/26.
 * DES:
 */
public class FinancialItemEntity extends BaseEntity implements Serializable {
    private String p_date;
    private String p_id;
    private String p_name;
    private double p_rate;
    private int p_sign;
    private String p_tags;
    private int p_buyer_count;
    private int p_min_price;
    private int p_percent;
    private long p_remain_price;
    private String top;
    private String p_expire_time;

    private ProjectDetail_Info p_info;

    private ProjectDetail_Safety p_safety;

    private ProjectTop_Info p_top;

    public ProjectDetail_Safety getP_safety() {
        return p_safety;
    }

    public void setP_safety(ProjectDetail_Safety p_safety) {
        this.p_safety = p_safety;
    }

    public String getP_date() {
        return p_date;
    }

    public String getP_expire_time() {
        return p_expire_time;
    }

    public void setP_expire_time(String p_expire_time) {
        this.p_expire_time = p_expire_time;
    }

    public void setP_date(String p_date) {
        this.p_date = p_date;
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

    public double getP_rate() {
        return p_rate;
    }

    public void setP_rate(double p_rate) {
        this.p_rate = p_rate;
    }

    public int getP_sign() {
        return p_sign;
    }

    public void setP_sign(int p_sign) {
        this.p_sign = p_sign;
    }

    public String getP_tags() {
        return p_tags;
    }

    public void setP_tags(String p_tags) {
        this.p_tags = p_tags;
    }

    public int getP_buyer_count() {
        return p_buyer_count;
    }

    public void setP_buyer_count(int p_buyer_count) {
        this.p_buyer_count = p_buyer_count;
    }

    public int getP_min_price() {
        return p_min_price;
    }

    public void setP_min_price(int p_min_price) {
        this.p_min_price = p_min_price;
    }

    public int getP_percent() {
        return p_percent;
    }

    public void setP_percent(int p_percent) {
        this.p_percent = p_percent;
    }

    public long getP_remain_price() {
        return p_remain_price;
    }

    public void setP_remain_price(long p_remain_price) {
        this.p_remain_price = p_remain_price;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public ProjectDetail_Info getP_info() {
        return p_info;
    }

    public void setP_info(ProjectDetail_Info p_info) {
        this.p_info = p_info;
    }

    public ProjectTop_Info getP_top() {
        return p_top;
    }

    public void setP_top(ProjectTop_Info p_top) {
        this.p_top = p_top;
    }
}
