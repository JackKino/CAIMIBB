package com.cmbb.app.entity.login;

import com.cmbb.app.entity.base.BaseEntity;

/**
 * Created by Storm on 2015/11/25.
 */
public class LoginResultEntity extends BaseEntity {
    private LoginAccountEntity account;

    public LoginAccountEntity getAccount() {
        return account;
    }

    public void setAccount(LoginAccountEntity account) {
        this.account = account;
    }
}
