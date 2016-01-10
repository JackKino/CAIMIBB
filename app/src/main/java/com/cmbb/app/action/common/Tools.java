package com.cmbb.app.action.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cmbb.app.application.CMBBApplication;
import com.cmbb.app.views.systembar.SystemBarTintManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Tools {
    /**
     * Description:隐藏键盘
     *
     * @param context
     * @param view
     * @author 李满 2014年12月4日
     */
    public static void hideKeyBoard(Context context, EditText view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
    }

    /**
     * Description:显示键盘
     *
     * @param @param context
     * @param @param view
     * @author 李满 2015年5月6日
     */
    public static void showKeyBoard(Context context, EditText view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Description:判断键盘的打开状态
     *
     * @param @param  context
     * @param @return
     * @author 李满 2015年5月6日
     */
    public static boolean isKeyBoardShowing(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();// isOpen若返回true，则表示输入法打开
    }

    /**
     * Description:判断空
     *
     * @param str
     * @return
     * @author 李满 2014-10-23
     */
    public static boolean isEmpty(String str) {
        if ("".equals(str) || str == null) {
            return true;
        }
        return false;
    }

    /**
     * Description:检查字符长度是否符合规范
     *
     * @param value
     * @param minLen
     * @param maxLen
     * @return
     * @author 李满 2014-10-23
     */
    public static boolean checkStrLenRight(String value, int minLen, int maxLen) {
        if (isEmpty(value)) {
            return false;
        }
        if (value.length() > maxLen || value.length() < minLen) {
            return false;
        }
        return true;
    }

    /**
     * Description:检查网络是否可用
     *
     * @param context
     * @return
     * @author 李满 2014-6-16
     */
    public static boolean checkNetworkEnable(Context context) {
        ConnectivityManager cwjManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cwjManager.getActiveNetworkInfo();
        if (network == null)
            return false;
        return network.isAvailable();
    }

    /**
     * Description:获取文件名
     *
     * @param filePath
     * @return
     * @author 李满 2014年12月5日
     */
    public static String getFileName(String filePath) {
        if (!isEmpty(filePath)) {
            if (filePath.contains("/")) {
                int index = filePath.lastIndexOf("/");
                return filePath.substring(index);
            }
        }
        return filePath;
    }

    /**
     * Description:时间格式化
     *
     * @param time
     * @param param
     * @return
     * @author 李满 2014-10-28
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatTime(long time, String param) {
        SimpleDateFormat format = new SimpleDateFormat(param);
        return format.format(new Date(time));
    }

    /**
     * Description:压缩图片
     *
     * @param @param  pathName
     * @param @param  targetWidth
     * @param @param  targetHeight
     * @param @return
     * @author 李满 2014年12月30日
     */
    public static Bitmap compressBySize(String pathName, int targetWidth,
                                        int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }

    /**
     * Description:保存图片
     *
     * @param @param  bm
     * @param @param  fileName
     * @param @throws Exception
     * @author 李满 2014年12月30日
     */
    public static void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);
        // // 检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete(); // 删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        // 100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 手机屏幕宽高
     */
    private static DisplayMetrics dm = null;

    public static int getScreenWidth(Activity context) {
        if (dm == null) {
            dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        // 获取屏幕信息
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity context) {
        if (dm == null) {
            dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        // 获取屏幕信息
        return dm.heightPixels;
    }

    /**
     * Description:文件拷贝
     *
     * @param @param  oldPath
     * @param @param  newPath
     * @param @return
     * @author 李满 2014年12月30日
     */
    public static boolean copyFile(String oldPath, String newPath) {
        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(oldPath);
        if (oldfile.exists()) { // 文件存在时
            try {
                File newFile = new File(newPath);
                if (newFile.exists()) {
                    newFile.delete();
                }
                newFile.createNewFile();
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                    fs.flush();
                }
                inStream.close();
                fs.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Description:保留两位小数
     *
     * @param @param  value
     * @param @return
     * @author 李满 2014年12月31日
     */
    public static BigDecimal getDouble2(double value) {
        BigDecimal original = new BigDecimal(value);
        return original.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * Description:小数点后两位
     *
     * @param format
     * @return
     * @author 李满 2014-10-23
     */
    public static String formatString(double format) {
        // TODO Auto-generated method stub
        String value = String.format("%.2f", Double.valueOf(format));
        return value;
    }

    /**
     * Description:打开网页链接
     *
     * @param @param context
     * @param @param url
     * @author 李满 2015年1月8日
     */
    public static void openWebLink(Context context, String url) {
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * escription:检查是不是wifi环境
     *
     * @param @param  context
     * @param @return
     * @author 李满 2015年1月21日
     */
    public static boolean isWifiNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Description:某月日历列表
     *
     * @param @param  calendar
     * @param @return
     * @author 李满 2015年1月28日
     */
    public static ArrayList<Date> getDates(Calendar calendar) {
        UpdateStartDateForMonth(calendar);
        ArrayList<Date> alArrayList = new ArrayList<Date>();

        for (int i = 1; i <= 42; i++) {
            alArrayList.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return alArrayList;
    }

    private static void UpdateStartDateForMonth(Calendar calendar) {
        calendar.set(Calendar.DATE, 1); // 设置成当月第一天
        // int iMonthViewCurrentMonth = calendar.get(Calendar.MONTH);//
        // 得到当前日历显示的月

        // 星期一是2 星期天是1 填充剩余天数
        int iDay = 0;
        int iFirstDayOfWeek = Calendar.MONDAY;
        int iStartDay = iFirstDayOfWeek;
        if (iStartDay == Calendar.MONDAY) {
            iDay = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
            if (iDay < 0)
                iDay = 6;
        }
        if (iStartDay == Calendar.SUNDAY) {
            iDay = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            if (iDay < 0)
                iDay = 6;
        }
        calendar.add(Calendar.DAY_OF_WEEK, -iDay);
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位
    }

    public static long lastClickTime;

    /**
     * Description:间隔 800毫秒
     *
     * @param @return
     * @author 李满 2015年1月29日
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 500) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

    /**
     * description:简单判断手机号码
     *
     * @param mobile
     * @return
     * @author STORM
     * @date 2015-5-28
     */
    public static boolean checkMoile(String mobile) {
        if (!isEmpty(mobile)) {
            if (mobile.length() != 11) {
                return false;
            } else {
                if (!mobile.startsWith("1")) {
                    return false;
                }
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void playMobile(Context context, String mobile) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + mobile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * @Directions:获取设备型号
     * @author: liman
     * @date: 2015年8月14日
     * @tag:@return
     */
    public static String getDeviceModel() {
        Build bd = new Build();
        String model = bd.MODEL;
        return model;
    }

    /**
     * @author: liman
     * @date: 2015年8月14日
     * @tag:@return
     */
    public static PackageInfo getAppInfo() {
        Context context = CMBBApplication.getInstance();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pkgInfo;
    }

    public static int getVersionCode(Context context) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pkgInfo.versionCode;
    }

    /**
     * dipתΪ px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px תΪ dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @Directions:判断键盘是否显示了
     * @author: liman
     * @date: 2015年8月26日
     * @tag:@param context
     * @tag:@return
     */
    public static boolean isSoftKeyVisible(Activity context) {
        if (context.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 隐藏软键盘
            // getWindow().setSoftInputMode(
            // WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            return true;
        }
        return false;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    /**
     * 设置状态栏背景状态
     */
    public static void setStatuBarTranslate(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = context.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);// 状态栏无背景
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 设置自定义通知栏的高度
     *
     * @Directions:
     * @author: liman
     * @date: 2015年8月24日
     * @tag:@param view
     */
    public static void setCustomStatuBarHeight(Context context, View view) {
        if (view != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(context)));
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public static String getHideString(String src, int startLen, int endLen) {
        if (!Tools.isEmpty(src) && src.length() > startLen + endLen) {
            String startStr = src.substring(0, startLen);
            String endStr = src.substring(src.length() - endLen);
            return startStr + "****" + endStr;
        }
        return src;
    }

    /**
     * 格式化钱
     *
     * @param money
     * @return
     */
    public static String formatMoney(long money) {
        DecimalFormat nf = new DecimalFormat("#,###.##");
        String value = nf.format(money);
        if (value.endsWith(".00")) {
            value = value.substring(0, value.length() - 3);
        }
        return value;
    }

    public static String formatMoney(double money) {
        DecimalFormat nf = new DecimalFormat("#,###.##");
        String value = nf.format(money);
        if (value.endsWith(".00")) {
            value = value.substring(0, value.length() - 3);
        }
        return value;
    }

    /**
     * 格式化百分比
     *
     * @param percent
     * @return
     */
    public static String formatPercent(double percent) {
        NumberFormat nf = new DecimalFormat("##.##");
        String value = nf.format(percent);
        if (value.endsWith(".00")) {
            value = value.substring(0, value.length() - 3);
        }
        return value;
    }

    public static String getIncome(String money, double rate, String days) {
//        预期收益=输入金额*预期年化率/365*期限天
        Logger.i("数据:", "金额:" + money + "  年化:" + rate + " 天数:" + days);
        BigDecimal bigRate = new BigDecimal(rate);
        BigDecimal bigMoney = new BigDecimal(money);
        BigDecimal bigDays = new BigDecimal(days);
        BigDecimal bigYear = new BigDecimal(365);
        String value = String.valueOf(bigMoney.multiply(bigRate).divide(bigYear, 10, BigDecimal.ROUND_DOWN).multiply(bigDays).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
        return value;
    }


    /**
     * 校验银行卡卡号
     *
     * @return
     */
    public static boolean checkBankCard(String nonCheckCodeCardId) {
//        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length()));
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.replaceAll(" ","").matches("^(\\d{16}|\\d{19})$")) {
            return false;
        }
        return true;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("^(\\d{16}|\\d{19})$")) {
            throw new IllegalArgumentException("Bank card code must be number!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumbers(String str) {
        boolean result = str.matches("[0-9]+");
        if (result == true) {
            return true;
        } else {
            return false;
        }
    }
}
