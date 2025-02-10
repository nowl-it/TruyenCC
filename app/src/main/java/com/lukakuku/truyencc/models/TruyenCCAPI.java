package com.lukakuku.truyencc.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TruyenCCAPI {
    @GET("novel/new")
    Call<List<Novel>> getNewestNovels();

    @GET("novel/new/trang-{page}")
    Call<List<Novel>> getNewestNovelsByPage(
            @retrofit2.http.Path("page") int page
    );

    @GET("novel")
    Call<List<Novel>> getCompletedNovels();

    @GET("novel/trang-{page}")
    Call<List<Novel>> getCompletedNovelsByPage(
            @retrofit2.http.Path("page") int page
    );

    @GET("genre")
    Call<List<Genre>> getGenres();

    @GET("novel/genre/{genreId}")
    Call<List<Novel>> getNovelsByGenre(
            @retrofit2.http.Path("genreId") String genreId
    );

    @GET("novel/genre/{genreId}/trang-{page}")
    Call<List<Novel>> getNovelsByGenreAndPage(
            @retrofit2.http.Path("genreId") String genreId,
            @retrofit2.http.Path("page") int page
    );

    @GET("novel/hot")
    Call<List<Novel>> getHotNovels(
    );

    @GET("novel/hot/trang-{page}")
    Call<List<Novel>> getHotNovelsByPage(
            @retrofit2.http.Path("page") int page
    );

    @GET("novel/{novelId}")
    Call<NovelInfo> getNovelById(
            @retrofit2.http.Path("novelId") String novelId
    );

    @GET("novel/{novelId}/chapter/trang-{page}")
    Call<NovelChapter> getChaptersByNovelIdAndPage(
            @retrofit2.http.Path("novelId") String novelId,
            @retrofit2.http.Path("page") int page
    );

    @GET("novel/{novelId}/chapter/{chapterId}")
    Call<ChapterContent> getChapterContent(
            @retrofit2.http.Path("novelId") String novelId,
            @retrofit2.http.Path("chapterId") String chapterId
    );

    @GET("search/{query}")
    Call<List<Novel>> searchNovels(
            @retrofit2.http.Path("query") String query
    );
}
