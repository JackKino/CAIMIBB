package com.cmbb.app.ui.capital;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cmbb.app.R;
import com.cmbb.app.adapter.base.FragmentAdapter;
import com.cmbb.app.application.CMBBApplication;
import com.cmbb.app.iface.ViewPagerScrollCallback;
import com.cmbb.app.ui.base.BaseFragmentActivity;
import com.cmbb.app.views.widget.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Storm on 2015/11/23.
 * 投资记录
 */
public class ActivityInvestList extends BaseFragmentActivity {
    private MyViewPager viewPager;
    private int moveBar_w;
    private View moveBar;
    private RadioGroup group;
    private RadioButton investing, investing_back;
    private List<Fragment> viewList = new ArrayList<Fragment>();
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_list);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.my_invest_history, true);
        moveBar = findViewById(R.id.move_bar);
        viewPager = (MyViewPager) findViewById(R.id.view_pager);
        group = (RadioGroup) findViewById(R.id.radio_group);
        investing = (RadioButton) findViewById(R.id.investing);
        investing_back = (RadioButton) findViewById(R.id.investing_back);
        moveBar_w = CMBBApplication.getInstance().getScreenWidth() / 2;
        moveBar.getLayoutParams().width = moveBar_w;
        moveBar.requestLayout();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.investing:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.investing_back:
                        viewPager.setCurrentItem(1, true);
                        break;
                }
            }
        });
        initViewPager();
        viewPager.setCurrentItem(0, true);
    }

    private void initViewPager() {
        InvestFragmet fragment = new InvestFragmet();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        fragment.setArguments(bundle);
        viewList.add(fragment);
        fragment = new InvestFragmet();
        bundle = new Bundle();
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        viewList.add(fragment);

        viewPager.setOnScrollCallback(2, moveBar_w, new ViewPagerScrollCallback() {
            @Override
            public void scolling(int l) {
                ((FrameLayout.LayoutParams) moveBar.getLayoutParams()).leftMargin = l;
                moveBar.requestLayout();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    investing.setChecked(true);
                } else if (i == 1) {
                    investing_back.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        viewPager.setAdapter(adapter = new FragmentAdapter(this.getSupportFragmentManager()));
        adapter.updatePager(viewList);
    }

    @Override
    public void setUpData() {

    }

}
