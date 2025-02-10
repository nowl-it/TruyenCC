package com.lukakuku.truyencc.models;

public class Chapter {
    private final String id;
    private final String title;

    public Chapter(String id, String title, String content) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
