package com.cmbb.app.entity.capital;

import com.cmbb.app.entity.base.BaseEntity;

import java.util.List;

/**
 * Created by Storm on 2015/12/2.
 * DES:
 */
public class CapitalFlowEntity extends BaseEntity {
    private int current_page;
    private int page_size;
    private List<CapitalFlowItemEntity> list;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public List<CapitalFlowItemEntity> getList() {
        return list;
    }

    public void setList(List<CapitalFlowItemEntity> list) {
        this.list = list;
    }
}
