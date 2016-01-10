package com.cmbb.app.user;

import android.content.Context;

import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.entity.login.LoginAccountEntity;

/**
 * Created by Storm on 2015/11/25.
 */
public class UserManager {
    /**
     * 获取用户id
     *
     * @param context
     * @return
     */
    public static String getUserId(Context context) {
        LoginAccountEntity account = ShareConfig.getUserAccount(context);
        if (account != null && !Tools.isEmpty(account.getUser_id())) {
            return account.getUser_id();
        }
        return null;
    }

    public static boolean isLogin(Context context) {
        LoginAccountEntity account = ShareConfig.getUserAccount(context);
        if (account != null && !Tools.isEmpty(account.getUser_id())) {
            return true;
        }
        return false;
    }
}
