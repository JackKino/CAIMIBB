package com.cmbb.app.ui.more;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.ui.base.BaseFragment;

/**
 * Created by Storm on 2015/11/21.
 */
public class FragmentMore extends BaseFragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initFragmentView(inflater, container, R.layout.activity_more_fragment);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.main_menu_title4, false);
        findViewById(R.id.more_about_us).setOnClickListener(this);
        findViewById(R.id.more_active_center).setOnClickListener(this);
        findViewById(R.id.more_helper_center).setOnClickListener(this);
        findViewById(R.id.more_connect_us).setOnClickListener(this);
        findViewById(R.id.more_feedback).setOnClickListener(this);
    }

    @Override
    public void setUpData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_about_us:
                toWebView(Environments.URL_ABOUT, getString(R.string.more_abount_us));
                break;
            case R.id.more_active_center:
//                startActivity(ActivityActiveCenter.class);
                toWebView(Environments.URL_ACTIVITY_CENTER, getString(R.string.more_message_center));
                break;
            case R.id.more_feedback:
                startActivity(ActivityFeedback.class);
                break;
            case R.id.more_helper_center:
                //toWebView(Environments.URL_HELPER, getString(R.string.more_helper_center));
                toWebView(Environments.URL_HELPER, getString(R.string.more_helper_center));
                break;
            case R.id.more_connect_us:
//                toWebView(Environments.URL_HELPER, getString(R.string.more_conntect_us));
                showConnnectUsDialog();
                break;
        }
    }

    private void toWebView(String url, String title) {
        Intent intent = new Intent(this.getActivity(), ActivityMoreWebView.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    private Dialog dialog;

    private void showConnnectUsDialog() {
        dialog = new Dialog(this.getActivity(), R.style.Loading_Dialog_WithBackGround_Style);
        dialog.setContentView(R.layout.connect_us_dialog);
        TextView telephone = (TextView) dialog.findViewById(R.id.telephone);
        telephone.setText(Environments.TELEPHONE_CALL);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent phoneIntent = new Intent(Intent.ACTION_CALL,
                        Uri.parse("tel:" + Environments.TELEPHONE_CALL));
                startActivity(phoneIntent);
            }
        });
        dialog.show();
    }
}
