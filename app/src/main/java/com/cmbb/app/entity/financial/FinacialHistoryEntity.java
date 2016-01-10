package com.cmbb.app.entity.financial;

import com.cmbb.app.entity.base.BaseEntity;

import java.util.List;

/**
 * Created by Storm on 2015/12/3.
 * DES:
 */
public class FinacialHistoryEntity extends BaseEntity {
    private String current_page;
    private String p_buyer_count;
    private String p_money;
    private String page_size;
    private List<FinacialHistoryItemEntity> investors;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getP_buyer_count() {
        return p_buyer_count;
    }

    public void setP_buyer_count(String p_buyer_count) {
        this.p_buyer_count = p_buyer_count;
    }

    public String getP_money() {
        return p_money;
    }

    public void setP_money(String p_money) {
        this.p_money = p_money;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public List<FinacialHistoryItemEntity> getInvestors() {
        return investors;
    }

    public void setInvestors(List<FinacialHistoryItemEntity> investors) {
        this.investors = investors;
    }
}
