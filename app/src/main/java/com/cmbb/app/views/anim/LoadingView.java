package com.cmbb.app.views.anim;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cmbb.app.R;

/**
 * Created by Storm on 2015/12/7.
 * DES:
 */
public class LoadingView extends LinearLayout {
    private View view;
    private Context context;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.loading_animation, null);
        this.addView(view);

        initAnimaton();
    }

    private void initAnimaton() {
        ImageView bg = (ImageView) view.findViewById(R.id.view1);
        ImageView forground = (ImageView) view.findViewById(R.id.view2);
        Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatMode(Animation.INFINITE);
        rotateAnimation.setDuration(1000);
        forground.startAnimation(rotateAnimation);

        bg.setBackgroundResource(R.anim.loading_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) bg.getBackground();
        animationDrawable.start();//开始
    }
}
