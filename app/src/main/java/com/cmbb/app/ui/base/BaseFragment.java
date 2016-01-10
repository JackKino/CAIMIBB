package com.cmbb.app.ui.base;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.MyToast;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.mgr.ImageManager;
import com.cmbb.app.action.mgr.UserLoginSucessListenerMgr;
import com.cmbb.app.entity.login.LoginAccountEntity;
import com.cmbb.app.iface.UserLoginSucessListener;
import com.cmbb.app.net.http.HttpRequest;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.dialog.LoadingDialog;
import com.cmbb.app.ui.login.ActivityLogin;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Storm on 2015/9/29.
 */
public abstract class BaseFragment extends Fragment implements UserLoginSucessListener {
    private View fragmentView;
    protected boolean isVisible;
    protected boolean isLoaded;

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        isVisible = !hidden;
        if (isVisible && !isLoaded) {
            isLoaded = true;
            setUpViews();
            setUpData();
        }
        visibleChanged(isVisible);
    }

    public View initFragmentView(LayoutInflater inflater, ViewGroup container, int resLayout) {
        this.fragmentView = LayoutInflater.from(this.getActivity()).inflate(resLayout, container, false);
        return this.fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserLoginSucessListenerMgr.addUserLoginSucessListener(this);
        Tools.setCustomStatuBarHeight(this.getActivity(), findViewById(R.id.system_bar));
    }

    public void showLoadingAnimation() {
        View loadingView = this.findViewById(R.id.loading);
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    public void closeLoadingAnimation() {
        View loadingView = this.findViewById(R.id.loading);
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    public void toLogin() {
        startActivity(ActivityLogin.class);
    }

    @Override
    public void onUserAccountChanged(LoginAccountEntity account) {
    }

    @Override
    public void onDestroy() {
        UserLoginSucessListenerMgr.removeUserLoginSucessListener(this);
        super.onDestroy();
    }

    protected void visibleChanged(boolean isVisible) {

    }

    /**
     * 初始化所有文件头
     *
     * @param
     * @param canBack
     */
    public void initTitle(String res, boolean canBack) {
        View back = findViewById(R.id.common_back);
        if (back != null) {
            if (!canBack) {
                back.setVisibility(View.INVISIBLE);
            } else {
                back.setVisibility(View.VISIBLE);
            }
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        TextView titleView = (TextView) findViewById(R.id.common_title);
        if (titleView != null) {
            titleView.setText(res);
        }
    }

    public void initTitle(int resid, boolean canBack) {
        initTitle(getString(resid), canBack);
    }

    protected boolean onBackClicked() {
        return true;
    }


    public View findViewById(int id) {
        return fragmentView.findViewById(id);
    }

    /**
     * 界面展示的抽象方法
     */
    public abstract void setUpViews();

    /**
     * 数据展示的抽象方法
     */
    public abstract void setUpData();

    public void startActivity(Class cls) {
        Intent intent = new Intent(this.getActivity(), cls);
        startActivity(intent);
    }

    /**
     * 请求数据
     *
     * @param params
     * @param pv
     * @param callback
     */
    public void httpRequest(Map<String, String> params, String method, String pv, Type cls, RequestCallback callback) {
        HttpRequest.getInstance().request(params, cls, method, pv, callback);
    }

    public void doToast(String msg) {
        MyToast.showToast(this.getActivity(), msg, 2000);
    }

    public void doToast(int resId) {
        doToast(getString(resId));
    }

    public void loadImage(ImageView imageView, String url,
                          DisplayImageOptions options) {
        ImageManager.getIntance().loadImage(url, imageView, options);
    }

    public void loadImage(String url, ImageLoadingListener displayListener) {
        ImageManager.getIntance().loadImage(url, displayListener);
    }

    /**
     * Description:显示加载
     *
     * @author 李满 2015-9-5
     */
    private View loadingView;
    private LoadingDialog loadingDialog;

    public void showLoadingDialog(String msg, boolean canCancel) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this.getActivity());
            loadingDialog.setCancelable(true);
        }
        loadingDialog.setCancelAble(canCancel);
        loadingDialog.setLoadingMessage(msg);
        loadingDialog.show();
    }

    /**
     * 等待框是否是显示状态
     *
     * @return
     */
    public boolean isDialogShowing() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * Description:关闭加载
     *
     * @author 李满 2014-9-5
     */
    public void closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void showLoadingFailedView() {
        final View loadingView = findViewById(R.id.loading_failed);
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            loadingView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            findViewById(R.id.reload_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReloadViewClicked(loadingView);
                }
            });
        }
    }

    public void closeLoadingFailedView() {
        View loadingView = findViewById(R.id.loading_failed);
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    protected void onReloadViewClicked(View view) {
        closeLoadingFailedView();
    }
}

