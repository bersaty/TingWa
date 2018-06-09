package com.tingwa.activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.tingwa.R;
import com.tingwa.data.StaticContent;
import com.tingwa.fragment.MainFragment;
import com.tingwa.utils.MusicCacheUtils;

import java.io.File;
import java.lang.ref.WeakReference;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private MusicCacheUtils mMusicCacheUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mMusicCacheUtils = new MusicCacheUtils(this);

        File destDir = new File(mContext.getExternalCacheDir() + "/mp3");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,new MainFragment())
                .commit();
    }

    @Override
    public void onClick(View v) {

    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(false);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
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

    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            switch (msg.what) {
                case StaticContent.MUSIC_LAST:
                    break;
                case StaticContent.MUSIC_NEXT:
                    break;
                case StaticContent.MUSIC_STOP:
                    break;
                case StaticContent.MUSIC_PLAY:
                    int position = (int) msg.obj;
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                String mp3url;
//                                //从cache里面查找是否存在音乐
//                                if (mMusicCacheUtils.isMusicExist(mSongUrl)) {
//                                    mp3url = MusicCacheUtils.getDiskCacheFilePath(activity, mSongUrl);
//                                }
//                                else {//不存在就下载到缓存里面
//                                    mp3url = JsoupHtmlUtils.getMp3Url(mSongUrl);
//                                    mMusicCacheUtils.putMusic(mSongUrl,activity);
//                                }
//                                playAudio(mp3url);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
                    break;
                    default:break;
            }
        }
    }
}
