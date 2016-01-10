package com.cmbb.app.action.mgr;

import com.cmbb.app.entity.login.LoginAccountEntity;
import com.cmbb.app.iface.UserLoginSucessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Storm on 2015/11/25.
 */
public class UserLoginSucessListenerMgr {
    private static List<UserLoginSucessListener> listenerList = new ArrayList<UserLoginSucessListener>();

    public static void addUserLoginSucessListener(UserLoginSucessListener listener) {
        if (listener != null && !listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    public static void removeUserLoginSucessListener(UserLoginSucessListener listener) {
        if (listener != null && listenerList.contains(listener)) {
            listenerList.remove(listener);
        }
    }

    public static void userLoginSucess(LoginAccountEntity account) {
        for (UserLoginSucessListener listener : listenerList) {
            listener.onUserAccountChanged(account);
        }
    }
}
