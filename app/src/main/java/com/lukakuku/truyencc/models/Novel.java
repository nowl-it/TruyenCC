package com.lukakuku.truyencc.models;

import androidx.annotation.NonNull;

public class Novel {
    private String novel_id, title, cover_img_url;
    private int newest_chapter;

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

    @NonNull
    public String toString() {
        return "Novel{" +
                "novel_id='" + novel_id + '\'' +
                ", title='" + title + '\'' +
                ", cover_img_url='" + cover_img_url + '\'' +
                ", newest_chapter=" + newest_chapter +
                '}';
    }
}
