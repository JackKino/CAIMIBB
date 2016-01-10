package com.cmbb.app.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.cmbb.app.ui.mgr.ActivityManager;
import com.cmbb.app.views.widget.LoadingAnimationView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Storm on 2015/11/16.
 */
public abstract class BaseActivity extends Activity implements UserLoginSucessListener {
    public String tag = this.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Tools.setStatuBarTranslate(this);
        super.onCreate(savedInstanceState);
        UserLoginSucessListenerMgr.addUserLoginSucessListener(this);
        ActivityManager.pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        UserLoginSucessListenerMgr.removeUserLoginSucessListener(this);
        ActivityManager.popActivity(this);
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Tools.setCustomStatuBarHeight(this, findViewById(R.id.system_bar));
        setUpViews();
        setUpData();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        Tools.setCustomStatuBarHeight(this, findViewById(R.id.system_bar));
        setUpViews();
        setUpData();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        Tools.setCustomStatuBarHeight(this, findViewById(R.id.system_bar));
        setUpViews();
        setUpData();
    }

    @Override
    public void onUserAccountChanged(LoginAccountEntity account) {

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
                    if (onBackClicked()) {
                        finish();
                    }
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


    /**
     * 带右边文字按钮的标题栏
     *
     * @Directions:
     * @author: liman
     * @date: 2015年8月17日
     * @tag:@param titleRes
     * @tag:@param rightRes
     * @tag:@param withback
     */
    public void initTitleWithRightText(String titleRes, String rightRes,
                                       boolean withback) {
        initTitle(titleRes, withback);

        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        if (tv_right != null) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(rightRes);
            tv_right.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    onRightPressed(v);
                }
            });
        }
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
            loadingDialog = new LoadingDialog(this);
            loadingDialog.setCancelable(true);
        }
        loadingDialog.setCancelAble(canCancel);
        loadingDialog.setLoadingMessage(msg);
        loadingDialog.show();
    }

    public void showLoadingSucessDialog(String msg, boolean canCancel, DialogInterface.OnDismissListener dimissListener) {
        if (loadingDialog == null) {
            showLoadingDialog(msg, canCancel);
        }
        loadingDialog.setStatus(LoadingAnimationView.STATUS_SUCESSED_ANIMATION, msg, dimissListener);
    }

    public void showLoadingFailedDialog(String msg, boolean canCancel, DialogInterface.OnDismissListener dimissListener) {
        if (loadingDialog == null) {
            showLoadingDialog(msg, canCancel);
        }
        loadingDialog.setStatus(LoadingAnimationView.STATUS_FAILED_ANIMATION, msg, dimissListener);
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void onRightPressed(View view) {

    }

    public void initTitleWithRightText(int titleRes, int rightRes,
                                       boolean withback) {
        initTitleWithRightText(getString(titleRes), getString(rightRes),
                withback);
    }

    public void toLogin() {
        startActivity(ActivityLogin.class);
    }

    protected boolean onBackClicked() {
        return true;
    }

    public void loadImage(ImageView imageView, String url,
                          DisplayImageOptions options) {
        ImageManager.getIntance().loadImage(url, imageView, options);
    }

    public void loadImage(String url, ImageLoadingListener displayListener) {
        ImageManager.getIntance().loadImage(url, displayListener);
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
        Intent intent = new Intent(this, cls);
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
        MyToast.showToast(this, msg, 2000);
    }

    public void doToast(int resId) {
        doToast(getString(resId));
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
