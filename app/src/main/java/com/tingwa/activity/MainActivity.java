package com.tingwa.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tingwa.R;
import com.tingwa.adapter.MSimpleAdapter;
import com.tingwa.asynctask.LoadHtmlTask;
import com.tingwa.decoration.DividerItemDecoration;
import com.tingwa.utils.HtmlUtils;
import com.tingwa.data.StaticContent;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextView;
    private Button mBtnRetrofit, mBtnMain, mBtnTop, mBtnMine;
    private RecyclerView mRecyclerview;
    private MSimpleAdapter mSimpleAdapter;
    private List<ContentValues> mData;
    private Context mContext;

    private MediaPlayer mediaPlayer;
    LoadHtmlTask loadHtmlTask;
    private String mp3Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mBtnRetrofit = (Button) findViewById(R.id.testPlay);
        mBtnMain = (Button) findViewById(R.id.main);
        mBtnTop = (Button) findViewById(R.id.top);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mBtnMine = (Button) findViewById(R.id.mine);
        mContext = this;

        Intent intent = new Intent(this, PlayMusicActivity.class);
        startActivity(intent);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();
        mSimpleAdapter = new MSimpleAdapter(mData, this);
        mSimpleAdapter.setOnItemClickListener(new MSimpleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                final TextView textView = (TextView) view.findViewById(R.id.url);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mp3Url = HtmlUtils.getMp3Url((String) textView.getText());
                        Log.i("wch mp3url = ", mp3Url + "~");
//                        try {
//                            playAudio((String) textView.getText());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                }).start();

                Toast.makeText(mContext, "click ~" + " url = " + mp3Url, Toast.LENGTH_SHORT).show();

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testPlay:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            playAudio(mp3Url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(mContext, " url = " + mp3Url, Toast.LENGTH_SHORT).show();

                break;
            case R.id.main:
                loadHtmlTask = new LoadHtmlTask(this, mData, mSimpleAdapter, StaticContent.MAIN_PAGE);
                if (mData.isEmpty())
                    loadHtmlTask.execute();
                break;
            case R.id.top:
                loadHtmlTask = new LoadHtmlTask(this, mData, mSimpleAdapter, StaticContent.TOP_PAGE);
                if (mData.isEmpty())
                    loadHtmlTask.execute();
                break;
            case R.id.mine:
                loadHtmlTask = new LoadHtmlTask(this, mData, mSimpleAdapter, StaticContent.MINE_PAGE);
                if (mData.isEmpty())
                    loadHtmlTask.execute();
                break;
        }
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
