package com.tingwa.fragment;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tingwa.R;
import com.tingwa.adapter.SongAdapter;
import com.tingwa.constant.StaticContent;
import com.tingwa.decoration.DividerItemDecoration;
import com.tingwa.event.LoadDataEvent;
import com.tingwa.presenter.SongPresenter;
import com.tingwa.service.MusicService;
import com.tingwa.utils.LogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private SongAdapter mSongAdapter;
    private SongPresenter mSongPresenter;
    private Button mMineBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSongPresenter = null;
    }

    private void initViews(View view) {
        mSongPresenter = new SongPresenter(getContext());
        mRecyclerView = view.findViewById(R.id.fragment_main_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSongAdapter = new SongAdapter(getContext());
        mSongAdapter.setOnItemClickListener(new SongAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView songUrl = (TextView) view.findViewById(R.id.url);
                TextView songTitle = (TextView) view.findViewById(R.id.title);
                String url = (String) songUrl.getText();
                String title = (String) songTitle.getText();

                MusicService.getInstance().playAudio(url,title,title);

                //歌曲首页地址
                String songUrlText = (String) songUrl.getText();
                Toast.makeText(getContext(), "click ~" + " url = " + songUrlText, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getContext(), "long press ~" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mSongAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mMineBtn = view.findViewById(R.id.mine);
        mMineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongPresenter.loadData(StaticContent.MINE_PAGE);
            }
        });
        Button mainBtn = view.findViewById(R.id.main);
        Button topBtn = view.findViewById(R.id.top);
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSongPresenter.loadData(StaticContent.TOP_PAGE);
            }
        });

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d(" onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d(" onServiceDisconnected");
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLoadDataEvent(LoadDataEvent loadDataEvent) {
        LogUtil.d(" receiveLoadDataEvent data size = " + loadDataEvent.getSongList().size());
        //此处有问题 wch_bug
        mSongAdapter.clearAllItems();
        mSongAdapter.addAllItems(loadDataEvent.getSongList());
        mRecyclerView.setAdapter(mSongAdapter);
        mSongAdapter.notifyDataSetChanged();
    }
}
