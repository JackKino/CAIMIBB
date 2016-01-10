package com.cmbb.app.ui.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Storm on 2015/11/16.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements UserLoginSucessListener {
    private String currentTag;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTrans;
    private Map<String, Fragment> fragmentMap = new HashMap<String, Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Tools.setStatuBarTranslate(this);
        super.onCreate(savedInstanceState);
        UserLoginSucessListenerMgr.addUserLoginSucessListener(this);
        ActivityManager.pushActivity(this);
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
        initFragment();
        setUpViews();
        setUpData();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initFragment();
        setUpViews();
        setUpData();
    }

    public void toLogin() {
        startActivity(ActivityLogin.class);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setUpViews();
        setUpData();
    }

    @Override
    public void onUserAccountChanged(LoginAccountEntity account) {

    }

    public void loadImage(ImageView imageView, String url,
                          DisplayImageOptions options) {
        ImageManager.getIntance().loadImage(url, imageView, options);
    }

    public void loadImage(String url, ImageLoadingListener displayListener) {
        ImageManager.getIntance().loadImage(url, displayListener);
    }


    /**
     * @Directions:当前页
     * @author: liman
     * @date: 2015年8月18日
     * @tag:@return
     */
    public String getCurrentFragmentTag() {
        return this.currentTag;
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTrans = fragmentManager.beginTransaction();
    }

    public void addFragment(int containerId, Fragment fragment, String tag) {
        fragmentTrans.add(containerId, fragment, tag);
        fragmentMap.put(tag, fragment);
        fragmentTrans.hide(fragment);
    }

    public void commit() {
        fragmentTrans.commit();
    }

    public void setCurrentFramentByTag(String tag) {
        if (!Tools.isEmpty(tag)) {
            currentTag = tag;
            fragmentTrans = fragmentManager.beginTransaction();
            Iterator<String> keys = fragmentMap.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.equals(tag)) {
                    fragmentTrans.hide(fragmentMap.get(key));
                }
            }
            fragmentTrans.show(fragmentMap.get(tag)).commitAllowingStateLoss();
        }
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


    protected void onRightPressed(View view) {

    }

    public void initTitleWithRightText(int titleRes, int rightRes,
                                       boolean withback) {
        initTitleWithRightText(getString(titleRes), getString(rightRes),
                withback);
    }

    protected boolean onBackClicked() {
        return true;
    }


    /**
     * 界面展示的抽象方法
     */
    public abstract void setUpViews();

    /**
     * 数据展示的抽象方法
     */
    public abstract void setUpData();

    /**
     * 直接启动某个activity
     *
     * @param cls
     */
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
}
