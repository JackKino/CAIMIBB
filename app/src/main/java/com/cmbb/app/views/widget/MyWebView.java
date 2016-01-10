package com.cmbb.app.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Storm on 2015/11/22.
 */
public class MyWebView extends WebView {
    private ProgressBar progressbar;

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WebSettings webSetting = getSettings();
        //支持javascript
        webSetting.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSetting.setSupportZoom(true);
        webSetting.setDisplayZoomControls(true);
        // 设置出现缩放工具
        webSetting.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSetting.setUseWideViewPort(true);
        //自适应屏幕
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setLoadWithOverviewMode(true);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5, 0, 0));
        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(INVISIBLE);
            } else {
                if (progressbar.getVisibility() == INVISIBLE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
