package com.tingwa.notificaton;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tingwa.activity.MainActivity;
import com.tingwa.receiver.MusicExitReceiver;
import com.tingwa.utils.LogUtil;

public class MusicNotification extends NotificationBase {

    private ContentValues mMusicInfo;
    Context mContext;

    public MusicNotification(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    int getNotificationID() {
        return MUSIC_NOTIFICATION_ID;
    }

    public void sendNotification(String title, String summary) {
        LogUtil.d("sendMusicNotification");


        //点击事件
        Intent openIntent = new Intent();
        openIntent.setClass(mContext, MainActivity.class);
        openIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent openPI = PendingIntent.getActivity(mContext, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //退出按扭
        Intent exitIntent = new Intent();
        exitIntent.setAction(MusicExitReceiver.MUSIC_EXIT_ACTION);
        exitIntent.putExtra(NOTIFICATION_CMD_KEY, NOTIFICATION_CMD_EXIT_MUSIC);
        PendingIntent exitPI = PendingIntent.getBroadcast(mContext, 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(0, "退出", exitPI)
                .build();

        Notification notification;
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            notification = getChannelNotification()
                    .setContentIntent(openPI)
                    .setContentText(summary)
                    .setContentTitle(title)
                    .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                    .addAction(action)
                    .build();
        } else {
            notification = getNotification()
                    .setContentIntent(openPI)
                    .setContentText(summary)
                    .setContentTitle(title)
                    .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                    .addAction(action)
                    .build();
        }

        //悬浮
        notification.flags |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
        //点击后删除，如果是FLAG_NO_CLEAR则不删除，FLAG_ONGOING_EVENT用于某事正在进行，例如电话。
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(MUSIC_NOTIFICATION_ID, notification);
    }
}
