package com.cmbb.app.entity.mine;

import com.cmbb.app.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * Created by Storm on 2015/12/1.
 * DES:
 */
public class SafetyEntity extends BaseEntity implements Serializable{
    private SafetyAccount account;

    public SafetyAccount getAccount() {
        return account;
    }

    public void setAccount(SafetyAccount account) {
        this.account = account;
    }
}
