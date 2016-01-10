package com.cmbb.app.iface;

import com.cmbb.app.entity.login.LoginAccountEntity;

/**
 * Created by Storm on 2015/11/25.
 */
public interface UserLoginSucessListener {
    void onUserAccountChanged(LoginAccountEntity account);
}
