package com.cmbb.app.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
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
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.adapter.base.ViewPagerAdapter;
import com.cmbb.app.ui.main.GuidePage;
import com.cmbb.app.ui.main.MainFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.InstrumentedActivity;

public class LauncherActivity extends InstrumentedActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private ImageView loadingView1, loadingView2;
    private List<View> viewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Tools.setStatuBarTranslate(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_page);

        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.guide_view);
        loadingView1 = (ImageView) findViewById(R.id.loading_view1);
        loadingView2 = (ImageView) findViewById(R.id.loading_view2);

        initViewPager();

        startNextTimer();
    }

    private void startNextTimer() {
        Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        alphaAnimation.setFillAfter(true);
        loadingView1.startAnimation(alphaAnimation);

        final Animation alphaAnimation2 = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
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
        loadingView2.startAnimation(alphaAnimation2);
    }

    private void toGuidePage() {
        if (ShareConfig.getGuideOver(this)) {
            startActivity();
        } else {
            ShareConfig.saveGuideOver(this, true);
            Animation leftOut = AnimationUtils.loadAnimation(this, R.anim.amin_to_left_out);
            leftOut.setFillAfter(true);
            findViewById(R.id.loading_).startAnimation(leftOut);

            Animation leftIn = AnimationUtils.loadAnimation(this, R.anim.amin_to_left_in);
            leftIn.setFillAfter(true);
            viewPager.setVisibility(View.VISIBLE);
            viewPager.startAnimation(leftIn);
        }
    }

    private void initViewPager() {
        if (!ShareConfig.getGuideOver(this)) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View page1 = inflater.inflate(R.layout.guide_item_view, null);
            View page2 = inflater.inflate(R.layout.guide_item_view, null);
            ImageView image1 = (ImageView) page1.findViewById(R.id.image_view);
            image1.setImageResource(R.mipmap.guide_1);

            ImageView image2 = (ImageView) page2.findViewById(R.id.image_view);
            image2.setImageResource(R.mipmap.guide_2);
            page2.findViewById(R.id.guide_over).setVisibility(View.VISIBLE);
            page2.findViewById(R.id.guide_over).setOnClickListener(this);
            viewList.add(page1);
            viewList.add(page2);
            viewPager.setAdapter(new ViewPagerAdapter(viewList));
        }
    }

    @Override
    public void onClick(View v) {
        startActivity();
    }

    public void startActivity() {
        Bundle bundle = this.getIntent().getExtras();
        Intent intent = new Intent(this, MainFragmentActivity.class);
        if (bundle != null) {
            String message = bundle.getString("extra");
            if (!Tools.isEmpty(message)) {
                intent.putExtra("extra", message);
            }
        }
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
