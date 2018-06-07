package com.tingwa.utils;

import android.content.ContentValues;
import android.util.Log;

import com.tingwa.data.StaticContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by wuchunhui on 16-4-20.
 */
public class HtmlUtils {

    public static String getMp3Url(final String url) {
        try {
            Document mDocument = Jsoup.connect(url).timeout(5000).post();
            Document content = Jsoup.parse(mDocument.toString());
            Element div_class = content.select("div.module_song").first();
            Document tab_contains = Jsoup.parse(div_class.toString());
            Element audio_elem = tab_contains.getElementById("tw_player");
            String mp3Url = audio_elem.attr("init-data");
            return mp3Url;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 加载登录界面的内容
     * @param mData
     */
    public static void LoadMineContent(List<ContentValues> mData) {
        try {
            for(int i = 1;i<10;i++) {
                String url = StaticContent.MINE_URL;
                if(i >1){
                    url = url + "&p="+i+"&tag=0";
                }
                Log.i("wch elemname = ", "1111~~~~~~~~~~~  ");
                Document mDocument = Jsoup.connect(url).timeout(5000).post();
                Document content = Jsoup.parse(mDocument.toString());
                Element div_class = content.select("div.lt_frame").first();
                Document tab_contains = Jsoup.parse(div_class.toString());
                Elements elements_name = tab_contains.select("div.left");//界面的内容
                Elements elements_page = tab_contains.select("div.pagenavi");//选择页数的内容
                Elements elements_summary = tab_contains.select("div.top_10.clearfix");

//                Log.i("wch page elem = ", elements_page.toString());
//                Log.i("wch elements_summary = ", elements_summary.toString() +"####+++====");

//                for(Element summarys : elements_summary) {
//                    String str = summarys.getElementsByClass("top_10 clearfix").text();
//                    Log.i("wch str = ", elements_summary.toString());
//                }

                for (Element links : elements_name) {
                    String title = links.getElementsByTag("a").text();
                    String link = links.select("a").attr("href").trim();
                    Log.i("wch title = ", "~~~~~~~~~~~  " + title + "   link = " + link);
                    ContentValues values = new ContentValues();
                    values.put("title", title);
                    values.put("url", link);
                    mData.add(values);
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 排行榜的内容
     * @param mData
     */
    public static void LoadTopContent(List<ContentValues> mData) {
        try {
            Document mDocument = Jsoup.connect(StaticContent.TOP_URL).timeout(5000).post();
            Document content = Jsoup.parse(mDocument.toString());
            Element div_class = content.select("div.rt_frame").first();
            Document tab_contains = Jsoup.parse(div_class.toString());
            Element top_data = tab_contains.getElementById("top_data");
            Log.i("wch LoadTopContent ",top_data+" ~~");
            Elements elements_name = top_data.getElementsByClass("music_name");
            for (Element links : elements_name) {
                String title = links.getElementsByTag("a").text();
                String link = links.select("a").attr("href").trim();
//                String link = links.select("a").attr("href").trim();
//                String url = StaticData.TOP_URL + link;
//                murl += title + "  " + url + "\n";
                ContentValues values = new ContentValues();
                values.put("title", title);
                values.put("url", link);
                mData.add(values);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 主页的内容
     * @param mData
     */
    public static void LoadMainContent(List<ContentValues> mData) {
        try {
            Document mDocument = Jsoup.connect(StaticContent.MAIN_URL).timeout(5000).post();
            Document content = Jsoup.parse(mDocument.toString());
            Element tab_menu_top_20 = content.select("div.wrap_960").first();
            Document tab_contains = Jsoup.parse(tab_menu_top_20.toString());
            Elements elements_li = tab_contains.getElementsByTag("li");
            for (Element links : elements_li) {
                String title = links.getElementsByTag("a").text();

                String link = links.select("a").attr("href").replace("/", "").trim();
                String url = StaticContent.MAIN_URL + link;
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
}
