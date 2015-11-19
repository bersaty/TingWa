package com.tingwa.asynctask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tingwa.adapter.MSimpleAdapter;
import com.tingwa.com.tingwa.data.StaticData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by wuchunhui on 15-11-9.
 */
//异步获取信息
public class LoadHtmlTask extends AsyncTask<String, String, String> {
    ProgressDialog mProgressDialog;
    Document mDocument;
    Context mContext;
    String murl = "";
    List<ContentValues> mData;
    MSimpleAdapter mSimpleAdapter;

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        try {
            mDocument = Jsoup.connect(StaticData.URL).timeout(5000).post();
            Document content = Jsoup.parse(mDocument.toString());
            Element tab_menu_top_20 = content.select("div.wrap_960").first();
            Document tab_contains = Jsoup.parse(tab_menu_top_20.toString());
            Elements elements_li = tab_contains.getElementsByTag("li");
            for (Element links : elements_li) {
                String title = links.getElementsByTag("a").text();

                String link = links.select("a").attr("href").replace("/", "").trim();
                String url = StaticData.URL + link;
                murl +=title+"  "+url+"\n";
                ContentValues values = new ContentValues();
                values.put("title", title);
                values.put("url", url);
                mData.add(values);
//                usedatabase.insert("Cach", values);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        Log.d("wch  ", murl+"");
        if(mSimpleAdapter != null) mSimpleAdapter.notifyDataSetChanged();
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

    public LoadHtmlTask(Context context,List<ContentValues> data,MSimpleAdapter adapter) {
        mContext = context;
        mData = data;
        mSimpleAdapter = adapter;
    }
}