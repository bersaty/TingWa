package com.tingwa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tingwa.R;
import com.tingwa.fragment.MainFragment;
import com.tingwa.service.MusicService;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,new MainFragment())
                .commit();
        Intent intent = new Intent();
        intent.setClass(this,MusicService.class);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
