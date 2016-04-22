package com.tingwa.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;


/**
 * 音乐缓存帮助类
 * 磁盘缓存DiskLruCache
 */
public class MusicCacheUtils {

    //缓存类
    private static DiskLruCache mDiskLruCache;

    //磁盘缓存大小
    private static final int DISKMAXSIZE = 512 * 1024 * 1024;//512M

    private static final String CACHEDIR = "images";

    public MusicCacheUtils(Context context) {
        try {
            // 获取DiskLruCahce对象
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(context), getAppVersion(context), 1, DISKMAXSIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从缓存（内存缓存，磁盘缓存）中获取歌曲
     */
    public boolean isMusicExist(String url) {
        String key = MD5Utils.md5(url);
//        Log.e("wch diskcache = ","m_isPicExisturl: "+url);
//        String key = FileUtils.getFileNameWithoutExtension(url);
        Log.d("wch diskcache = ", "m_isPicExistkey: " + key);
        try {
            if (mDiskLruCache.get(key) != null) {
                // 从DiskLruCahce取
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                if (snapshot != null) {
                    Log.d("wch diskcache = ", "m_exist ");
                    return true;
                }
            }
        } catch (IOException e) {
            Log.d("wch diskcache = ", "m_isPicExistkey: " + e.toString());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 存入缓存（内存缓存，磁盘缓存）
     */
    public void putMusic(String url, String name) {
        // 判断是否存在DiskLruCache缓存，若没有存入
        String key = MD5Utils.md5(url);
        //       String key = FileUtils.getFileNameWithoutExtension(url);
        try {
            if (mDiskLruCache.get(key) == null) {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
//                    OutputStream outputStream = editor.newOutputStream(0);
//                    if (outputStream != null && bitmap != null && bitmap.compress(CompressFormat.JPEG, 100, outputStream)) {
//                        editor.commit();
//                    } else {
//                        editor.abort();
//                    }
                }
                mDiskLruCache.flush();  //将缓存记录同步到journal文件中。
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取cache里面的文件
     * @param context
     * @param url
     * @return
     */
    public static String getDiskCacheFilePath(Context context, String url) {
        File file = getDiskCacheDir(context);
        String key = MD5Utils.md5(url);
        File cacheFile = new File(file.getPath() + File.separator + key + ".mp3");
        return cacheFile.getPath();
    }

    /**
     * 该方法会判断当前sd卡是否存在，然后选择缓存地址
     *
     * @param context
     * @return
     */
    private static File getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File file = new File(cachePath + File.separator + CACHEDIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}