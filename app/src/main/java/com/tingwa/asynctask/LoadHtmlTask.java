package com.tingwa.asynctask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tingwa.adapter.MSimpleAdapter;
import com.tingwa.data.StaticContent;
import com.tingwa.utils.HtmlUtils;

import java.util.List;

/**
 * Created by wuchunhui on 15-11-9.
 */
//异步获取信息
public class LoadHtmlTask extends AsyncTask<String, String, String> {
    ProgressDialog mProgressDialog;
    Context mContext;
    String murl = "";
    List<ContentValues> mData;
    MSimpleAdapter mSimpleAdapter;
    int WebType;

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        if (WebType == StaticContent.MAIN_PAGE) //主页内容
            HtmlUtils.LoadMainContent(mData);
        if (WebType == StaticContent.TOP_PAGE) //排行榜内容
            HtmlUtils.LoadTopContent(mData);
        if (WebType == StaticContent.MINE_PAGE)
            HtmlUtils.LoadMineContent(mData);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        Log.d("wch  ", murl + "");
        if (mSimpleAdapter != null) mSimpleAdapter.notifyDataSetChanged();
        mProgressDialog.dismiss();
//        ListItemAdapter adapter = new ListItemAdapter(context, usedatabase.getlist());
//        listmenu.setAdapter(adapter);
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在加载数据····");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public LoadHtmlTask(Context context, List<ContentValues> data, MSimpleAdapter adapter, int webtype) {
        WebType = webtype;
        mContext = context;
        mData = data;
        mSimpleAdapter = adapter;
    }
}