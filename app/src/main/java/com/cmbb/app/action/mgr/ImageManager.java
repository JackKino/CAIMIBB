package com.cmbb.app.action.mgr;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImageManager {

    private static ImageManager instance;

    public static ImageManager getIntance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    /**
     * @Directions:加载图片
     * @author: liman
     * @date: 2015年8月17日
     * @tag:@param url
     * @tag:@param imageView
     * @tag:@param options
     */
    public void loadImage(String url, ImageView imageView,
                          DisplayImageOptions options) {
        if (!Tools.isEmpty(url)) {
            if (url.endsWith(";")) {
                url = url.substring(0, url.length() - 1);
            }
        }
        ImageLoader.getInstance().displayImage(url, imageView, options,
                new AnimateFirstDisplayListener());
    }

    /**
     * @des:带回调的
     * @author: liman
     * @date: 2015年9月22日
     * @params: @param url
     * @params: @param imageView
     * @params: @param options
     * @params: @param displayListener
     */
    public void loadImage(String url, ImageView imageView,
                          DisplayImageOptions options,
                          SimpleImageLoadingListener displayListener) {
        if (!Tools.isEmpty(url)) {
            if (url.endsWith(";")) {
                url = url.substring(0, url.length() - 1);
            }
        }
        ImageLoader.getInstance().displayImage(url, imageView, options,
                displayListener);
    }

    public void loadImage(String uri, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(uri, listener);
    }

    /**
     * @Directions:图片加载完成的回调接口，一般不需要手动再写了，这个已经够用了
     * @author: liman
     * @date: 2015年8月14日
     */
    public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            ImageView imageView = (ImageView) view;
            if (imageView != null && loadedImage != null) {
                imageView.setImageBitmap(loadedImage);
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // FadeInBitmapDisplayer.animate(imageView, 1000);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    /**
     * @Directions:获取图片时加载的各种状态时的图片配置函数
     * @author: liman
     * @date: 2015年8月14日
     * @tag:@param onloadingRes
     * @tag:@param emptyUriRes
     * @tag:@param onFailRes
     * @tag:@return
     */
    public static DisplayImageOptions getOption(int onloadingRes,
                                                int emptyUriRes, int onFailRes) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(onloadingRes)
                .showImageForEmptyUri(emptyUriRes).showImageOnFail(onFailRes)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(0)).build();
        return options;
    }

    public static DisplayImageOptions getHeadIconOptions() {
        return getOption(R.mipmap.default_headicon,
                R.mipmap.default_headicon, R.mipmap.default_headicon);
    }

    public static DisplayImageOptions getImageDefaultOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(0)).build();
        return options;
    }
}
