package com.tingwa.utils;

import com.tingwa.event.LoadDataEvent;

import org.greenrobot.eventbus.EventBus;

public class EventUtils {

    public static void sendLoadDataEvent(LoadDataEvent event){
        LogUtil.d("sendLoadDataEvent");
        EventBus.getDefault().post(event);
    }
}
