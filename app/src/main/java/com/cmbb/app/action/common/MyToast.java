package com.cmbb.app.action.common;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * @Directions:有序弹出Toast,不会出现多次重复的弹的情况
 * @author: liman
 * @date: 2015年8月17日
 */
public class MyToast {
	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	public static void showToast(Context mContext, String text, int duration) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		}
		mHandler.postDelayed(r, duration);
		mToast.show();
	}
}
