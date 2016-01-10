package com.cmbb.app.ui.more;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cmbb.app.R;
import com.cmbb.app.ui.base.BaseActivity;

/**
 * Created by admin on 2015/12/25.
 */
public class ActivityAeestsHtml extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argument);
    }

    @Override
    public void setUpData() {
        String fileName = getIntent().getExtras().getString("file_name");
        webView.loadUrl("file:///android_asset/" + fileName);
    }

    @Override
    public void setUpViews() {
        String title = getIntent().getExtras().getString("title");
        initTitle(title, true);
        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }
}
