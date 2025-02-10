package com.lukakuku.truyencc.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChapterContent {
    private String title, content;

    @Nullable
    private Integer prev_chapter, next_chapter;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getPrevChapter() {
        return prev_chapter != null ? prev_chapter : -1;
    }

    public int getNextChapter() {
        return next_chapter != null ? next_chapter : -1;
    }

    @NonNull
    public String toString(int chapter) {
        return "" + chapter;
    }
}
