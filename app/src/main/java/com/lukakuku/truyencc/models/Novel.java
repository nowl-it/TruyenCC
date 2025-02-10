package com.lukakuku.truyencc.models;

public class Novel {
    private final String novel_id;
    private final String cover_img_url;
    private final int newest_chapter;
    private final String title;

    public Novel(String novel_id, String title, String cover_img_url, int newest_chapter) {
        this.novel_id = novel_id;
        this.title = title;
        this.cover_img_url = cover_img_url;
        this.newest_chapter = newest_chapter;
    }

    public String getNovelId() {
        return novel_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverImgUrl() {
        return cover_img_url;
    }


    public int getNewestChapter() {
        return newest_chapter;
    }

    @Override
    public String toString() {
        return "Novel{" +
                "novel_id='" + novel_id + '\'' +
                ", cover_img_url='" + cover_img_url + '\'' +
                ", newest_chapter=" + newest_chapter +
                ", title='" + title + '\'' +
                '}';
    }
}
