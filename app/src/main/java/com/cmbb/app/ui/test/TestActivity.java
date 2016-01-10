package com.cmbb.app.ui.test;

import android.os.Bundle;
import android.view.View;

import com.cmbb.app.R;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.LoadingAnimationView;

/**
 * Created by admin on 2015/12/15.
 */
public class TestActivity extends BaseActivity {
    private LoadingAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_view);
    }

    @Override
    public void setUpViews() {
        loading = (LoadingAnimationView) findViewById(R.id.loading);
    }

    @Override
    public void setUpData() {

    }

    public void loading(View v) {
        loading.setLoadingStatus(LoadingAnimationView.STATUS_LOADING_ANIMATION);
    }

    public void loadingSucessed(View v) {
        loading.setLoadingStatus(LoadingAnimationView.STATUS_SUCESSED_ANIMATION);
    }

    public void loadingFailed(View v) {
        loading.setLoadingStatus(LoadingAnimationView.STATUS_FAILED_ANIMATION);
    }
}
