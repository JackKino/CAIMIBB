package com.cmbb.app.ui.capital;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.cmbb.app.R;
import com.cmbb.app.action.common.Methods;
import com.cmbb.app.action.common.Params;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.adapter.myticket.TicketAdapter;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.entity.capital.CouponEntity;
import com.cmbb.app.entity.capital.CouponItemEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.cmbb.app.ui.base.BaseActivity;
import com.cmbb.app.ui.more.ActivityAeestsHtml;
import com.cmbb.app.ui.more.ActivityMoreWebView;

import java.util.List;

/**
 * Created by Storm on 2015/11/22.
 * 我的优惠券
 */
public class PageMyTicket {
    private View view;
    private ExpandableListView expandableListView;
    private TicketAdapter adapter;
    private BaseActivity mContext;
    private List<CouponItemEntity> couponList;

    public PageMyTicket(BaseActivity context) {
        this.mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.page_ticket, null);
        initViews();
        getData();
    }

    public View getView() {
        return view;
    }

    private void initViews() {
        expandableListView = (ExpandableListView) view.findViewById(R.id.content_view);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(adapter = new TicketAdapter(this.mContext));
        view.findViewById(R.id.coupon_des).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ActivityAeestsHtml.class);
//                intent.putExtra("file_name", "aboutcoupons.html");
//                intent.putExtra("title", mContext.getString(R.string.wallet_ticket_user_detail));
//                mContext.startActivity(intent);
                toActivity(Environments.ABOUT_COUPONS, mContext.getString(R.string.wallet_ticket_user_detail));
            }
        });
        expandAll();
    }

    /**
     * 优惠券使用说明
     */
    private void toActivity(String url, String title) {
        Intent webIntent = new Intent(this.mContext, ActivityMoreWebView.class);
        webIntent.putExtra("url", url);
        webIntent.putExtra("title", title);
        this.mContext.startActivity(webIntent);
    }

    private void getData() {
        mContext.showLoadingDialog(mContext.getString(R.string.common_loading), false);
        this.mContext.httpRequest(Params.getMyPurseList(this.mContext), Methods.METHOD_MY_PURSE_LIST, Environments.PV1, CouponEntity.class, new RequestCallback() {
            @Override
            public void onRequestSucess(BaseEntity result, String jsonData) {
                mContext.closeLoadingDialog();
                if (result.isSucess()) {
                    if (result instanceof CouponEntity) {
                        showDataList((CouponEntity) result);
                    }
                } else {
                    mContext.doToast(result.getMsg());
                }
            }

            @Override
            public void onRequestFailed(String msg) {
                mContext.closeLoadingDialog();
                mContext.doToast(msg);
            }
        });
    }

    private void expandAll() {
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    private void showDataList(CouponEntity entity) {
        couponList = entity.getCoupon();
        if (couponList != null && couponList.size() > 0) {
            adapter.updateList(couponList);
            expandAll();
        }
    }
}
