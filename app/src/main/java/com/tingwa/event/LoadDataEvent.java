package com.tingwa.event;

import android.content.ContentValues;

import java.util.List;

public class LoadDataEvent {

    List<ContentValues> mSongList;

    public List<ContentValues> getSongList() {
        return mSongList;
    }

    public void setSongList(List<ContentValues> songList) {
        this.mSongList = songList;
    }
}
