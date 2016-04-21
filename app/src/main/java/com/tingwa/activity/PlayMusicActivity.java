package com.tingwa.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;

import com.tingwa.R;

public class PlayMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.y = 30;
        layoutParams.height = 150;
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;//点击可以穿透透明区域
        getWindow().setAttributes(layoutParams);

    }
}
