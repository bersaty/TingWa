package com.tingwa.service;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tingwa.constant.StaticContent;
import com.tingwa.notificaton.MusicNotification;
import com.tingwa.utils.LogUtil;

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

    public static MusicService getInstance() {
        return INSTANCE;
    }

    private MusicService(Context applicationContext) {
        this.mApplicationContext = applicationContext;
        mMusicNotification = new MusicNotification(applicationContext);
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

        String url = intent.getExtras().getString(StaticContent.INTENT_KEY_URL);
        String title = intent.getExtras().getString(StaticContent.INTENT_KEY_TITLE);
        String summary = intent.getExtras().getString(StaticContent.INTENT_KEY_SUMMARY);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogUtil.d(" onDestroy");
        super.onDestroy();
        killMediaPlayer();
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(false);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        mMusicNotification.sendNotification("听蛙");
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
