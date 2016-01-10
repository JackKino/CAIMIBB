package com.cmbb.app.entity.recharge;

import com.cmbb.app.entity.base.BaseEntity;

import java.util.List;

/**
 * Created by Storm on 2015/12/1.
 * DES:
 */
public class RechargeHistoryEntity extends BaseEntity {
    private List<RechargeHistoryItemEntity> topup;

    public List<RechargeHistoryItemEntity> getTopup() {
        return topup;
    }

    public void setTopup(List<RechargeHistoryItemEntity> topup) {
        this.topup = topup;
    }
}
