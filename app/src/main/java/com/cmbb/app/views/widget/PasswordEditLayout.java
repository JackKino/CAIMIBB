package com.cmbb.app.views.widget;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmbb.app.R;
import com.cmbb.app.iface.OnEditFinishListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/12/10.
 */
public class PasswordEditLayout extends LinearLayout {
    private Context mContext;
    private EditText editText;
    private List<TextView> numbers;
    private OnEditFinishListener listener;
    private final int MAX_LEN = 6;

    public PasswordEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initEdit();
    }

    public EditText getEditText() {
        return this.editText;
    }

    public void setOnPasswordEidtOverListener(OnEditFinishListener listener) {
        this.listener = listener;
    }

    private void initEdit() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.password_edit_layout, null);
        this.addView(view);
        numbers = new ArrayList<TextView>();
        editText = (EditText) view.findViewById(R.id.edit_view);
        numbers.add((TextView) view.findViewById(R.id.number_1));
        numbers.add((TextView) view.findViewById(R.id.number_2));
        numbers.add((TextView) view.findViewById(R.id.number_3));
        numbers.add((TextView) view.findViewById(R.id.number_4));
        numbers.add((TextView) view.findViewById(R.id.number_5));
        numbers.add((TextView) view.findViewById(R.id.number_6));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String value = s.toString();
                for (int i = 0; i < MAX_LEN; i++) {
                    if (i > value.length() - 1) {
                        numbers.get(i).setText(null);
                    } else {
                        numbers.get(i).setText("*");
                    }
                }
                if (value.length() == MAX_LEN) {
                    if (listener != null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listener.onPasswordEditOver(value);
                                editText.setText(null);
                            }
                        }, 500);
                    }
                }
            }
        });
    }

    public String getText() {
        return editText.getText().toString();
    }
}
