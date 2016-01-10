package com.cmbb.app.ui.capital;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cmbb.app.R;
import com.cmbb.app.adapter.base.ViewPagerAdapter;
import com.cmbb.app.iface.ViewPagerScrollCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.views.widget.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Storm on 2015/11/22.
 * 我的钱包
 */
public class ActivityMyWallet extends BaseActivity {
    private View moveBar;
    private RadioGroup group;
    private RadioButton button0, button1, button2;
    private MyViewPager viewPager;
    private List<View> viewList = new ArrayList<View>();
    private PageMyTicket pageTicket;
    private PageIncome pageIncome;
    private PageIncomTotal pageIncomeTotal;
    private int moveBar_W = 0;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
    }

    @Override
    public void setUpViews() {
        initTitle(R.string.my_money_title, true);
        moveBar = findViewById(R.id.move_bar);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        group = (RadioGroup) findViewById(R.id.radio_group);
        button0 = (RadioButton) findViewById(R.id.radio_0);
        button1 = (RadioButton) findViewById(R.id.radio_1);
        button2 = (RadioButton) findViewById(R.id.radio_2);
        viewPager = (MyViewPager) findViewById(R.id.view_pager);
        moveBar_W = screenWidth / 3;
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_0:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.radio_1:
                        viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.radio_2:
                        viewPager.setCurrentItem(2, true);
                        break;
                }
            }
        });
        initPager();
        final int index = getIntent().getExtras().getInt("index");
        moveBar.getLayoutParams().width = moveBar_W;
        moveBar.requestLayout();

        if (index == 0) {
            viewPager.setCurrentItem(0, true);
        } else {
            viewPager.setCurrentItem(1, true);
        }
    }

    private void initPager() {
        pageTicket = new PageMyTicket(this);
        viewList.add(pageTicket.getView());
        pageIncome = new PageIncome(this);
        viewList.add(pageIncome.getView());
        pageIncomeTotal = new PageIncomTotal(this);
        viewList.add(pageIncomeTotal.getView());
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        viewPager.setOnScrollCallback(3, moveBar_W, new ViewPagerScrollCallback() {
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
                    button0.setChecked(true);
                } else if (i == 1) {
                    button1.setChecked(true);
                } else {
                    button2.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void setUpData() {

    }
}
