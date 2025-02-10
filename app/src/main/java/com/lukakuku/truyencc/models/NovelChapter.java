package com.lukakuku.truyencc.models;

import java.util.List;

public class NovelChapter {
    private final List<Chapter> chapters;
    private final int page;
    private final int maxPage;

    public NovelChapter(List<Chapter> chapters, int page, int maxPage) {
        this.chapters = chapters;
        this.page = page;
        this.maxPage = maxPage;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public int getPage() {
        return page;
    }

    public int getMaxPage() {
        return maxPage;
    }
}
