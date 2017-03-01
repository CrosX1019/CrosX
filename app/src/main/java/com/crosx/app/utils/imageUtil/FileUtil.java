package com.crosx.app.utils.imageUtil;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件工具类
 */

public class FileUtil {


    public final static String USER_HAED = "head";

    private static String ROOT_CACHE;
    public static String ROOT_DIR = "Blin";
    private static FileUtil instance = null;

    private FileUtil() {
    }

    public static FileUtil getInstance(Context context) {
        if (instance == null) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                ROOT_CACHE = (Environment.getExternalStorageDirectory() + "/"
                        + ROOT_DIR + "/");
            } else {
                ROOT_CACHE = (context.getFilesDir().getAbsolutePath() + "/" + ROOT_DIR + "/");
            }
            File dir = new File(ROOT_CACHE);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            instance = new FileUtil();
        }
        return instance;
    }

    public File makeDir(String dir) {
        File fileDir = new File(ROOT_CACHE + dir);
        if (fileDir.exists()) {
            return fileDir;
        } else {
            fileDir.mkdirs();
            return fileDir;
        }
    }


}
