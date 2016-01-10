package com.cmbb.app.views.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.iface.TextChangeAfterCallback;


public class EditLayout extends LinearLayout {
    private View view;
    private Context context;
    private EditText editText;
    private ImageView deleteImg;
    private TextChangeAfterCallback callback;
    private String mDegit;
    private int inputType = InputType.TYPE_CLASS_TEXT;
    private boolean isPasswd = false;

    public EditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.edit_layout, this,
                false);
        this.addView(view);
        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasWindowFocus);
    }

    public void setDegit(String degit) {
        if (!Tools.isEmpty(degit)) {
            this.mDegit = degit;
//            editText.setKeyListener(new DigitsKeyListener() {
//                protected char[] getAcceptedChars() {
//                    return mDegit;
//                }
//            });
//            editText.setKeyListener(DigitsKeyListener.getInstance(degit));
        }
    }

    public void setMaxLengh(int length) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    public String getText() {
        return this.editText.getText().toString();
    }

    public void setText(String text) {
        this.editText.setText(text);
    }

    public void setHide(String res) {
        this.editText.setHint(res);
    }

    public void drawableLeft(int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        editText.setCompoundDrawables(drawable, null, null, null);
    }

    public void setIsPassword(boolean isPassword) {
        isPasswd = isPassword;
        if (isPassword) {
            if (inputType == InputType.TYPE_CLASS_NUMBER) {
                setInputType(this.inputType);
                this.editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                this.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //字符
            }
        } else {
            this.editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            if (inputType == InputType.TYPE_CLASS_NUMBER) {
                setInputType(this.inputType);
                this.editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                this.editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); //字符
            }
        }
        editText.postInvalidate();
        this.editText.setSelection(this.editText.getText().length());
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
        this.editText.setInputType(inputType);
    }

    public void setHide(int res) {
        setHide(context.getString(res));
    }

    public EditText getEditText() {
        return this.editText;
    }

    public ImageView getDeleteButton() {
        return this.deleteImg;
    }

    private void initView() {
        editText = (EditText) view.findViewById(R.id.content_edit);
        deleteImg = (ImageView) view.findViewById(R.id.content_delete);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                if (!Tools.isEmpty(mDegit)) {
                    char[] degit = new char[arg0.length()];
                    arg0.getChars(0, arg0.length(), degit, 0);
                    for (int i = 0; i < degit.length; i++) {
                        char ch = degit[i];
                        if (!mDegit.contains(String.valueOf(ch))) {
                            String value = arg0.toString();
                            value = value.replaceAll(String.valueOf(ch), "");
                            editText.setText(value);
                            return;
                        }
                    }
                }

                if (arg0.length() > 0 && editText.hasFocus()) {
                    deleteImg.setVisibility(View.VISIBLE);
                } else {
                    deleteImg.setVisibility(View.INVISIBLE);
                }
                if (callback != null) {
                    callback.textChangeAfter(arg0);
                }
            }
        });
        deleteImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                editText.setText("");
            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    deleteImg.setVisibility(View.INVISIBLE);
                } else {
                    String content = editText.getText().toString();
                    if (!Tools.isEmpty(content)) {
                        deleteImg.setVisibility(View.VISIBLE);
                    } else {
                        deleteImg.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    public void setOnTextChangeAfter(TextChangeAfterCallback callback) {
        this.callback = callback;
    }
}
