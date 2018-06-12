package com.tingwa.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tingwa.notificaton.NotificationBase;
import com.tingwa.service.MusicService;
import com.tingwa.utils.LogUtil;

public class MusicExitReceiver extends BroadcastReceiver {

    public static final String MUSIC_EXIT_ACTION = "com.wch.intent.action.MUSIC_EXIT";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(NotificationBase.NOTIFICATION_CMD_EXIT_MUSIC.
                equals(intent.getExtras().getString(NotificationBase.NOTIFICATION_CMD_KEY))){
            LogUtil.d(" MusicExitReceiver ");
            MusicService.getInstance().stopMusic();
        }
    }
}
