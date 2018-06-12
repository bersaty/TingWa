package com.tingwa.service;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tingwa.constant.StaticContent;
import com.tingwa.notificaton.MusicNotification;
import com.tingwa.utils.LogUtil;

import java.io.IOException;

/**
 *
 */
public class MusicService extends Service {

    private Context mApplicationContext;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private static volatile MusicService INSTANCE = null;
    private MusicNotification mMusicNotification;

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

        if(intent.getExtras() !=null) {
            String url = intent.getExtras().getString(StaticContent.INTENT_KEY_URL);
            String title = intent.getExtras().getString(StaticContent.INTENT_KEY_TITLE);
            String summary = intent.getExtras().getString(StaticContent.INTENT_KEY_SUMMARY);

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

    public void playAudio(String url,String title,String summary){
        LogUtil.d(" play Audio title = "+title +" url = "+url);
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMusicNotification.sendNotification(title,summary);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
