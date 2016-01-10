package com.cmbb.app.entity.capital;

import com.cmbb.app.entity.base.BaseEntity;

import java.util.List;

/**
 * Created by Storm on 2015/12/1.
 * DES:
 */
public class CouponEntity extends BaseEntity {
    private List<CouponItemEntity> coupon;

    public List<CouponItemEntity> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<CouponItemEntity> coupon) {
        this.coupon = coupon;
    }
}
