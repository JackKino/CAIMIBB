package com.cmbb.app.views.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.cmbb.app.application.CMBBApplication;
import com.cmbb.app.iface.ViewPagerScrollCallback;


public class MyViewPager extends ViewPager {
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;
    private ViewPagerScrollCallback callback;
    private int pageSize;
    private int slideWidth;
    private int hSpace;
    private int paddingLeft;
    private int screenWidth;
    private int delay = -1;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // @Override
    // public boolean onInterceptTouchEvent(MotionEvent ev) {
    // switch (ev.getAction()) {
    // case MotionEvent.ACTION_DOWN:
    // xDistance = yDistance = 0f;
    // xLast = ev.getX();
    // yLast = ev.getY();
    // break;
    // case MotionEvent.ACTION_MOVE:
    // final float curX = ev.getX();
    // final float curY = ev.getY();
    //
    // xDistance += Math.abs(curX - xLast);
    // yDistance += Math.abs(curY - yLast);
    // xLast = curX;
    // yLast = curY;
    //
    // if (xDistance > yDistance) {
    // return false;
    // }
    // }
    //
    // return super.onInterceptTouchEvent(ev);
    // }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        if (callback != null) {
            int left = this.paddingLeft + (l * hSpace) / screenWidth;
            callback.scolling(left + delay);
//            Logger.i("slide left", left + "" + " delay =" + delay);
        }
    }

    public void setOnScrollCallback(int pageSize, int slideWidth,
                                    ViewPagerScrollCallback callback) {
        this.callback = callback;
        this.pageSize = pageSize;
        this.slideWidth = slideWidth;
        this.screenWidth = CMBBApplication.getInstance().getScreenWidth();
        this.hSpace = screenWidth / this.pageSize;
        this.paddingLeft = (this.hSpace - slideWidth) / 2;
        onScrollChanged(0, 0, 0, 0);
    }

    @Override
    public void setCurrentItem(int item) {
        if (delay == -1) {
            onScrollChanged(this.screenWidth * item, 0, 0, 0);
            delay = this.slideWidth * item;
        }
        super.setCurrentItem(item);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (delay == -1) {
            onScrollChanged(this.screenWidth * item, 0, 0, 0);
            delay = this.slideWidth * item;
        }
        super.setCurrentItem(item, smoothScroll);
    }
}
