package com.cmbb.app.entity.capital;

import com.cmbb.app.entity.base.BaseEntity;

import java.util.List;

/**
 * Created by Storm on 2015/12/8.
 * DES:
 */
public class InvestmentRecordEntity extends BaseEntity {
    private int money;
    private List<InvestmentRecordItemEntity> list;

    public List<InvestmentRecordItemEntity> getList() {
        return list;
    }

    public void setList(List<InvestmentRecordItemEntity> list) {
        this.list = list;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
