package com.cmbb.app.ui.certification;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.EditLayout;

/**
 * Created by Storm on 2015/12/2.
 * DES:实名认证第一步
 */
public class ActivityCertificationStep1 extends BaseActivity {
    private EditLayout edit1, edit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_step1);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.certification_step1, true);
        edit1 = (EditLayout) findViewById(R.id.edit_pwd1);
        edit1.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit1.setHide(R.string.certification_step1_pwd_hide);
        edit1.setIsPassword(true);
        edit1.setDegit(getString(R.string.common_degit_number));
        edit1.setMaxLengh(6);
        edit2 = (EditLayout) findViewById(R.id.edit_pwd2);
        edit2.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit2.setIsPassword(true);
        edit2.setMaxLengh(6);
        edit2.setDegit(getString(R.string.common_degit_number));
        edit2.setHide(R.string.certification_step1_pwd_hide);
        findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPwd();
            }
        });
        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Tools.hideKeyBoard(ActivityCertificationStep1.this, edit1.getEditText());
                return false;
            }
        });
    }

    private void checkPwd() {
        Tools.hideKeyBoard(this, edit1.getEditText());
        String pwd1 = edit1.getText();
        String pwd2 = edit2.getText();
        if (Tools.isEmpty(pwd1) || Tools.isEmpty(pwd2) || pwd1.length() < 6 || pwd2.length() < 6) {
            doToast(R.string.certification_step1_pwd_toast);
            return;
        }
        if (!pwd1.equals(pwd2)) {
            doToast(R.string.certification_step1_pwd_unequals);
            return;
        }

        Intent intent = new Intent(this, ActivityCertificationStep2.class);
        intent.putExtra("pwd", pwd1);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void setUpData() {

    }
}
