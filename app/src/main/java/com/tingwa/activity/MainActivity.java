package com.tingwa.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tingwa.R;
import com.tingwa.adapter.MSimpleAdapter;
import com.tingwa.asynctask.LoadHtmlTask;
import com.tingwa.data.StaticContent;
import com.tingwa.decoration.DividerItemDecoration;
import com.tingwa.utils.DownLoadUtils;
import com.tingwa.utils.HtmlUtils;
import com.tingwa.utils.MusicCacheUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mSongTitle;
    private Button mBtnRetrofit, mBtnMain, mBtnTop, mBtnMine;
    private RecyclerView mRecyclerview;
    private MSimpleAdapter mSimpleAdapter;
    private List<ContentValues> mData;
    private Context mContext;

    private MediaPlayer mMediaPlayer;
    private LoadHtmlTask mLoadHtmlTask;
    private String mSongUrl;

    private MyHandler mHandler;
    private MusicCacheUtils mMusicCacheUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSongTitle = (TextView) findViewById(R.id.song_title);
        mBtnRetrofit = (Button) findViewById(R.id.testPlay);
        mBtnMain = (Button) findViewById(R.id.main);
        mBtnTop = (Button) findViewById(R.id.top);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mBtnMine = (Button) findViewById(R.id.mine);
        mContext = this;
        mHandler = new MyHandler(this);
        mMusicCacheUtils = new MusicCacheUtils(this);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();
        mSimpleAdapter = new MSimpleAdapter(mData, this);
        mSimpleAdapter.setOnItemClickListener(new MSimpleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView songUrl = (TextView) view.findViewById(R.id.url);
                TextView songTitle = (TextView) view.findViewById(R.id.title);

                Message msg = new Message();
                msg.what = StaticContent.MUSIC_PLAY;
                msg.obj = position;
                mHandler.sendMessage(msg);
                //歌曲首页地址
                mSongUrl = (String) songUrl.getText();
                mSongTitle.setText(songTitle.getText());
                Toast.makeText(mContext, "click ~" + " url = " + mSongUrl, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(mContext, "long press ~" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerview.setAdapter(mSimpleAdapter);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mBtnRetrofit.setOnClickListener(this);
        mBtnMain.setOnClickListener(this);
        mBtnTop.setOnClickListener(this);
        mBtnMine.setOnClickListener(this);

        File destDir = new File(mContext.getExternalCacheDir() + "/mp3");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testPlay:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            playAudio(mSongUrl);
                            DownLoadUtils.mp3load(StaticContent.testUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(mContext, " url = " + mSongUrl, Toast.LENGTH_SHORT).show();

                break;
            case R.id.main:
                mLoadHtmlTask = new LoadHtmlTask(this, mData, mSimpleAdapter, StaticContent.MAIN_PAGE);
                if (mData.isEmpty())
                    mLoadHtmlTask.execute();
                break;
            case R.id.top:
                mLoadHtmlTask = new LoadHtmlTask(this, mData, mSimpleAdapter, StaticContent.TOP_PAGE);
                if (mData.isEmpty())
                    mLoadHtmlTask.execute();
                break;
            case R.id.mine:
                mLoadHtmlTask = new LoadHtmlTask(this, mData, mSimpleAdapter, StaticContent.MINE_PAGE);
                if (mData.isEmpty())
                    mLoadHtmlTask.execute();
                break;
        }
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(false);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                Toast.makeText(MainActivity.this,"播放完",Toast.LENGTH_SHORT).show();

                String uuu = (String) mData.get(new Random().nextInt() % 10).get("url");

                try {
                    playAudio(uuu);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String mp3url;
                                if (mMusicCacheUtils.isMusicExist(mSongUrl))//从cache里面查找是否存在音乐
                                    mp3url = mMusicCacheUtils.getDiskCacheFilePath(activity, mSongUrl);
                                else {//不存在就下载到缓存里面
                                    mp3url = HtmlUtils.getMp3Url(mSongUrl);
                                    mMusicCacheUtils.putMusic(mSongUrl,activity);
                                }
                                playAudio(mp3url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;

            }
        }
    }
}
