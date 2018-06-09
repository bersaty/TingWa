package com.tingwa.Presenter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.tingwa.contract.SongContract;
import com.tingwa.data.StaticContent;
import com.tingwa.event.LoadDataEvent;
import com.tingwa.utils.EventUtils;
import com.tingwa.utils.JsoupHtmlUtils;
import com.tingwa.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class SongPresenter extends BasePresenter implements SongContract.Presenter {

    private List<ContentValues> mSongList = new ArrayList<>();
    private LoadHtmlTask mLoadHtmlTask = null;

    public SongPresenter(Context context) {
        super(context);
    }

    @Override
    public void loadData(int webType) {
        mLoadHtmlTask = new LoadHtmlTask(mContext, mSongList, webType);
        mLoadHtmlTask.execute();
    }

    /**
     * Created by wuchunhui on 15-11-9.
     */
    //异步获取信息
    public class LoadHtmlTask extends AsyncTask<String, String, String> {
        ProgressDialog mProgressDialog;
        Context mContext;
        List<ContentValues> mData;
        int WebType;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (WebType == StaticContent.MAIN_PAGE) {
                //主页内容
                JsoupHtmlUtils.LoadMainContent(mData);
            }
            if (WebType == StaticContent.TOP_PAGE) {
                //排行榜内容
                JsoupHtmlUtils.LoadTopContent(mData);
            }
            if (WebType == StaticContent.MINE_PAGE) {
                //浅蓝、暮的主页
                JsoupHtmlUtils.LoadMineContent(mData);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            LogUtil.d("result = " + result);
            LoadDataEvent event = new LoadDataEvent();
            event.setSongList(mData);
            EventUtils.sendLoadDataEvent(event);
            mProgressDialog.dismiss();
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

        public LoadHtmlTask(Context context, List<ContentValues> data, int webtype) {
            WebType = webtype;
            mContext = context;
            mData = data;
        }
    }
}
