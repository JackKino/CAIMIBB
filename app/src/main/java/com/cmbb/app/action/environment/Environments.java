package com.cmbb.app.action.environment;

/**
 * Created by Storm on 2015/11/16.
 * App的环境配置类
 */
public class Environments {
    /**
     * 测试与线上版的开关
     */
    private static final boolean isReleaseVersion = false;

    /**
     * 全局日志打印开关
     */
    public static boolean printLog = true;

    /**
     * 服务器版本
     */
    public static final String PV1 = "v1";

    /**
     * 银行利率
     */
    public static final double RATE_IN_BANK = 1.75;

    /**
     * 服务器地址
     */
    //private static String SERVER_URL = "http://app.caimibb.com/cmbb/%s/%s";

    private static String SERVER_URL = "http://port.caimibb.com/%s/%s";

    /**
     * 帮助中心
     */
    public static String URL_HELPER = "http://www.caimibb.com/html5/help.html";

    /**
     * 关于我们
     */
    public static String URL_ABOUT = "http://www.caimibb.com/html5/aboutUs.html";

    /**
     * 活动中心
     */
    public static String URL_ACTIVITY_CENTER = "http://www.caimibb.com/html5/activityCenter.html";

    /**
     * 联系电话
     */
    public static String TELEPHONE_CALL = "400-755-2688";

    /**
     * 新手有礼
     */
    public static String NEWER_USER = "http://www.caimibb.com/html5/rookie.html";

    /**
     * 优惠券使用说明
     */
    public static String ABOUT_COUPONS = "http://www.caimibb.com/html5/aboutcoupons.html";
    /**
     * 使用协议
     */
    public static String ARGUMENT1 = "http://www.caimibb.com/html5/registrationProtocol.html";
    /**
     * 隐私条款
     */
    public static String ARGUMENT2 = "http://www.caimibb.com/html5/PrivacyPolicy.html";


    /**
     * 根据服务器版本来获取服务器地址
     *
     * @param pv
     * @return
     */
    public static String getServerUrl(String pv, String method) {
        return String.format(SERVER_URL,pv, method);
    }


    /**
     * 静态初始化线上环境
     */
    static {
        if (isReleaseVersion) {
            SERVER_URL = "";
            printLog = false;
        }
    }
}
