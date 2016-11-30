/*
 * Copyright (C) 2011 iBoxPay.com
 *
 * $Id: Util.java 1653 2014-08-08 05:38:08Z huangpengfei $
 * 
 * Description: mixed Util function
 *
 */

package com.kuafu.wenhao.dialogdemo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Util {

    private static final String CACHDIR = "ImgCach";

    private static final String WHOLESALE_CONV = ".cach";

    private static final int MB = 1024 * 1024;

    private static final int CACHE_SIZE = 10;

    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

    private Util() {
    }

    /*
    * access_token值的正则匹配，用于判断该用户是否是大平台用户
    * */

    //public static synchronized boolean isDPTAccount() {
    //    boolean retVal = false;
    //    String pattern = "^DPT.*$";
    //    retVal = Consts.ACCESS_TOKEN_VALUE.matches(pattern);
    //    return retVal;
    //}



    public static String getAreaJsonToText(Context context) throws JSONException {
        String text = null;
        try {
            // 文件读取
            InputStream is = context.getAssets().open("area.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);
        } catch (IOException e) {
            ;
        }
        JSONArray provinceJa = null;
        // 解析配置文件
        return text;
    }


    private Handler versionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public static int getIntValue(JSONObject obj, String key) throws JSONException {
        if (obj.has(key)) {
            return obj.getInt(key);
        } else {
            // throw new JSONException("JSONKEY:" + key + "no exist");
            return 0;
        }
    }

    /**
     * Gets the state of GPS location.
     *
     * @return true if enabled.
     */
    public static boolean isGpsOpen(Context context) {
        ContentResolver resolver = context.getContentResolver();
        boolean open = Settings.Secure
                .isLocationProviderEnabled(resolver, LocationManager.GPS_PROVIDER);
        return open;
    }

    public static String getStringValue(JSONObject obj, String key) throws JSONException {
        if (obj.has(key)) {
            return obj.getString(key);
        } else {
            return "";
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);
    }

    public static Bitmap rotate(Bitmap original, final int angle) {
        if ((angle % 360) == 0) {
            return original;
        }

        final boolean dimensionsChanged = angle == 90 || angle == 270;
        final int oldWidth = original.getWidth();
        final int oldHeight = original.getHeight();
        final int newWidth = dimensionsChanged ? oldHeight : oldWidth;
        final int newHeight = dimensionsChanged ? oldWidth : oldHeight;

        Bitmap bitmap = Bitmap.createBitmap(newWidth, newHeight, original.getConfig());
        Canvas canvas = new Canvas(bitmap);

        Matrix matrix = new Matrix();
        matrix.preTranslate((newWidth - oldWidth) / 2f, (newHeight - oldHeight) / 2f);
        matrix.postRotate(angle, bitmap.getWidth() / 2f, bitmap.getHeight() / 2);
        canvas.drawBitmap(original, matrix, null);

        original.recycle();

        return bitmap;
    }






    @Deprecated
    // 很耗内存建议放弃
    public static Bitmap getZipBitmap(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280f;// 这里设置高度为800f
        float ww = 720f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static void chmod(String permission, String filePath) {
        try {
            String command = "chmod " + permission + " " + filePath;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            ;
        }
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options,
                    baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(
                baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }



    /**
     * 计算两个日期之间的天数差
     */
    public static int daysBetween(Calendar date1, Calendar date2) {
        long time1 = date1.getTimeInMillis();
        long time2 = date2.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return (int) between_days;
    }

    public static EditText EmptyChecker(EditText... params) {
        for (EditText et : params) {
            if (TextUtils.isEmpty(et.getText().toString().trim())) {
                return et;
            }
        }
        return null;
    }

    static public boolean isNetworkConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            ;
        }
        return false;
    }

    static public String toYuanByFen(String moneyStr) {
        moneyStr = moneyStr.replaceAll(",", "");
        if (checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                double money = new BigDecimal(moneyStr).divide(new BigDecimal("100"))
                        .setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMinimumFractionDigits(2);
                String result = nf.format(money);
                return result;
            } catch (NumberFormatException e) {
                ;
                return ((Integer.parseInt(moneyStr)) / 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    static public String toYuanByFen2(String moneyStr) {
        moneyStr = moneyStr.replaceAll(",", "");
        if (checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                double money = new BigDecimal(moneyStr).divide(new BigDecimal("100"))
                        .setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();

                DecimalFormat df = new DecimalFormat("#0.00");
                String st = df.format(money);
                return st;
            } catch (NumberFormatException e) {
                ;
                return ((Integer.parseInt(moneyStr)) / 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    /**
     * 转换分为元，去掉小数点后的数字
     */
    static public String toYuanByFen3(String moneyStr) {
        if (!Util.checkString(moneyStr)) {
            return "";
        }
        moneyStr = moneyStr.replaceAll(",", "");
        if (checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                long money = (int) new BigDecimal(moneyStr).divide(new BigDecimal("100"))
                        .setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
                NumberFormat nf = NumberFormat.getNumberInstance();
                String result = nf.format(money);
                return result;
            } catch (NumberFormatException e) {
                ;
                return ((Integer.parseInt(moneyStr)) / 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    /**
     * 转换10000元为10，000元
     */
    static public String addCommasToYuan(String moneyStr) {
        if (!Util.checkString(moneyStr)) {
            return "";
        }
        moneyStr = moneyStr.replaceAll(",", "");
        if (checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                long money = Long.valueOf(moneyStr);
                NumberFormat nf = NumberFormat.getNumberInstance();
                String result = nf.format(money);
                return result;
            } catch (NumberFormatException e) {
                ;
                return ((Integer.parseInt(moneyStr)) / 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    static public String toFenByYuan(String moneyStr) {
        moneyStr = moneyStr.replaceAll(",", "");
        if (checkString(moneyStr) && checkMoneyValid(moneyStr)) {
            try {
                return (new BigDecimal(moneyStr).multiply(new BigDecimal("100"))
                        .setScale(0, BigDecimal.ROUND_FLOOR) + "");
            } catch (NumberFormatException e) {
                ;
                return ((Integer.parseInt(moneyStr)) * 100) + "";
            }
        } else {
            return moneyStr;
        }
    }

    static public boolean compareMoneyALessB(String a, String b) {
        if (checkString(a) && checkString(b)) {
            if (-1 == new Double(a).compareTo(new Double(b))) {
                return true;
            }
        }
        return false;
    }

    static public boolean compareMoneyALessEqualB(String a, String b) {
        if (checkString(a) && checkString(b)) {
            if (-1 == new Double(a).compareTo(new Double(b)) || 0 == new Double(a)
                    .compareTo(new Double(b))) {
                return true;
            }
        }
        return false;
    }

    static public boolean compareMoneyALessEquareZero(String a) {
        if (checkString(a)) {
            if (0 >= new Double(a).compareTo(new Double("0"))) {
                return true;
            }
        }
        return false;
    }

    static public boolean checkIdCard(String idCard) {
        try {
            if (!checkString(idCard)) {
                return false;
            }
            if (idCard.length() == 18) {
                Pattern p = Pattern.compile("^\\d{15}$|^\\d{17}(?:\\d|x|X)$");
                // Pattern p =
                // Pattern.compile("^\\d{15}$|^\\d{17}(?:\\d|\\*)$");
                Matcher m = p.matcher(idCard);
                return (m.matches() && checkIdCheckbit(idCard));
            } else {
                return false;
            }
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    private static boolean checkIdCheckbit(String id) {
        // 17位加权因子，与身份证号前17位依次相乘。
        int w[] = {
                7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2
        };
        int sum = 0;// 保存级数和
        for (int i = 0; i < id.length() - 1; i++) {
            sum += new Integer(id.substring(i, i + 1)) * w[i];
        }
        /**
         * 校验结果，上一步计算得出的结果与11取模，得到的结果相对应的字符就是身份证最后一位，也就是校验位。例如：0对应下面数组第一个元素，以此类推
         * 。
         */
        String sums[] = {
                "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"
        };
        if (sums[(sum % 11)]
                .equalsIgnoreCase(id.substring(id.length() - 1, id.length()))) {// 与身份证最后一位比较
            return true;
        } else {
            return false;
        }
    }

    static public boolean checkEmail(String email) {
        try {
            if (email.length() < 1 || email.length() > 30) {
                return false;
            } else {
                Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
                Matcher m = p.matcher(email);
                return (m.matches());
            }
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    static public boolean checkMobile(String mobile) {
        try {
            if (mobile.length() != 11) {
                return false;
            } else {
                Pattern p = Pattern.compile("^[1][0-9]{10}$");
                Matcher m = p.matcher(mobile);
                return (m.matches());
            }
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    static public boolean checkBankCard(String num) {
        boolean result = false;
        try {
            if (checkString(num) && /*!isZeroOnTopOfSrting(num)*/ !num.startsWith(".")) {
                if (num.length() >= 9 && num.length() <= 30 /*&& !isZeroOnTopOfSrting(num) */ && num
                        .matches("[0-9]+")) {
                    result = true;
                }
            }
        } catch (Exception e) {
            ;
        }
        return result;
    }



    static public boolean checkCreditNum(String num) {
        boolean result = false;
        try {
            if (checkString(num) && !isZeroOnTopOfSrting(num)) {
                if (num.length() >= 15 && num.length() <= 19 && !isZeroOnTopOfSrting(num)) {
                    result = true;
                }
            }
        } catch (Exception e) {
            ;
        }
        return result;
    }

    static public boolean checkConfirmCreditNum(String num, String cnum) {
        try {
            if (!checkCreditNum(cnum)) {
                return false;
            }
            if (!num.equals(cnum)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static public boolean checkMoneyValid(String money) {
        if (checkString(money)) {
            Pattern p = Pattern.compile("^((\\d+)|0|)(\\.(\\d+)$)?");
            Matcher m = p.matcher(money);
            return (m.matches());
        }
        return false;
    }



    static public boolean checkPWD(String pwd) {
        try {
            if (pwd.length() < 6 || pwd.length() > 20) {
                return false;
            }
            Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
            Matcher m = p.matcher(pwd);
            return (m.matches());
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    static public boolean checkPWD(int minLength, int maxLength, String pwd) {
        try {
            if (pwd.length() < minLength || pwd.length() > maxLength) {
                return false;
            }
            Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
            Matcher m = p.matcher(pwd);
            return (m.matches());
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    static public boolean checkConfirmPWD(String pwd, String cpwd) {
        try {
            if (cpwd.length() < 6 || cpwd.length() > 20) {
                return false;
            }
            if (!pwd.equals(cpwd)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static public boolean checkVerifyCode(String code) {
        try {
            if (code.length() != 4) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static public boolean checkRegisterVerifyCode(String code) {
        try {
            if (code.length() != 6) {
                return false;
            } else if (!TextUtils.isDigitsOnly(code)) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }

    static public boolean isZeroOnTopOfSrting(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.substring(0, 1).equals("0") || str.substring(0, 1).equals(".")) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    public static int convertDIP2PX(Context context, int dip) {
        try {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
        } catch (Exception e) {
            ;
            return dip;
        }
    }

    public static int convertPX2DIP(Context context, int px) {
        try {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
        } catch (Exception e) {
            ;
            return px;
        }
    }

    public static String formatTimeToday(String timeStr) {
        try {
            String result = timeStr;
            if (!TextUtils.isEmpty(timeStr) && timeStr.length() == 14) {
                try {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date time = (Date) sdf1.parse(timeStr);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    result = sdf2.format(time);
                } catch (Exception e) {
                    ;
                }
            }
            return result;
        } catch (Exception e) {
            ;
            return timeStr;
        }
    }

    public static String formatTime(String timeStr) {
        try {
            String result = timeStr;
            if (!TextUtils.isEmpty(timeStr) && timeStr.length() == 14) {
                try {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date time = (Date) sdf1.parse(timeStr);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    result = sdf2.format(time);
                } catch (Exception e) {
                    ;
                }
            }
            return result;
        } catch (Exception e) {
            ;
            return timeStr;
        }
    }

    /**
     * date "yyyy-MM-dd HH:mm:ss"
     */
    public static Date getDateFromString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // String time1="2010-12-12 17:05:23";
        ParsePosition po = new ParsePosition(0);
        Date d = sdf.parse(time, po);
        return d;

    }

    public static String getDateTime(String timeFormat) {
        String result = "NaN";
        if (!checkString(timeFormat)) {
            timeFormat = "yyyy-MM-dd";
        }
        try {
            Date curDate = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
            result = sdf.format(curDate);
        } catch (Exception e) {
            ;
        }
        return result;
    }

    public static String creditSeparator(String value) {
        try {
            if (TextUtils.isEmpty(value)) {
                return value;
            }
            String clearValue = ClearSeparator(value);
            StringBuffer result = new StringBuffer(clearValue);
            for (int i = 1; i <= (clearValue.length() - 1) / 4; i++) {
                result.insert(i * 4 + i - 1, "-");
            }
            return result.toString();
        } catch (Exception e) {
            ;
            return value;
        }
    }

    public static String mobileSeparator(String mobile) {
        mobile = ClearSeparator(mobile);
        try {
            if (TextUtils.isEmpty(mobile)) {
                return mobile;
            }
            if (mobile.length() == 11) {
                mobile = ClearSeparator(mobile);
                mobile = String.format("%s %s %s", mobile.substring(0, 3), mobile.substring(3, 7),
                        mobile.substring(7));
            }
            return mobile;
        } catch (Exception e) {
            ;
            return mobile;
        }
    }

    public static String ClearSeparator(String value) {
        try {
            if (TextUtils.isEmpty(value)) {
                return value;
            }
            value = value.replace("-", "");
            return value;
        } catch (Exception e) {
            ;
            return value;
        }
    }

    public static String replaceUserNameWithStar(String userName) {
        try {
            if (!checkString(userName) || userName.length() < 2) {
                return userName;
            }
            String star = "";
            int userNameLen = (userName.length() % 2 == 0) ? (userName.length())
                    : (userName.length() - 1);
            int starLen = userNameLen / 2;
            for (int i = 0; i < starLen; ++i) {
                star += "*";
            }
            return (star + userName.substring(starLen));
        } catch (Exception e) {
            ;
            return userName;
        }
    }

    public static short[] stringToHex(String randNo) {
        char[] cc = new char[randNo.length()];
        short[] mc = new short[randNo.length()];
        randNo.getChars(0, randNo.length(), cc, 0);

        for (int i = 0; i < cc.length; ++i) {
            mc[i] = (short) cc[i];
        }
        return mc;
    }

    static public String getFileMD5(String file) {
        File f = new File(file);

        try {
            if (f.exists()) {
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                FileInputStream stream = new FileInputStream(f);
                byte[] bytes = new byte[1024];
                int c;
                while ((c = stream.read(bytes)) != -1) {
                    digest.update(bytes, 0, c);
                }
                stream.close();
                String hash = new BigInteger(1, digest.digest()).toString(16);

                while (hash.length() < 32) {
                    hash = "0" + hash;
                }

                return hash;
            }
        } catch (NoSuchAlgorithmException e) {
            ;
        } catch (IOException e) {
            ;
        }
        return null;
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            ;
        }
    }

    public static String getCallbackUrl(String callbackHost, String paramString) {
        if (!checkString(callbackHost)) {
            return "";
        }
        if (!checkString(paramString)) {
            return callbackHost;
        }
        try {
            StringBuilder callbackUrl = new StringBuilder();
            callbackUrl.append(callbackHost);
            callbackUrl.append("?");
            callbackUrl.append(paramString);
            return callbackUrl.toString();
        } catch (Exception e) {
            ;
        }
        return "";
    }



    public static String getParamString(TreeMap<String, String> treeMap) {
        if (treeMap == null || treeMap.isEmpty()) {
            return "";
        }
        try {
            StringBuilder paramString = new StringBuilder();
            for (Map.Entry<String, String> map : treeMap.entrySet()) {
                paramString.append(map.getKey());
                paramString.append("=");
                paramString.append(map.getValue());
                paramString.append("&");
            }
            paramString.delete(paramString.length() - 1, paramString.length());
            return paramString.toString();
        } catch (Exception e) {
            ;
        }
        return "";
    }

    public static boolean checkString(String str) {
        return (!TextUtils.isEmpty(str) && !str.equalsIgnoreCase("null"));
    }

    public static String replaceStringWrap(String content) {
        return (checkString(content) && content.contains("\\n")) ? (content
                .replaceAll(Pattern.quote("\\n"), "\n")) : content;
    }

    private static byte[] getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            if (packageName.equals("com.iboxpay.iboxpay")) {
                return info.signatures[0].toByteArray();
            }
        }
        return null;
    }

   

    public static String getRandom(int len) {
        try {
            char[] str = {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
            };
            StringBuilder code = new StringBuilder("");
            Random random = new Random();
            int count = 0;
            while (count < len) {
                int i = Math.abs(random.nextInt(40));
                if (i >= 0 && i < str.length) {
                    code.append(str[i]);
                    count++;
                }
            }
            return code.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getNumRandom(int len) {
        try {
            char[] str = {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
            };
            StringBuilder code = new StringBuilder("");
            Random random = new Random();
            int count = 0;
            while (count < len) {
                int i = Math.abs(random.nextInt(40));
                if (i >= 0 && i < str.length) {
                    code.append(str[i]);
                    count++;
                }
            }
            return code.toString();
        } catch (Exception e) {
     
            return "";
        }
    }

    public static String getParterRequesetId() {
        String req_id_default = "1000000001";
        String req_id = "";
        try {
            req_id = (System.currentTimeMillis() + "").substring(5) + getRandom(2);
        } catch (Exception e) {
            req_id = getRandom(10);
          
        }
        if (req_id.length() != 10) {
            req_id = req_id_default;
        }
        return req_id;
    }

    public static String getTradeRandom() {
        String req_id_default = "3456789012345678901234567890";
        String req_id = "";
        try {
            req_id = System.currentTimeMillis() + getNumRandom(5);
        } catch (Exception e) {
            req_id = getNumRandom(18);
            ;
        }
        if (req_id.length() < 18) {
            req_id = req_id_default;
        }
        return req_id;
    }

    public static boolean checkIsCN(String s) {
        if (checkString(s)) {
            try {
                byte[] b = s.getBytes("UTF-8");
                if (s.length() != b.length) {
                    return true;
                }
            } catch (UnsupportedEncodingException e) {
                ;
            }
        }
        return false;
    }

    public static String replaceAllBlank(String s) {
        if (checkString(s)) {
            s = s.replaceAll(" ", "");
        }
        return s;
    }

    public static String convertCNum2Asterisk(String cNum) {
        String num = replaceAllBlank(cNum);
        if (checkCreditNum(num)) {
            String firstSix = num.substring(0, 6);
            String lastFour = num.substring(num.length() - 4, num.length());
            return firstSix + "******" + lastFour;
        }
        return num;
    }



    public static String get2DigNum(int num) {
        String sNum = "";
        if (num < 10) {
            sNum = "0" + num;
        } else {
            sNum = num + "";
        }

        return sNum;
    }

    public static boolean checkChineseName(String str) {
        if (str.length() < 2) {
            return false;
        } else {
            return checkChinese(str);
        }
    }

    public static boolean checkName(String str) {
        if (str == null) {
            return false;
        }
        if (str.length() < 2) {
            return false;
        } else {
            return checkChineseXinJiang(str);
        }
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检验中文字符串
     */
    public static boolean checkChinese(String ChineseStr) {
        Pattern p = Pattern.compile("^[\u4e00-\u9fa5]{0,}$");
        Matcher m = p.matcher(ChineseStr);
        return (m.matches());
    }

    /**
     * 增加对新疆姓名中.的检测
     */
    public static boolean checkChineseXinJiang(String ChineseStr) {
//        Pattern p = Pattern.compile("^[\u4e00-\u9fa5.\u3002]{0,}$");
        Pattern p = Pattern.compile("^[\u4e00-\u9fa5.\u3002\u00b7]{0,}$");
        Matcher m = p.matcher(ChineseStr);
        return (m.matches());
    }

    /**
     * 以英文字母开头 4-18位
     */
    public static boolean checkAccount(String account) {
        Pattern p = Pattern.compile("^[a-zA-Z][0-9a-zA-Z_]{3,17}$");
        Matcher m = p.matcher(account);
        return (m.matches());
    }

    /**
     * 检测营业执照号是否合法
     */
    public static boolean checkBusinessLicenseCode(String businessLicenseCode) {
        if (!checkString(businessLicenseCode)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9A-Za-z-]{18}|[0-9A-Za-z-]{15}|[0-9A-Za-z-]{13}");
        Matcher matcher = pattern.matcher(businessLicenseCode);
        return matcher.matches();
    }

    /**
     * 检测组织结构代码证号码
     */
    public static boolean checkOrganizationStructCode(String str) {
        return checkString(str) && checkOrganizationStructCodeFormat(str);
    }

    /**
     * 检测组织结构代码证号码格式
     */
    public static boolean checkOrganizationStructCodeFormat(String str) {
        Pattern p = Pattern.compile("[0-9A-Za-z]{8}[-]?[0-9A-Za-z]");
        Matcher m = p.matcher(str);
        return (m.matches());
    }

    /**
     * 检测税务登记号码
     */
    public static boolean checkTaxRegistrationNum(String str) {
        if (!checkString(str)) {
            return false;
        }
//        if (str.length() != 15) {
//            return false;
//        } else {
        return checkTaxRegisterationNumFormat(str);
//        }
    }

    /**
     * 检测税务登记号码格式
     */
    public static boolean checkTaxRegisterationNumFormat(String str) {
        Pattern p1 = Pattern.compile("[0-9A-Za-z]+-[0-9A-Za-z]+");
        Matcher m1 = p1.matcher(str);

        Pattern p2 = Pattern
                .compile("[0-9A-Za-z]{20}|[0-9A-Za-z]{18}|[0-9A-Za-z]{17}|[0-9A-Za-z]{15}");
        Matcher m2 = p2.matcher(str);

        return ((m1.matches() && str.length() == 21) || m2.matches());
    }

    // 获取AppKey
   /* public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }*/

    /**
     * 判断sdCard是否可以
     */
    public static boolean checkSdCardEnable() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * @param context used to check the device version and DownloadManager information
     * @return true if the download manager is available
     * @see
     */
    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui",
                    "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager()
                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isZhiXiaCity(String provinceCityCountyCode) {
        String provinceCode = provinceCityCountyCode.substring(0, 2);
        if (provinceCode.equals("11") || provinceCode.equals("12") || provinceCode.equals("31")
                || provinceCode.equals("50")) {
            return true;
        } else {
            return false;
        }
    }

    public static File saveBitmapToFile(Context context, Bitmap bm, String filename)
            throws IOException {
        // create a file to write bitmap data
        File f = new File(context.getCacheDir(), filename);
        f.createNewFile();

        // Convert bitmap to byte array
        Bitmap bitmap = bm;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, bos);
        byte[] bitmapdata = bos.toByteArray();

        // write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        return f;
    }

    public static String toHex(Integer i) {
        return Integer.toHexString((i & 0x000000ff) | 0xffffff00).substring(6).toUpperCase();
    }

    public static String clearSpace(String s) {
        if (s == null) {
            return null;
        }
        return s.replace(" ", "");
    }

    private static long[] mHits = new long[3];

//    public static void showIPDialog(Activity context, int keyCode) {
//        if (keyCode == KeyEvent.KEYCODE_MENU && Consts.DEVELOP) {
//            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
//            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
//            if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
//                final Dialog d = new Dialog(context);
//                d.setContentView(R.layout.dialog_edittext);
//                Button okbtn = (Button) d.findViewById(R.id.button);
//                final EditText et = (EditText) d.findViewById(R.id.edittext);
//                et.setText(BuildConfig.MARKETBOX_SERVER_URL_SSL);
//                okbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Consts.MARKETBOX_SERVER_URL_SSL = et.getText().toString();
//                        d.dismiss();
//                    }
//                });
//                d.show();
//            }
//        }
//    }
//
//    public static boolean isBusinessLicense(String s) {
//        if ((s.length() == 13 || s.length() == 15) && TextUtils.isDigitsOnly(s)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public static String replaceUserPhoneNumberCenter(String mobile) {
//        if (TextUtils.isEmpty(mobile)) {
//            return mobile;
//        }
//        if (mobile.length() == 11) {
//            char[] m = mobileSeparator(mobile).toCharArray();
//            String s = "";
//            for (int i = 0; i < m.length; i++) {
//                if (i > 3 && i < 8) {
//                    s += "*";
//                } else {
//                    s += m[i];
//                }
//            }
//
//            return s;
//        }
//
//        return mobile;
//    }

    public static String getMaskIdCard4(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return idCard;
        }

        if (idCard.length() == 15) {
            char[] m = idCard.toCharArray();
            String s = "";
            for (int i = 0; i < m.length; i++) {
                if (i < 11) {
                    s += "*";
                } else {
                    s += m[i];
                }
            }
            return s;
        }

        if (idCard.length() == 18) {
            char[] m = idCard.toCharArray();
            String s = "";
            for (int i = 0; i < m.length; i++) {
                if (i < 14) {
                    s += "*";
                } else {
                    s += m[i];
                }
            }
            return s;
        }
        return idCard;
    }

    public static String getMask4IdCard(String idCard) {
        if (TextUtils.isEmpty(idCard) || idCard.length() <= 9) {
            return idCard;
        } else if (idCard.length() <= 12) {
            return idCard.substring(0, 4) + " **** " + idCard.substring(idCard.length() - 4);
        }
        return idCard.substring(0, 4) + " " + idCard.substring(4, 8) + " **** " + idCard
                .substring(idCard.length() - 4);
    }

    public static String getMask4IdCardV2(String idCard) {
        if (TextUtils.isEmpty(idCard) || idCard.length() < 9) {
            return idCard;
        } else if (idCard.length() == 9) {
            return idCard.substring(0, 5) + " **** ";
        } else if (idCard.length() <= 12) {
            return idCard.substring(0, 4) + " **** " + idCard.substring(idCard.length() - 4);
        }
        return idCard.substring(0, 4) + " " + idCard.substring(4, 6) + " **** " + idCard
                .substring(idCard.length() - 4);
    }

    // 前6后四 1
    public static String getMask4IdCardV3(String idCard) {
        if (TextUtils.isEmpty(idCard) || idCard.length() < 9) {
            return idCard;
        } else if (idCard.length() <= 12) {
            char[] chars = idCard.toCharArray();
            StringBuffer idCardBf = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                if (i % 4 == 0 && i != 0) {
                    idCardBf.append(' ');
                }
                if (chars.length - i <= 4) {
                    idCardBf.append(chars[i]);
                } else {
                    idCardBf.append("*");
                }
            }
            return idCardBf.toString();
        } else {
            char[] chars = idCard.toCharArray();
            StringBuffer idCardBf = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                if (i % 4 == 0 && i != 0) {
                    idCardBf.append(' ');
                }
                if (chars.length - i <= 4 || i < 6) {
                    idCardBf.append(chars[i]);
                } else {
                    idCardBf.append("*");
                }
            }
            return idCardBf.toString();
        }

    }

    public static String getMaskMobile3_4(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return mobile;
        }
        if (mobile.length() == 11) {
            char[] m = mobile.toCharArray();
            String s = "";
            for (int i = 0; i < m.length; i++) {
                if (i > 2 && i < 7) {
                    s += "*";
                } else {
                    s += m[i];
                }
            }
            return s;
        }
        return mobile;
    }

    // dip转像素
    public static int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);
        return valuePixels;

    }

    // 像素转dip
    public static float PixelsToDip(Context context, int Pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float dips = Pixels / SCALE;
        return dips;
    }



    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * （DisplayMetrics类中属性scaledDensity）
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * （DisplayMetrics类中属性scaledDensity）
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static void showSoftKeyBoardImmediately(final View view) {
        if (view == null) {
            return;
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Dimiss Keyboard
     */
    public static boolean hideSoftKeyBoard(Activity context) {
        View view = context.getWindow().getCurrentFocus();
        return hideSoftKeyBoard(view);
    }

    public static boolean hideSoftKeyBoard(View view) {
        if (view == null) {
            return false;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        return inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static String appZero(int num) {

        if (num == -1) {
            return "00";
        }

        if (num < 10) {
            return "0" + num;
        }

        return num + "";
    }


    public static Bitmap saveWhiteBackgroundBitmap(Bitmap image) {
        Bitmap newBitmap = Bitmap
                .createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(image, 0, 0, null);
        return newBitmap;
    }

    public static void deleteApk(String file) {
        try {
            File f = new File(file);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception ex) {

        }
    }

    public static Bitmap addTextToBitmap(Bitmap bitmap, int colorId, int textSize, String text) {

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorId);
        paint.setTextSize(textSize);
        paint.setTextAlign(Align.CENTER);
        canvas.drawText(text, bitmap.getWidth() / 2, bitmap.getHeight() / 2, paint);
        return bitmap;
    }

    public static Bitmap addTextToBitmap(Bitmap bitmap, int colorId, int textSize, String text,
            int yDelt) {

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        // paint.setColor(Color.rgb(61, 61, 61));
        paint.setColor(colorId);
        // text size in pixels
        paint.setTextSize(textSize);
        // text shadow
        // paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        // Rect bounds = new Rect();
        paint.setTextAlign(Align.CENTER);

        // paint.getTextBounds(gText, 0, gText.length(), bounds);
        // int x = (bitmap.getWidth() - bounds.width())/2;
        // int y = (bitmap.getHeight() + bounds.height())/2;
        //
        // canvas.drawText(gText, x * scale, y * scale, paint);
        canvas.drawText(text, bitmap.getWidth() / 2, bitmap.getHeight() / 2 + yDelt, paint);
        return bitmap;
    }


    /**
     * 获取顶端statusBar高度
     */
    public static int getStatusBarHeight(Activity context) {
        // get the status bar height
        // Rect rectangle = new Rect();
        // Window window = context.getWindow();
        // window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        // return rectangle.top;

        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isLetterOrDigit(char c) {
        int codePoint = (int) c;
        // Optimized case for ASCII
        if (('A' <= codePoint && codePoint <= 'Z') || ('a' <= codePoint && codePoint <= 'z')) {
            return true;
        }
        if ('0' <= codePoint && codePoint <= '9') {
            return true;
        }
        return false;
    }

    public static Point getScreenDimension(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT > 12) {
            display.getSize(size);
        } else {
            size.x = display.getWidth();
            size.y = display.getHeight();  // Deprecated
        }
        return size;
    }

    /** 将图片存入文件缓存 * */
    public static void saveBitmap(Bitmap bm, String url) {
        if (bm == null) {
            return;
        }
        //判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            //SD空间不足
            return;
        }
        String filename = convertUrlToFileName(url);
        String dir = getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dir + "/" + filename);
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {
            ;
        } catch (IOException e) {
            ;
        }
    }

    /** 计算sdcard上的剩余空间 * */
    public static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /** 将url转成文件名 * */
    public static String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1];
    }

    /** 获得缓存目录 * */
    public static String getDirectory() {
        String dir = getSDPath() + "/" + CACHDIR;
        return dir;
    }

    /** 取SD卡路径 * */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }

    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifSort implements Comparator<File> {

        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /** 从缓存中获取图片 * */
    public static Bitmap getImage(final String url) {
        String filename = Util.convertUrlToFileName(url);
        final String path = getDirectory() + "/" + filename;
        File file = new File(path);
        if (file.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            if (bmp == null) {
                file.delete();
            } else {
//                updateFileTime(path);
                return bmp;
            }
        }
        return null;
    }

    /**
     * 千分位实现
     */
    public static String getNumKb(String s) throws ParseException {
        if (!checkString(s)) {
            return "";
        }
        NumberFormat formatter = new DecimalFormat("###,###,###,###,###");
        Long num = (long) Double.parseDouble(s);
        System.out.println("num=" + num);
        return formatter.format(num);
    }

    public static String fmtMicrometer(String s) throws ParseException {
        if (!checkString(s)) {
            return "";
        }
        NumberFormat formatter = null;

        if (s.indexOf(".") > 0) {
            formatter = new DecimalFormat("###,###,###,###,##0.00");
        } else {
            formatter = new DecimalFormat("###,###,###,###,##0");
        }
        double num = 0.00;
        try {
            num = Double.parseDouble(s);
        } catch (Exception e) {
            num = 0.00;
        }
        return formatter.format(num);
    }

    //20150429155731  要把这样的数据进行转化
    public static String stringConvertTodayTomorrow(String time) {
        if (!checkString(time)) {
            return "";
        }
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        String myTime = null;
        String hour = null;
        String minute = null;
        //20150429155731  2008/10/13 15:20:25
        if (time.length() == 14) {
            String year = time.substring(0, 4);
            String month = time.substring(4, 6);
            String day = time.substring(6, 8);
            hour = time.substring(8, 10);
            minute = time.substring(10, 12);
            String second = time.substring(12, 14);
            myTime = year + "-" + month + "-" + day + " " + hour + ":" + minute;
        }

        Date date = null;
        try {
            date = format.parse(myTime);
        } catch (ParseException e) {
            ;
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            return "今天" + "   " + hour + ":" + minute;
        } else if (current.before(today) && current.after(yesterday)) {
            return "昨天" + "   " + hour + ":" + minute;
        }
        return date.getMonth() + "-" + date.getDay() + "   " + hour + ":" + minute;
    }

    /**
     * 比较两个字符串，此处null和""等价
     */
    public static boolean compareStr(String str1, String str2) {
        if (TextUtils.isEmpty(str1)) {
            return TextUtils.isEmpty(str2);
        }
        return str1.equals(str2);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(@NonNull Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取连接网络类型名称
     *
     * @return typeName 网络类型名称
     */
    public static String getNetworkTypeName(@NonNull Context context) {
        String typeName = "";
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                typeName = info.getTypeName();
            }
        }
        return typeName;
    }


}
