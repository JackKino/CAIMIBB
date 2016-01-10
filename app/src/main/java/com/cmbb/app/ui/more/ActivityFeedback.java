package com.cmbb.app.ui.more;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;

/**
 * Created by Storm on 2015/11/21.
 */
public class ActivityFeedback extends BaseActivity {
    private EditText feedbackEdit;
    private TextView editLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_feedback);
    }

    @Override
    public void setUpViews() {
        initTitleWithRightText(getString(R.string.more_feedback), getString(R.string.common_text_commit), true);
        feedbackEdit = (EditText) findViewById(R.id.et_feedback_edit);
        feedbackEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLengh(s.toString().trim().length());
            }
        });
        editLeft = (TextView) findViewById(R.id.tv_edit_left);
        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Tools.hideKeyBoard(ActivityFeedback.this, feedbackEdit);
                }
                return false;
            }
        });
        checkLengh(0);
    }

    private void checkLengh(int len) {
        len = 100 - len;
        editLeft.setText(getString(R.string.more_feedback_edit_left, len));

        if (len == 0) {
            editLeft.setTextColor(getResources().getColor(R.color.color_ee5447));
        } else {
            editLeft.setTextColor(getResources().getColor(R.color.color_999999));
        }
    }

    @Override

    protected void onRightPressed(View view) {
        super.onRightPressed(view);
        final String text = feedbackEdit.getText().toString().trim();
        if (Tools.isEmpty(text)) {
            doToast(R.string.more_feedback_edit_null);
            return;
        }

        showLoadingDialog(getString(R.string.common_commiting), false);
        Tools.hideKeyBoard(ActivityFeedback.this, feedbackEdit);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                httpRequest(Params.getFeedbackParams(ActivityFeedback.this, text), Methods.METHOD_FEEDBACK, Environments.PV1, BaseEntity.class, new RequestCallback() {
                    @Override
                    public void onRequestSucess(BaseEntity result, String jsonData) {
                        if (result.isSucess()) {
                            showLoadingSucessDialog(getString(R.string.more_feedback_commit_sucess), false, new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    finish();
                                }
                            });
                        } else {
                            showLoadingFailedDialog(result.getMsg(), false, null);
                        }
                    }

                    @Override
                    public void onRequestFailed(String msg) {
                        closeLoadingDialog();
                        doToast(msg);
                    }
                });
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setUpData() {

    }
}
