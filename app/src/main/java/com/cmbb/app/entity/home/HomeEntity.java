package com.cmbb.app.entity.home;

import com.cmbb.app.entity.base.BaseEntity;

import java.util.List;

/**
 * Created by Storm on 2015/11/25.
 */
public class HomeEntity extends BaseEntity {
    private List<HomeBannerItemEntity> banner;
    private HomeActivityEntity activity;
    private List<HomeInvestorsItem> investors;
    private HomeRecommend recommend;

    public List<HomeBannerItemEntity> getBanner() {
        return banner;
    }

    public void setBanner(List<HomeBannerItemEntity> banner) {
        this.banner = banner;
    }

    public HomeActivityEntity getActivity() {
        return activity;
    }

    public void setActivity(HomeActivityEntity activity) {
        this.activity = activity;
    }

    public List<HomeInvestorsItem> getInvestors() {
        return investors;
    }

    public void setInvestors(List<HomeInvestorsItem> investors) {
        this.investors = investors;
    }

    public HomeRecommend getRecommend() {
        return recommend;
    }

    public void setRecommend(HomeRecommend recommend) {
        this.recommend = recommend;
    }
}
