package com.cmbb.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.views.widget.LoadingAnimationView;

public class LoadingDialog extends Dialog {
    private View loadingView;
    private LoadingAnimationView progress;
    private TextView msgView;

    public LoadingDialog(Context context) {
        super(context, R.style.Loading_Dialog_Style);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    private void initView(Context context) {
        loadingView = LayoutInflater.from(context).inflate(
                R.layout.loading_view, null);
        this.setContentView(loadingView);

        progress = (LoadingAnimationView) findViewById(R.id.progress);
        msgView = (TextView) findViewById(R.id.msg);
    }

    public void setCancelAble(boolean canCancel) {
        this.setCancelable(canCancel);
    }

    public void setLoadingMessage(String message) {
        if (loadingView != null && msgView != null) {
            progress.setLoadingStatus(LoadingAnimationView.STATUS_LOADING_ANIMATION);
            if (!Tools.isEmpty(message)) {
                msgView.setVisibility(View.VISIBLE);
                msgView.setText(message);
            } else {
                msgView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * //    public static final int STATUS_LOADING_ANIMATION = 1;//加载中
     * //    public static final int STATUS_SUCESSED_ANIMATION = 2; //加载成功
     * //    public static final int STATUS_FAILED_ANIMATION = 3; //加载失败
     *
     * @param loadingAnimationViewStatus
     * @param msg
     */
    public void setStatus(int loadingAnimationViewStatus, String msg, final DialogInterface.OnDismissListener dimissListener) {
        if (loadingView != null && progress != null && msgView != null) {
            setLoadingMessage(msg);
            progress.setLoadingStatus(loadingAnimationViewStatus);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (dimissListener != null) {
                        dimissListener.onDismiss(LoadingDialog.this);
                    }
          LoadingDialog.this.dismiss();
                }
            }, 2000);
        }
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
    }
}
