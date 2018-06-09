package com.tingwa.contract;

import android.content.ContentValues;

import java.util.List;

/**
 * 使用eventbus 的话可以不用这个绑定view和presenter
 */
public interface SongContract {

    interface View {
        //更新界面数据
        void updateData(List<ContentValues> songList);
    }

    interface Presenter{
        //获取网络数据
        void loadData(int webType);
    }
}
