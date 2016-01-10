package com.cmbb.app.ui.main;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Logger;
import com.cmbb.app.action.common.ShareConfig;
import com.cmbb.app.adapter.base.ViewPagerAdapter;
import com.cmbb.app.ui.launcher.LauncherActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/12/15.
 */
public class GuidePage implements View.OnClickListener {
    private LauncherActivity parent;
    private FrameLayout view;
    private ViewPager viewPager;
    private ImageView loadingView1,loadingView2;
    private List<View> viewList = new ArrayList<>();

    public GuidePage(LauncherActivity context) {
        this.parent = context;
        view = (FrameLayout) LayoutInflater.from(this.parent).inflate(R.layout.guide_page, null);
        initView();
    }

    public View getView() {
        return view;
    }

    private void initView() {
        viewPager = (ViewPager) view.findViewById(R.id.guide_view);
        loadingView1 = (ImageView) view.findViewById(R.id.loading_view1);
        loadingView2 = (ImageView) view.findViewById(R.id.loading_view2);

        startNextTimer();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void startNextTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.0f);
                alphaAnimation.setDuration(800);
                alphaAnimation.setFillAfter(true);
                loadingView1.startAnimation(alphaAnimation);

                AlphaAnimation alphaAnimation2 = new AlphaAnimation(0, 1);
                alphaAnimation2.setDuration(800);
                alphaAnimation2.setFillAfter(true);
                loadingView2.startAnimation(alphaAnimation2);
                alphaAnimation2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Logger.i("Animation", "onAnimationEnd");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toGuidePage();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }, 2000);
    }

    private void toGuidePage() {
        if (ShareConfig.getGuideOver(this.parent)) {
            over();
        } else {
            ShareConfig.saveGuideOver(this.parent, true);
            LayoutInflater inflater = LayoutInflater.from(this.parent);
            View page1 = inflater.inflate(R.layout.guide_item_view, null);
            View page2 = inflater.inflate(R.layout.guide_item_view, null);
            View page3 = inflater.inflate(R.layout.guide_item_view, null);
            ImageView image1 = (ImageView) page1.findViewById(R.id.image_view);
            image1.setImageResource(R.mipmap.guide_1);

            ImageView image2 = (ImageView) page2.findViewById(R.id.image_view);
            image2.setImageResource(R.mipmap.guide_2);
            page2.findViewById(R.id.guide_over).setVisibility(View.VISIBLE);
            page2.findViewById(R.id.guide_over).setOnClickListener(this);
            viewList.add(page1);
            viewList.add(page2);
            viewPager.setAdapter(new ViewPagerAdapter(viewList));

            Animation leftOut = AnimationUtils.loadAnimation(this.parent, R.anim.amin_to_left_out);
            leftOut.setFillAfter(true);
            view.findViewById(R.id.loading_).startAnimation(leftOut);

            Animation leftIn = AnimationUtils.loadAnimation(this.parent, R.anim.amin_to_left_in);
            leftIn.setFillAfter(true);
            viewPager.setVisibility(View.VISIBLE);
            viewPager.startAnimation(leftIn);
        }
    }

    private void over() {
    }

    @Override
    public void onClick(View v) {
        over();
    }
}

