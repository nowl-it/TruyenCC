package com.lukakuku.truyencc.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TruyenCCAPI {
    @GET("novel/new")
    Call<List<Novel>> getNewestNovels();

    @GET("novel")
    Call<List<Novel>> getCompletedNovels();

    @GET("genre")
    Call<List<Genre>> getGenres();

    @GET("novel/genre/{genreId}")
    Call<List<Novel>> getNovelsByGenre(
            @retrofit2.http.Path("genreId") String genreId
    );

    @GET("novel/hot")
    Call<List<Novel>> getHotNovels();
}
