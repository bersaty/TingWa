package com.tingwa.asynctask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tingwa.adapter.MSimpleAdapter;
import com.tingwa.com.tingwa.data.HtmlTagContent;
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
    int WebType;

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        if (WebType == HtmlTagContent.MAIN_PAGE) //主页内容
            LoadMainContent();
        if (WebType == HtmlTagContent.TOP_PAGE) //排行榜内容
            LoadTopContent();
        return null;
    }

    private void LoadTopContent() {
        try {
            mDocument = Jsoup.connect(StaticData.TOP_URL).timeout(5000).post();
            Document content = Jsoup.parse(mDocument.toString());
            Element div_class = content.select("div.rt_frame").first();
            Document tab_contains = Jsoup.parse(div_class.toString());
            Element top_data = tab_contains.getElementById("top_data");
            Elements elements_dd = top_data.getElementsByTag("dd");
//            Log.i("wch title = ",elements_dd.toString());

            for (Element links : elements_dd) {
                String title = links.getElementsByTag("a").text();
                Log.i("wch title = ",title);

//                String link = links.select("a").attr("href").replace("/", "").trim();
//                String url = StaticData.TOP_URL + link;
//                murl += title + "  " + url + "\n";
//                ContentValues values = new ContentValues();
//                values.put("title", title);
//                values.put("url", url);
//                mData.add(values);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void LoadMainContent() {
        try {
            mDocument = Jsoup.connect(StaticData.MAIN_URL).timeout(5000).post();
            Document content = Jsoup.parse(mDocument.toString());
            Element tab_menu_top_20 = content.select("div.wrap_960").first();
            Document tab_contains = Jsoup.parse(tab_menu_top_20.toString());
            Elements elements_li = tab_contains.getElementsByTag("li");
            for (Element links : elements_li) {
                String title = links.getElementsByTag("a").text();

                String link = links.select("a").attr("href").replace("/", "").trim();
                String url = StaticData.MAIN_URL + link;
                murl += title + "  " + url + "\n";
                ContentValues values = new ContentValues();
                values.put("title", title);
                values.put("url", url);
                mData.add(values);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public LoadHtmlTask(Context context, List<ContentValues> data, MSimpleAdapter adapter,int webtype) {
        WebType = webtype;
        mContext = context;
        mData = data;
        mSimpleAdapter = adapter;
    }
}