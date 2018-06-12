package com.tingwa.service;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tingwa.constant.StaticContent;
import com.tingwa.notificaton.MusicNotification;
import com.tingwa.utils.JsoupHtmlUtils;
import com.tingwa.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class MusicService extends Service {

    private Context mApplicationContext;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private static volatile MusicService INSTANCE = null;
    private MusicNotification mMusicNotification;
    private ThreadPoolExecutor mThreadPoolExecutor;

    public static void create(Application application) {
        LogUtil.d(" with application");
        if (INSTANCE == null) {
            synchronized (MusicService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Builder().context(application).build();
                }
            }
        }
    }

    public MusicService() {
    }

    public static MusicService getInstance() {
        return INSTANCE;
    }

    private MusicService(Context applicationContext) {
        this.mApplicationContext = applicationContext;
        mMusicNotification = new MusicNotification(applicationContext);
        mMediaPlayer = new MediaPlayer();
        mThreadPoolExecutor = new ThreadPoolExecutor(1,1,3,TimeUnit.SECONDS
                ,new LinkedBlockingQueue<Runnable>(),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(" onBind");
        return null;
    }

    @Override
    public void onCreate() {
        LogUtil.d(" onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(" onStartCommand");

        Bundle bundle = intent.getExtras();
        if(bundle !=null) {
            String url = bundle.getString(StaticContent.INTENT_KEY_URL);
            String title = bundle.getString(StaticContent.INTENT_KEY_TITLE);
            String summary = bundle.getString(StaticContent.INTENT_KEY_SUMMARY);
            playAudio(url, title, summary);
            mMusicNotification.sendNotification(title, summary);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d(" onDestroy");
        super.onDestroy();
        killMediaPlayer();
    }

    public void playAudio(final String url, final String title, final String summary){
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String mp3Url = JsoupHtmlUtils.getMp3Url(url);
                LogUtil.d(" play Audio title = "+title +" mp3Url = "+mp3Url);
                try {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(mp3Url);
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setLooping(false);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                    mMusicNotification.sendNotification(title,summary);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void killMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMusic(){
        mMediaPlayer.stop();
        killMediaPlayer();
        mMusicNotification.clearNotification(mApplicationContext);
    }

    public static class Builder {
        private Context context;
        public MusicService.Builder context(Application application) {
            this.context = application;
            return this;
        }

        public MusicService build() {
            if (context == null) {
                throw new NullPointerException("context for app must not be null");
            }
            return new MusicService(context);
        }
    }
}
