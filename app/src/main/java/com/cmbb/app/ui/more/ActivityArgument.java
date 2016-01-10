package com.cmbb.app.ui.more;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cmbb.app.R;
import com.cmbb.app.ui.base.BaseActivity;

/**
 * Created by Storm on 2015/12/10.
 * DES:
 */
public class ActivityArgument extends BaseActivity {
    private int type;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argument);
    }

    @Override
    public void setUpData() {
        String name = "register_text.html";
        if (type == 2) {
            name = "persional_text.html";
        }
        webView.loadUrl("file:///android_asset/" + name);
    }

    @Override
    public void setUpViews() {
        type = getIntent().getExtras().getInt("type");
        if (type == 1) {
            initTitle(R.string.forget_text_des1, true);
        } else {
            initTitle(R.string.forget_text_des2, true);
        }
        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }
}
