package com.tingwa.notificaton;

import android.app.NotificationManager;
import android.content.Context;


public abstract class NotificationBase{
    private static final String TAG = "NotificationBase";
    public static final int MUSIC_NOTIFICATION = 1000;
    public static final String NOTIFICATION_CMD_KEY = "notification_cmd_key";
    public static final String NOTIFICATION_CMD_EXIT_MUSIC = "notification_cmd_exit_music";
    private final Context mContext;

    protected final NotificationManager mNotificationManager;
    protected boolean mNotificationClearFlag;

    public NotificationBase(Context context) {
        this.mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    abstract int getNotificationID();

    public void clearNotification(Context context) {
        if(mNotificationManager != null) {
            mNotificationManager.cancel(getNotificationID());
        }
    }

    public static void clearNotificationById(int id,Context context){
        NotificationManager notifyMgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMgr.cancel(id);
    }

    public static void clearAllNotification(Context context){
        NotificationManager notifyMgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMgr.cancelAll();
    }
}
