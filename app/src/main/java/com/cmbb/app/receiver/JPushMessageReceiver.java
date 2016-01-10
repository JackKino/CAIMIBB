package com.cmbb.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cmbb.app.action.common.Logger;
import com.cmbb.app.action.common.SystemUtils;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.ui.home.MessageDetailActivity;
import com.cmbb.app.ui.main.MainFragmentActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by admin on 2015/12/22.
 */
public class JPushMessageReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        String content = "";

        String title = bundle.getString(JPushInterface.EXTRA_TITLE);//保存服务器推送下来的消息的标题。
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//保存服务器推送下来的附加字段。这是个 JSON 字符串
        String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);//保存服务器推送下来的内容类型。
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);//保存服务器推送下来的消息内容
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        int notificationId = bundle
                .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
            content = message;
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action) || JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            content = alert;
        }
        Logger.i("JPushMessageReceiver",
                intent.getAction());
        if (!Tools.isEmpty(content)) {
            if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
                detailMessage(content, 0);
            } else if (intent.getAction().equals(
                    JPushInterface.ACTION_REGISTRATION_ID)) {
            } else if (intent.getAction().equals(
                    JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
                //接收到推送下来的通知
                JPushInterface.removeLocalNotification(this.context, notificationId);
                detailMessage(content, 0);
            } else if (intent.getAction().equals(
                    JPushInterface.ACTION_NOTIFICATION_OPENED)) {
                //用户点击打开了通知
                messageOpen(extras, notificationId, 1);
            }
            else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.i("JPushMessageReceiver", "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.i("JPushMessageReceiver" , "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.i("JPushMessageReceiver" , "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        }

        Logger.i("JPushMessageReceiver", "JPushMessageReceiver title = (" + title + ")   extras = ("
                + extras + ")" + " type = (" + type + ")" + " message = ("
                + message + ")  alert = " + alert);
    }

    private void detailMessage(String exraMessage, int type) {
        toDetail(exraMessage, type);
    }

    private void messageOpen(String extra, int notificationId, int type) {
        JPushInterface.removeLocalNotification(this.context, notificationId);
        toDetail(extra, type);
    }

    private void toDetail(String extra, int type) {
        if (/*SystemUtils.isAppAlive(context, this.context.getPackageName())*/ MainFragmentActivity.getInstance() != null && type == 0) {
            Intent intent = new Intent(this.context,
                    MessageDetailActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("extra", extra);
            context.startActivity(intent);
        } else if (type == 1) {
            if (MainFragmentActivity.getInstance() != null) {
                Intent intent = new Intent(this.context,
                        MessageDetailActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("extra", extra);
                context.startActivity(intent);
            } else {
                Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                launchIntent.putExtra("extra", extra);
                context.startActivity(launchIntent);
            }
        }
    }
}
