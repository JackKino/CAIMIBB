package com.cmbb.app.entity.financial;

import com.cmbb.app.entity.base.BaseEntity;

import java.util.List;

/**
 * Created by Storm on 2015/12/6.
 * DES:
 */
public class PayProductBuyEntity extends BaseEntity {
    private List<PayProductBuyCouponEntity> coupon;
    private PayProductBuyPayment payment;
    private PayProductBuyProduct product;
    private PayProductBuyBank bank_card;

    public PayProductBuyBank getBank_card() {
        return bank_card;
    }

    public void setBank_card(PayProductBuyBank bank_card) {
        this.bank_card = bank_card;
    }

    public List<PayProductBuyCouponEntity> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<PayProductBuyCouponEntity> coupon) {
        this.coupon = coupon;
    }

    public PayProductBuyPayment getPayment() {
        return payment;
    }

    public void setPayment(PayProductBuyPayment payment) {
        this.payment = payment;
    }

    public PayProductBuyProduct getProduct() {
        return product;
    }

    public void setProduct(PayProductBuyProduct product) {
        this.product = product;
    }
}
