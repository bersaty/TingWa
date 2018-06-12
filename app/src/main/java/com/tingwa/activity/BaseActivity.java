package com.tingwa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tingwa.utils.LogUtil;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        LogUtil.d(getClass().getSimpleName() + " onStart ");
        super.onStart();
    }

    @Override
    protected void onStop() {
        LogUtil.d(getClass().getSimpleName() + " onStop ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.d(getClass().getSimpleName() + " onDestroy ");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        LogUtil.d(getClass().getSimpleName() + " onBackPressed ");
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        LogUtil.d(getClass().getSimpleName() + " onPause ");
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.d(getClass().getSimpleName() + " onNewIntent ");
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        LogUtil.d(getClass().getSimpleName() + " onResume ");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        LogUtil.d(getClass().getSimpleName() + " onRestart ");
        super.onRestart();
    }
}
