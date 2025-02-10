package com.lukakuku.truyencc.models;

public class Novel {
    private String novel_id, title, cover_img_url;
    private int newest_chapter;

    public Novel(String novel_id, String title, String cover_img_url, int newest_chapter) {
        this.novel_id = novel_id;
        this.title = title;
        this.cover_img_url = cover_img_url;
        this.newest_chapter = newest_chapter;
    }

    public String getNovelId() {
        return novel_id;
    }

    public Novel setNovelId(String novel_id) {
        this.novel_id = novel_id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Novel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCoverImgUrl() {
        return cover_img_url;
    }

    public Novel setCoverImgUrl(String cover_img_url) {
        this.cover_img_url = cover_img_url;
        return this;
    }

    public int getNewestChapter() {
        return newest_chapter;
    }

    public Novel setNewestChapter(int newest_chapter) {
        this.newest_chapter = newest_chapter;
        return this;
    }
}
