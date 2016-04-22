package com.tingwa.utils;

import android.content.Context;

/**
 * Created by wuchunhui on 16-4-22.
 */
public class MusicUtils {
    /**
     * 缓存mp3文件到cache里面
     * @param musicCacheUtils
     * @param context
     * @param cachePath
     * @param fileName
     */
    public static void saveMusicToCache(MusicCacheUtils musicCacheUtils, Context context, String cachePath,String fileName) {
        if (musicCacheUtils != null)
            musicCacheUtils.putMusic(cachePath,fileName);
    }
}
