package com.cmbb.app.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.cmbb.app.R;

/**
 * Created by Storm on 2015/11/29.
 * DES:
 */
public class DialogMgr {
    private static Dialog dialog;

    public static void showMessageDialog(final Activity context, String message, final boolean needFinish) {
        dialog = new Dialog(context, R.style.Loading_Dialog_WithBackGround_Style);
        dialog.setContentView(R.layout.message_dialog);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (needFinish) {
                    context.finish();
                }
            }
        });
        TextView textView = (TextView) dialog.findViewById(R.id.message_context);
        textView.setText(message);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 500);
    }
}
