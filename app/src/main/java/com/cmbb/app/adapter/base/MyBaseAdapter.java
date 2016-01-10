package com.cmbb.app.adapter.base;

import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cmbb.app.action.mgr.ImageManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public abstract class MyBaseAdapter extends BaseAdapter {
    public void loadImage(ImageView imageView, String url,
                          DisplayImageOptions options) {
        new ImageManager().loadImage(url, imageView, options);
    }
}