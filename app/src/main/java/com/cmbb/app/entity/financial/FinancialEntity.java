package com.cmbb.app.entity.financial;

import com.cmbb.app.entity.base.BaseEntity;

import java.util.List;

/**
 * Created by Storm on 2015/11/26.
 * DES:
 */
public class FinancialEntity extends BaseEntity {
    private int page_size;
    private int current_page;
    private List<FinancialItemEntity> list;

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<FinancialItemEntity> getList() {
        return list;
    }

    public void setList(List<FinancialItemEntity> list) {
        this.list = list;
    }
}
