/*
 * Copyright (C) 2011-2013 ShenZhen iBOXPAY Information Technology Co.,Ltd.
 *
 * All right reserved.
 *
 * This software is the confidential and proprietary
 * information of iBoxPay Company of China.
 * ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only
 * in accordance with the terms of the contract agreement
 * you entered into with iBoxpay inc.
 *
 * $Id: FileUtil.java 1653 2014-08-08 05:38:08Z huangpengfei $
 */

package com.kuafu.wenhao.dialogdemo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import static com.kuafu.wenhao.dialogdemo.Util.freeSpaceOnSd;

public class FileUtil {

    private static final String BASE_PATH = "com.iboxpay.platform/platform";

    public static String LOC_HELP_PAY_PATH = "/sdcard/com.iboxpay.platform/userVerfy/";

    public static File makeReturnFile() {
        File file = new File(LOC_HELP_PAY_PATH);//创建路径
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = "photo_" + System.currentTimeMillis() + ".jpg";
        File returnFile = new File(LOC_HELP_PAY_PATH + fileName);
        if (!returnFile.exists()) {
            try {
                returnFile.createNewFile();//创建文件
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return returnFile;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                file.delete();
                return true;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return true;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
            return true;
        }
    }

    /**
     * 根据文件地址删除文件
     */
    public static boolean deleteFile(String fileStr) {
        File f = new File(fileStr);
        return deleteFile(f);
    }


    /**
     * 同上，不同的是，如果该路径有问题，则返回系统默认路径 加上创建的路径名字
     */
    public static File getBaseAndSysPath(Context context, String savepath) throws IOException {
        File basePath = null;
        String sDStateString = android.os.Environment.getExternalStorageState();
        if ((sDStateString.equals(android.os.Environment.MEDIA_MOUNTED) && (
                20 < freeSpaceOnSd()))) {
            basePath = new File(Environment.getExternalStorageDirectory(),
                    BASE_PATH + File.separator + savepath);
            if (!basePath.exists()) {
                if (!basePath.mkdirs()) {
                    throw new IOException(
                            String.format("%s cannot be created!", basePath.toString()));
                }
            }
            if (!basePath.isDirectory()) {
                throw new IOException(String.format("%s is not a directory!", basePath.toString()));
            }
        } else {
            basePath = getFilePath(context, savepath);
            if (!basePath.exists()) {
                if (!basePath.mkdirs()) {
                    throw new IOException(
                            String.format("%s cannot be created!", basePath.toString()));
                }
            }
        }
        return basePath;
    }

    /**
     * 返回系统默认路径: /data/data/com.xxx.xxx/
     */
    public static File getFilePath(Context context) {
        return new File(context.getFilesDir() + File.separator);
    }

    public static File getFilePath(Context context, String filepath) {
        return new File(context.getFilesDir() + File.separator + filepath);
    }

}
