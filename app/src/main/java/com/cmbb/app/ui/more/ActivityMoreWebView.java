package com.cmbb.app.ui.more;

import android.os.Bundle;
import android.view.KeyEvent;

import com.cmbb.app.R;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.MyWebView;

/**
 * Created by Storm on 2015/11/21.
 */
public class ActivityMoreWebView extends BaseActivity {
    private String webUrl;
    private MyWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_webview);
    }

    @Override
    public void setUpViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            webUrl = bundle.getString("url");
            initTitle(bundle.getString("title"), true);
            webView = (MyWebView) findViewById(R.id.webview);
            initWebView();
        }
    }

    @Override
    protected void onDestroy() {
//        webView.clearCache(true);
        webView.destroyDrawingCache();
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public void setUpData() {

    }

    private void initWebView() {
        webView.loadUrl(webUrl);
    }

    @Override
    protected boolean onBackClicked() {
        if (webView.canGoBack()) {
            webView.goBack();
            return false;
        }
        return super.onBackClicked();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView.canGoBack()) {
            webView.goBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
