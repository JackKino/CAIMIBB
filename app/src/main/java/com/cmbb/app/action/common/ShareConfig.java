package com.cmbb.app.action.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.cmbb.app.action.mgr.UserLoginSucessListenerMgr;
import com.cmbb.app.entity.login.LoginAccountEntity;
import com.google.gson.Gson;

/**
 * Created by Storm on 2015/11/25.
 */
public class ShareConfig {
    private static final String XML_USER_ACCOUNT = "XML_USER_ACCOUNT";
    private static final String XML_GUIDE_OVER = "XML_GUIDE_OVER";

    /**
     * 保存用户信息
     *
     * @param context
     * @param account
     */
    public static void saveUserAccount(Context context, LoginAccountEntity account) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_USER_ACCOUNT, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = "";
        if (account != null) {
            value = new Gson().toJson(account);
        }
        editor.putString(XML_USER_ACCOUNT, value);
        editor.commit();

        /**
         * 通知各监听回调
         */
        UserLoginSucessListenerMgr.userLoginSucess(account);
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @return
     */
    public static LoginAccountEntity getUserAccount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_USER_ACCOUNT, Activity.MODE_PRIVATE);
        String account = sharedPreferences.getString(XML_USER_ACCOUNT, "");
        if (!Tools.isEmpty(account)) {
            LoginAccountEntity accountEnttiy = new Gson().fromJson(account, LoginAccountEntity.class);
            return accountEnttiy;
        }
        return null;
    }

    /**
     * 引导过标记
     *
     * @param context
     * @param isOver
     */
    public static void saveGuideOver(Context context, boolean isOver) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_GUIDE_OVER, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(XML_GUIDE_OVER, isOver);
        editor.commit();
    }

    /**
     * 引导过标记
     *
     * @param context
     * @return
     */
    public static boolean getGuideOver(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(XML_GUIDE_OVER, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(XML_GUIDE_OVER, false);
    }
}
