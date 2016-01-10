package com.cmbb.app.action.common;

import android.content.Context;

import com.cmbb.app.application.CMBBApplication;
import com.cmbb.app.user.UserManager;

import java.util.HashMap;
import java.util.Map;

public class Params {
    /**
     * 公共参数
     *
     * @param params
     * @return
     */
    private static Map<String, String> getCommonParams(Context context, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        params.put("version_id", String.valueOf(Tools.getVersionCode(context)));
        params.put("push_id", String.valueOf(CMBBApplication.getInstance().getMessageRegistId()));
        String userId = UserManager.getUserId(context);
        if (Tools.isEmpty(userId)) {
            userId = "0";
        }
        params.put("user_id", userId);
        params.put("plat_id", "1");
        return params;
    }

    /**
     * 注册第一步
     *
     * @param mobile
     * @return
     */
    public static Map<String, String> getRegistStep1(Context context, String mobile) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        return getCommonParams(context, params);
    }

    /**
     * 注册第二步
     *
     * @param mobile
     * @param code
     * @return
     */
    public static Map<String, String> getRegistStep2(Context context, String mobile, String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("sms_code", code);
        return getCommonParams(context, params);
    }

    /**
     * 注册第三步
     *
     * @param mobile
     * @param passwd
     * @return
     */
    public static Map<String, String> getRegistStep3(Context context, String mobile, String passwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("passwd", passwd);
        return getCommonParams(context, params);
    }

    /**
     * 登录
     *
     * @param mobile
     * @param passwd
     * @return
     */
    public static Map<String, String> getLoginParams(Context context, String mobile, String passwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("passwd", passwd);
        return getCommonParams(context, params);
    }

    /**
     * 主页
     *
     * @param context
     * @return
     */
    public static Map<String, String> getHomeParams(Context context) {
        Map<String, String> params = new HashMap<String, String>();
        return getCommonParams(context, params);
    }

    /**
     * 理财列表
     *
     * @param context
     * @param current_page
     * @param page_size
     * @return
     */
    public static Map<String, String> getFinancialListParams(Context context, int current_page, int page_size) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("current_page", String.valueOf(current_page));
        params.put("page_size", String.valueOf(page_size));
        return getCommonParams(context, params);
    }

    /**
     * 项目详情
     *
     * @param context
     * @return
     */
    public static Map<String, String> getProjectDetailParams(Context context, String p_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("p_id", p_id);
        return getCommonParams(context, params);
    }

    /**
     * 项目投资记录
     *
     * @param context
     * @param p_id
     * @param current_page
     * @param page_size
     * @return
     */
    public static Map<String, String> getFinacialHistoryParams(Context context, String p_id, int current_page, int page_size) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("p_id", p_id);
        params.put("current_page", String.valueOf(current_page));
        params.put("page_size", String.valueOf(page_size));
        return getCommonParams(context, params);
    }

    /**
     * 账户信息
     *
     * @param context
     * @return
     */
    public static Map<String, String> getCapitalParams(Context context) {
        Map<String, String> params = new HashMap<String, String>();
        return getCommonParams(context, params);
    }

    /**
     * 投资记录
     *
     * @param context
     * @return
     */
    public static Map<String, String> getInvestListParams(Context context, int type_id, int current_page, int page_size) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type_id", String.valueOf(type_id));
        params.put("current_page", String.valueOf(current_page));
        params.put("page_size", String.valueOf(page_size));
        return getCommonParams(context, params);
    }

    /**
     * 流水
     *
     * @param context
     * @param type_id
     * @param current_page
     * @param page_size
     * @return
     */
    public static Map<String, String> getInvestFlowParams(Context context, int type_id, int current_page, int page_size) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type_id", String.valueOf(type_id));
        params.put("current_page", String.valueOf(current_page));
        params.put("page_size", String.valueOf(page_size));
        return getCommonParams(context, params);
    }

    /**
     * 意见反馈
     *
     * @param context
     * @param feedback
     * @return
     */
    public static Map<String, String> getFeedbackParams(Context context, String feedback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("feedback", feedback);
        return getCommonParams(context, params);
    }

    /**
     * 优惠券
     *
     * @return
     */
    public static Map<String, String> getMyPurseList(Context context) {
        Map<String, String> params = new HashMap<String, String>();
        return getCommonParams(context, params);
    }

    /**
     * 充值记录
     *
     * @param context
     * @return
     */
    public static Map<String, String> getRechargeHistory(Context context) {
        Map<String, String> params = new HashMap<String, String>();
        return getCommonParams(context, params);
    }

    /**
     * 安全中心
     *
     * @param context
     * @return
     */
    public static Map<String, String> getSafetyCenter(Context context) {
        Map<String, String> params = new HashMap<String, String>();
        return getCommonParams(context, params);
    }

    /**
     * 忘记密码第一步
     *
     * @param context
     * @param mobile
     * @param type
     * @return
     */
    public static Map<String, String> forgetPwdStep1(Context context, String mobile, int type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("type_id", String.valueOf(type));
        return getCommonParams(context, params);
    }

    /**
     * 忘记密码第二步
     *
     * @param context
     * @param mobile
     * @param type
     * @return
     */
    public static Map<String, String> forgetPwdStep2(Context context, String mobile, String sms_code, int type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("sms_code", sms_code);
        params.put("type_id", String.valueOf(type));
        return getCommonParams(context, params);
    }

    /**
     * 忘记密码第三步
     *
     * @param context
     * @param mobile
     * @param type
     * @return
     */
    public static Map<String, String> forgetPwdStep3(Context context, String mobile, String passwd, int type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("passwd", passwd);
        params.put("type_id", String.valueOf(type));
        return getCommonParams(context, params);
    }

    /**
     * 实名认证
     *
     * @param context
     * @param name
     * @param pid
     * @param card_no
     * @param mobile
     * @param passwd
     * @return
     */
    public static Map<String, String> CertificationParams(Context context, String name, String pid, String card_no, String mobile, String passwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("pid", pid);
        params.put("card_no", card_no);
        params.put("mobile", mobile);
        params.put("passwd", passwd);
        return getCommonParams(context, params);
    }

    /**
     * 第二步
     *
     * @param context
     * @param name
     * @param pid
     * @param card_no
     * @param mobile
     * @param sms_code
     * @param related
     * @return
     */
    public static Map<String, String> CertificationParams2(Context context, String name, String pid, String card_no, String mobile, String sms_code, String related) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("pid", pid);
        params.put("card_no", card_no);
        params.put("mobile", mobile);
        params.put("related", related);
        params.put("sms_code", sms_code);
        return getCommonParams(context, params);
    }


    /**
     * 修改密码
     *
     * @param context
     * @param mobile
     * @param type_id
     * @param old_passwd
     * @param passwd
     * @return
     */
    public static Map<String, String> updatePwdParams(Context context, String mobile, int type_id, String old_passwd, String passwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("type_id", String.valueOf(type_id));
        params.put("old_passwd", old_passwd);
        params.put("passwd", passwd);
        return getCommonParams(context, params);
    }

    /**
     * 充值
     *
     * @param context
     * @param money
     * @return
     */
    public static Map<String, String> rechargeParams(Context context, String money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", money);
        return getCommonParams(context, params);
    }

    /**
     * 充值第二步
     *
     * @param context
     * @param money
     * @param related
     * @return
     */
    public static Map<String, String> rechargeStep2Params(Context context, String money, String related, String sms_code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", money);
        params.put("related", related);
        params.put("sms_code", sms_code);
        return getCommonParams(context, params);
    }

    /**
     * 支付信息
     *
     * @param context
     * @param p_id
     * @param money
     * @return
     */
    public static Map<String, String> productBuyParams(Context context, String p_id, String money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("money", money);
        params.put("p_id", p_id);
        return getCommonParams(context, params);
    }

    /**
     * 开始支付
     *
     * @param context
     * @param p_id
     * @param passwd
     * @param payment   投资金额
     * @param money     实际投资金额
     * @param coupon_id 优惠券ID
     * @return
     */
    public static Map<String, String> getPayParams(Context context, String p_id, String passwd, String payment, String money, String coupon_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("passwd", passwd);
        params.put("p_id", p_id);
        params.put("payment", payment);
        params.put("money", money);
        if (!Tools.isEmpty(coupon_id)) {
            params.put("coupon_id", coupon_id);
        }
        return getCommonParams(context, params);
    }

    /**
     * 实名认证验证码
     *
     * @param context
     * @return
     */
    public static Map<String, String> getCodeParams(Context context, String mobile) {
        Map<java.lang.String, java.lang.String> params = new HashMap<java.lang.String, java.lang.String>();
        params.put("mobile", mobile);
        return getCommonParams(context, params);
    }
}
