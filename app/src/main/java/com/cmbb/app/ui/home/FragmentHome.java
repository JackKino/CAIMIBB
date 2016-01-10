package com.cmbb.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.action.mgr.ImageManager;
import com.cmbb.app.adapter.base.ViewPagerAdapter;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.financial.FinancialItemEntity;
import com.cmbb.app.entity.home.HomeActivityEntity;
import com.cmbb.app.entity.home.HomeBannerItemEntity;
import com.cmbb.app.entity.home.HomeEntity;
import com.cmbb.app.entity.home.HomeInvestorsItem;
import com.cmbb.app.entity.home.HomeRecommend;
import com.cmbb.app.entity.login.LoginAccountEntity;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseFragment;
import com.cmbb.app.ui.capital.ActivityProjectDetail;
import com.cmbb.app.ui.main.MainFragmentActivity;
import com.cmbb.app.ui.more.ActivityMoreWebView;
import com.cmbb.app.ui.test.TestActivity;
import com.cmbb.app.views.widget.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Storm on 2015/11/21.
 */
public class FragmentHome extends BaseFragment implements View.OnClickListener, RequestCallback, SwipeRefreshLayout.OnRefreshListener {
    private ViewFlipper viewFlipper;
    private HomeEntity homeEntity;
    private TextView tv_day, tv_percent, tv_project_name;
    private ImageView iv_activity;
    private ViewPager bannerViewPager;
    private LayoutInflater layoutInflater;
    private List<View> bannerList = new ArrayList<View>();
    private LinearLayout docLayout;
    private View preDocView;
    private String preJsonData = null;
    private SwipeRefreshLayout refreshLayout;
    private boolean viewLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initFragmentView(inflater, container, R.layout.activity_home_fragment);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.app_name, false);
        layoutInflater = LayoutInflater.from(this.getActivity());
        findViewById(R.id.ll_about).setOnClickListener(this);
        findViewById(R.id.main_more_1).setOnClickListener(this);
        findViewById(R.id.main_more_2).setOnClickListener(this);
        findViewById(R.id.main_func_1).setOnClickListener(this);
        findViewById(R.id.main_func_2).setOnClickListener(this);
        findViewById(R.id.main_func_3).setOnClickListener(this);
        findViewById(R.id.main_add_to).setOnClickListener(this);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshing_layout);
        refreshLayout.setOnRefreshListener(this);

        docLayout = (LinearLayout) findViewById(R.id.doc_layout);
        bannerViewPager = (ViewPager) findViewById(R.id.banner_viewpager);
        bannerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                View currentView = docLayout.getChildAt(i);
                currentView.setAlpha(1.0f);
                preDocView.setAlpha(0.5f);
                preDocView = currentView;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //解决与SwipeRefreshLayout的冲突
        bannerViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        refreshLayout.setEnabled(false);
                        canScroll = false;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        refreshLayout.setEnabled(true);
                        canScroll = true;
                        break;
                }
                return false;
            }
        });

        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        tv_project_name = (TextView) findViewById(R.id.tv_project_name);
        tv_day = (TextView) findViewById(R.id.tv_money);
        tv_percent = (TextView) findViewById(R.id.tv_year_pencent);
        iv_activity = (ImageView) findViewById(R.id.iv_lasted_active);
        iv_activity.setOnClickListener(this);

        viewLoaded = true;
    }

    private void setViewPagerScrollSpeed() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(bannerViewPager.getContext(),
                    new AccelerateInterpolator());
            field.set(bannerViewPager, scroller);
            scroller.setmDuration(200);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        if (viewFlipper.isAutoStart() && viewFlipper.isFlipping()) {
            viewFlipper.stopFlipping();
        }
        stopScrollTimer();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUpData() {
        showLoadingAnimation();
    }

    private void refreshData() {
        if (viewLoaded) {
            httpRequest(Params.getHomeParams(this.getActivity()), Methods.METHOD_MAIN_PAGE, Environments.PV1, HomeEntity.class, this);
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onUserAccountChanged(LoginAccountEntity account) {
        super.onUserAccountChanged(account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_about:
                break;
            case R.id.main_func_1:
                toSurprise();
                break;
            case R.id.main_func_2:
            case R.id.main_more_2:
//                startActivity(ActivityActiveCenter.class);
                toActivityCenter();
                break;
            case R.id.main_func_3:
            case R.id.main_more_1:
                MainFragmentActivity.getInstance().toFinancial();
                break;
            case R.id.main_add_to:
                toProjectDetail();
                break;
            case R.id.iv_lasted_active:
                if (homeEntity != null) {
                    HomeActivityEntity activity = homeEntity.getActivity();
                    if (activity != null) {
                        toActivity(activity.getLink(), activity.getTitle());
                    }
                }
                break;
            case R.id.banner_image: {
                HomeBannerItemEntity item = (HomeBannerItemEntity) v.getTag();
                toActivity(item.getLink(), item.getTitle());
                break;
            }
        }
    }

    private void toActivityCenter() {
//        Intent intent = new Intent(this.getActivity(), ActivityMoreWebView.class);
//        intent.putExtra("title", getString(R.string.more_message_center));
//        intent.putExtra("url", Environments.URL_ACTIVITY_CENTER);
//        startActivity(intent);
        toActivity(Environments.URL_ACTIVITY_CENTER,getString(R.string.more_message_center));
    }

    /**
     * 新手有礼
     */
    private void toSurprise() {
        toActivity(Environments.NEWER_USER, getString(R.string.main_menu_item1));
    }

    private void toProjectDetail() {
        if (homeEntity != null) {
            HomeRecommend recommend = homeEntity.getRecommend();
            if (recommend != null) {
                getProjectDetail();
            }
        }

//        startActivity(TestActivity.class);
    }

    /**
     * 活动页面
     */
    private void toActivity(String url, String title) {
        Intent webIntent = new Intent(this.getActivity(), ActivityMoreWebView.class);
        webIntent.putExtra("url", url);
        webIntent.putExtra("title", title);
        startActivity(webIntent);
    }

    @Override
    public void onRequestSucess(BaseEntity result, String jsonData) {
        closeLoadingAnimation();
        closeLoadingDialog();
        if (result.isSucess()) {
            if (result instanceof HomeEntity) {
                if (preJsonData == null || !preJsonData.equals(jsonData)) {
                    preJsonData = jsonData;
                    homeEntity = (HomeEntity) result;
                    if (homeEntity != null) {
                        showData();
                    }
                }
            }
        }

        stopRefresh();
    }

    @Override
    public void onRequestFailed(String msg) {
        closeLoadingAnimation();
        closeLoadingDialog();
//        doToast(msg);
        showLoadingFailedView();
        stopRefresh();
    }

    private void stopRefresh() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onReloadViewClicked(View view) {
        super.onReloadViewClicked(view);
        setUpData();
    }

    /**
     * 显示数据
     */
    private void showData() {
        initFlipper();
        showRecommend();
        showActivity();
        showBanner();
    }

    /**
     * 初始化跑马灯
     */
    private void initFlipper() {
        List<HomeInvestorsItem> investorsList = homeEntity.getInvestors();
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        if (investorsList != null && investorsList.size() > 0) {
            for (int i = 0; i < investorsList.size(); i++) {
                HomeInvestorsItem item = investorsList.get(i);
                View viewItem = inflater.inflate(R.layout.view_filpper_item, null);
                TextView tv_mobile = (TextView) viewItem.findViewById(R.id.tv_mobile);
                TextView tv_money = (TextView) viewItem.findViewById(R.id.tv_money);
                TextView tv_percent = (TextView) viewItem.findViewById(R.id.tv_pencent);
                tv_mobile.setText(Tools.getHideString(item.getMobile(), 3, 4));
                tv_money.setText(Html.fromHtml("<html>" + getString(R.string.invest_money) + "<font color='#ee5447'>" + Tools.formatMoney(item.getMoney()) + "</font>" + getString(R.string.recharge_acount_yuan) + "</html>"));
                tv_percent.setText(Html.fromHtml("<html>" + getString(R.string.invest_year_percent) + "<font color='#ee5447'>" + Tools.formatPercent(item.getRate()) + "</font>%</html>"));
                viewFlipper.addView(viewItem);
            }
            viewFlipper.setInAnimation(this.getActivity(), R.anim.amin_from_bottom_top);
            viewFlipper.setOutAnimation(this.getActivity(), R.anim.amin_from_top_bottom);
            viewFlipper.setAutoStart(true);
            viewFlipper.startFlipping();
        }
    }

    /**
     * 显示推荐内容
     */
    private void showRecommend() {
        HomeRecommend recommend = homeEntity.getRecommend();
        if (recommend != null) {
            tv_day.setText(recommend.getP_date());
            tv_project_name.setText(recommend.getP_name());
            tv_percent.setText(recommend.getP_rate());
        }
    }

    /**
     * 显示活动数据
     */
    private void showActivity() {
        HomeActivityEntity activity = homeEntity.getActivity();
        if (activity != null) {
            loadImage(iv_activity, activity.getImg(), ImageManager.getImageDefaultOptions());
        }
    }

    /**
     * 广告
     */
    private void showBanner() {
        List<HomeBannerItemEntity> bannerList = homeEntity.getBanner();
        if (bannerList != null && bannerList.size() > 0) {
            for (HomeBannerItemEntity item : bannerList) {
                View view = layoutInflater.inflate(R.layout.banner_item_view, null);
                ImageView img = (ImageView) view.findViewById(R.id.banner_image);
                img.setTag(item);
                loadImage(img, item.getImg(), ImageManager.getImageDefaultOptions());
                this.bannerList.add(view);
                img.setOnClickListener(this);

                View doc = layoutInflater.inflate(R.layout.banner_doc_view, null);
                doc.setAlpha(0.5f);
                docLayout.addView(doc);
            }
            preDocView = docLayout.getChildAt(0);
            preDocView.setAlpha(1.0f);
            bannerViewPager.setAdapter(new ViewPagerAdapter(this.bannerList));
            startScrollTimer();
        }
    }

    /**
     * 自动滚动定时器
     */
    private Timer scrollTimer;
    private final int SCROLL_DELAY = 4000;
    private int currentIndex = 0;
    private int indexFlag = 1;
    private boolean canScroll = true;

    private void startScrollTimer() {
        scrollTimer = new Timer();
        scrollTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (canScroll) {
                    if (currentIndex >= bannerList.size() - 1) {
                        indexFlag = -1;
                    } else if (currentIndex <= 0) {
                        indexFlag = 1;
                    }
                    currentIndex += indexFlag;
                    scrollHandler.sendEmptyMessage(0);
                }
            }
        }, SCROLL_DELAY, SCROLL_DELAY);
    }

    Handler scrollHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bannerViewPager.setCurrentItem(currentIndex, true);
        }
    };

    private void stopScrollTimer() {
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer = null;
        }
    }

    private void getProjectDetail() {
        if (homeEntity != null && homeEntity.getRecommend() != null) {
            showLoadingDialog(getString(R.string.common_loading), true);
            httpRequest(Params.getProjectDetailParams(this.getActivity(), homeEntity.getRecommend().getP_id()), Methods.METHOD_PRODUCT_DETAIL, Environments.PV1, FinancialItemEntity.class, new RequestCallback() {
                @Override
                public void onRequestSucess(BaseEntity result, String jsonData) {
                    closeLoadingDialog();
                    if (result.isSucess()) {
                        if (result instanceof FinancialItemEntity) {
                            FinancialItemEntity item = (FinancialItemEntity) result;
                            Intent intent = new Intent(getActivity(), ActivityProjectDetail.class);
                            intent.putExtra("item", item);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onRequestFailed(String msg) {
                    closeLoadingDialog();
                    doToast(msg);
                }
            });
        }
    }
}
