/* 
 * Copyright (C) 2014 AutonetCo., Ltd. 
 * All Rights Reserved.
 *
 * ALL RIGHTS ARE RESERVED BY AUTONET CO., LTD. ACCESS TO THIS
 * SOURCE CODE IS STRICTLY RESTRICTED UNDER CONTRACT. THIS CODE IS TO
 * BE KEPT STRICTLY CONFIDENTIAL.
 *
 * UNAUTHORIZED MODIFICATION OF THIS FILE WILL VOID YOUR SUPPORT CONTRACT
 * WITH AUTONET CO., LTD. IF SUCH MODIFICATIONS ARE FOR THE PURPOSE
 * OF CIRCUMVENTING LICENSING LIMITATIONS, LEGAL ACTION MAY RESULT.
 */
package com.cmbb.app.action.common;

import android.util.Log;

import com.cmbb.app.action.environment.Environments;

/**
 * Description:日志打印
 *
 * @author 李满 2014-9-1
 */
public class Logger {
    private static String TAG = "caimibb: ";
    private static String MSG = "(msg)";
    private static String TIME = "(time)";

    public static void e(String tag, String msg) {
        if (Environments.printLog) {
            Log.e(tag, TAG + MSG + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (Environments.printLog) {
            Log.i(tag, TAG + MSG + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (Environments.printLog) {
            Log.d(tag, TAG + MSG + msg);
        }
    }

    public static void v(String tag, String msg) {
        if (Environments.printLog) {
            Log.v(tag, TAG + MSG + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Environments.printLog) {
            Log.w(tag, TAG + MSG + msg);
        }
    }

    public static void printTime(String tag, String msg) {
        if (Environments.printLog) {
            Log.i(tag, TAG + TIME + msg);
        }
    }
}
