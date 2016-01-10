package com.cmbb.app.action.common;

import java.security.MessageDigest;

/**
 * Created by Storm on 2015/11/16.
 * MD5加密算法
 */
public class MD5 {
//    public static String getMD5Str(String str) {
//        MessageDigest messageDigest = null;
//        try {
//            messageDigest = MessageDigest.getInstance("MD5");
//            messageDigest.reset();
//            messageDigest.update(str.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException e) {
//            System.exit(-1);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        byte[] byteArray = messageDigest.digest();
//        StringBuffer md5StrBuff = new StringBuffer();
//        for (int i = 0; i < byteArray.length; i++) {
//            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
//                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
//            else
//                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
//        }
//        return md5StrBuff.toString();
//    }


//    public static String getMD5Str(String string) {
//        byte[] hash;
//        try {
//            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Huh, MD5 should be supported?", e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
//        }
//        return getString(hash);
//    }
//
//    private static String getString(byte[] b) {
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < b.length; i++) {
//            sb.append(b[i]);
//        }
//        return sb.toString();
//    }

//    public static String getMD5String(String s) {
//        char hexDigits[] = {'0', '1', '2', '3', '4',
//                '5', '6', '7', '8', '9',
//                'A', 'B', 'C', 'D', 'E', 'F'};
//        try {
//            byte[] btInput = s.getBytes();
//            //获得MD5摘要算法的 MessageDigest 对象
//            MessageDigest mdInst = MessageDigest.getInstance("MD5");
//            //使用指定的字节更新摘要
//            mdInst.update(btInput);
//            //获得密文
//            byte[] md = mdInst.digest();
//            //把密文转换成十六进制的字符串形式
//            int j = md.length;
//            char str[] = new char[j * 2];
//            int k = 0;
//            for (int i = 0; i < j; i++) {
//                byte byte0 = md[i];
//                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//                str[k++] = hexDigits[byte0 & 0xf];
//            }
//            return new String(str);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
