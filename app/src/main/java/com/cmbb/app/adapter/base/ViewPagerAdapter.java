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
package com.cmbb.app.adapter.base;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Description: ViewPager适配器
 * 
 * @author 李满 2014-5-24
 */
public class ViewPagerAdapter extends PagerAdapter {
	private List<View> mList = null;

	public ViewPagerAdapter(List<View> list) {
		this.mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		ViewPager pViewPager = ((ViewPager) container);
		pViewPager.removeView(mList.get(position));
	}

//	@Override
//	public Object instantiateItem(View arg0, int arg1) {
//		ViewPager pViewPager = ((ViewPager) arg0);
//		pViewPager.addView(mList.get(arg1));
//		return mList.get(arg1);
//	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ViewPager pViewPager = ((ViewPager) container);
		pViewPager.addView(mList.get(position));
		return mList.get(position);
	}
}
