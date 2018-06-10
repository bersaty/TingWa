package com.tingwa.notificaton;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class MusicNotification {

    public static int NOTIFICATION_MUSIC_ID = 1;

    NotificationManager mManager;
    Context mContext;

    public MusicNotification(Context context) {
        mContext = context;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendNotification(String title,String summary){

        Intent intent = new Intent();

        Notification notification = new Notification.Builder(mContext)
                .setContentText(summary)
                .setContentTitle(title)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .build();

        mManager.notify(NOTIFICATION_MUSIC_ID,notification);
    }
}
