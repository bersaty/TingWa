package com.tingwa.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tingwa.R;
import com.tingwa.adapter.MSimpleAdapter;
import com.tingwa.asynctask.LoadHtmlTask;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private Button mBtnRetrofit, mBtnjsoup;
    private RecyclerView mRecyclerview;
    private MSimpleAdapter mSimpleAdapter;
    private List<ContentValues> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mBtnRetrofit = (Button) findViewById(R.id.retofit);
        mBtnjsoup = (Button) findViewById(R.id.jsoup);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();
        mSimpleAdapter = new MSimpleAdapter(mData,this);
        mRecyclerview.setAdapter(mSimpleAdapter);

        mBtnRetrofit.setOnClickListener(this);
        mBtnjsoup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retofit:
                break;
            case R.id.jsoup:
                LoadHtmlTask loadHtmlTask = new LoadHtmlTask(this, mData,mSimpleAdapter);
                if (mData.isEmpty())
                    loadHtmlTask.execute();
                Log.i("wch ", mData + " ");
                break;
        }
    }

}