package com.cmbb.app.ui.mgr;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Created by admin on 2015/12/22.
 */
public class ActivityManager {
    private static LinkedList<Activity> activityLinkedList = new LinkedList<>();

    public static void pushActivity(Activity activity) {
        if (activity != null) {
            activityLinkedList.add(activity);
        }
    }

    public static void popActivity(Activity activity) {
        if (activity != null) {
            activityLinkedList.remove(activity);
        }
    }

    public static void clearActivities() {
        activityLinkedList.clear();
    }

    public static Activity getTopActivity() {
        if (activityLinkedList.size() > 0) {
            return activityLinkedList.get(activityLinkedList.size() - 1);
        }
        return null;
    }
}
