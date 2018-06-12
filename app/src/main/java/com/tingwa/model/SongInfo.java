package com.tingwa.model;

/**
 * Created by wuchunhui on 16-4-21.
 * 解析出来的数据
 */
public class SongInfo {
    private String music_singer;//艺术家
    private String recommend_time;//推荐时间
    private String listen_number;//播放次数
    private String music_style;//音乐风格
    private String musical_instruments;//包含乐器
    private String web_url;//网页网址
    private String mp3_url;//mp3地址
    private String pic_url;//图片地址
    private String introduction;//介绍语

    public String getMusic_singer() {
        return music_singer;
    }

    public void setMusic_singer(String music_singer) {
        this.music_singer = music_singer;
    }

    public String getRecommend_time() {
        return recommend_time;
    }

    public void setRecommend_time(String recommend_time) {
        this.recommend_time = recommend_time;
    }

    public String getListen_number() {
        return listen_number;
    }

    public void setListen_number(String listen_number) {
        this.listen_number = listen_number;
    }

    public String getMusic_style() {
        return music_style;
    }

    public void setMusic_style(String music_style) {
        this.music_style = music_style;
    }

    public String getMusical_instruments() {
        return musical_instruments;
    }

    public void setMusical_instruments(String musical_instruments) {
        this.musical_instruments = musical_instruments;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getMp3_url() {
        return mp3_url;
    }

    public void setMp3_url(String mp3_url) {
        this.mp3_url = mp3_url;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
