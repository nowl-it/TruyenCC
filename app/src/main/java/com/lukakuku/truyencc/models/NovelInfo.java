package com.lukakuku.truyencc.models;

import java.util.List;

public class NovelInfo {
    private final String novel_id;
    private final String title;
    private final String cover_img_url;
    private final String cre;
    private final String status;

    private final String description;

    private final Author author;

    private final List<Genre> genres;

    public NovelInfo(String novel_id, String title, String cover_img_url, String cre, String status, String description, Author author, List<Genre> genres) {
        this.novel_id = novel_id;
        this.title = title;
        this.cover_img_url = cover_img_url;
        this.cre = cre;
        this.status = status;
        this.description = description;
        this.author = author;
        this.genres = genres;
    }

    public String getNovelId() {
        return novel_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverImageUrl() {
        return cover_img_url;
    }

    public String getCre() {
        return cre;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Author getAuthor() {
        return author;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getGenresName() {
        StringBuilder genresName = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            genresName.append(genres.get(i).getName());
            if (i != genres.size() - 1) {
                genresName.append(", ");
            }
        }
        return genresName.toString();
    }
}
