package com.tingwa.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 音乐缓存帮助类
 * 磁盘缓存DiskLruCache
 * 网页计算MD5缓存，不用每次都解析
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

    public static void createCacheDir(Context context) {
        File destDir = new File(context.getExternalCacheDir() + "/mp3");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 从缓存（内存缓存，磁盘缓存）中获取歌曲
     */
    public boolean isMusicExist(String url) {
        String key = MD5Utils.md5(url);
//        Log.e("wch diskcache = ","m_isPicExisturl: "+url);
//        String key = FileUtils.getFileNameWithoutExtension(url);
        LogUtil.d(" isMusicExist key: " + key);
        try {
            if (mDiskLruCache.get(key) != null) {
                // 从DiskLruCahce取
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                if (snapshot != null) {
                    LogUtil.d(" diskcache = snapshot exist ");
                    return true;
                }
            }
        } catch (IOException e) {
            LogUtil.d("isMusicExist key: " + e.toString());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 存入缓存（内存缓存，磁盘缓存）
     */
    public void putMusic(String url, Context context) {
        // 判断是否存在DiskLruCache缓存，若没有存入
        String key = MD5Utils.md5(url);
        String fileUrl = context.getExternalCacheDir() + "/mp3/";
        try {
            if (mDiskLruCache.get(key) == null) {
                downFile(url, fileUrl, key);
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (outputStream != null) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();  //将缓存记录同步到journal文件中。
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 该函数返回整形    -1：代表下载文件错误0 ：下载文件成功1：文件已经存在
     *
     * @param urlstr
     * @param path
     * @param fileName
     * @return
     */
    public int downFile(String urlstr, String path, String fileName) {
        InputStream inputStream = null;

        if (FileUtils.checkLocalExist(path + fileName)) {
            return 1;
        } else {
            inputStream = getInputStreamFormUrl(urlstr);
            File resultFile = FileUtils.writeToSDfromInput(path, fileName, inputStream);
            if (resultFile == null) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlstr
     * @return
     */
    public InputStream getInputStreamFormUrl(String urlstr) {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlstr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            inputStream = urlConn.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 获取cache里面的文件
     *
     * @param context
     * @param url
     * @return
     */
    public static String getDiskCacheFilePath(Context context, String url) {
        File file = getDiskCacheDir(context);
        String key = MD5Utils.md5(url);
        File cacheFile = new File(file.getPath() + File.separator + key);
        Log.i("wch cache music = ", cacheFile.getPath());
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

    /**
     * @param url     下载连接
     * @param saveDir 储存下载文件的SDCard目录
     */
    public void download(final String url, final String saveDir, final String fileName, final OnDownloadListener listener) {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                if (listener != null) {
                    listener.onDownloadFailed();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(saveDir, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        if (listener != null) {
                            listener.onDownloading(progress);
                        }
                    }
                    fos.flush();

                    LogUtil.d(" OkHttpDown succee url = "+url +" saveDir = "+saveDir + "fileName = "+fileName);
                    // 下载完成
                    if (listener != null) {
                        listener.onDownloadSuccess();
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onDownloadFailed();
                    }
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

}