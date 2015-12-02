package com.tingwa.activity;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tingwa.R;
import com.tingwa.adapter.MSimpleAdapter;
import com.tingwa.asynctask.LoadHtmlTask;
import com.tingwa.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private Button mBtnRetrofit, mBtnjsoup;
    private RecyclerView mRecyclerview;
    private MSimpleAdapter mSimpleAdapter;
    private List<ContentValues> mData;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mBtnRetrofit = (Button) findViewById(R.id.retofit);
        mBtnjsoup = (Button) findViewById(R.id.jsoup);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mContext = this;

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mData = new ArrayList<>();
        mSimpleAdapter = new MSimpleAdapter(mData,this);
        mSimpleAdapter.setOnItemClickListener(new MSimpleAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext,"click ~"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(mContext,"long press ~"+position,Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerview.setAdapter(mSimpleAdapter);
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

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
