package com.tingwa.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

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
}
