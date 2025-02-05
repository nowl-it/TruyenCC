package com.lukakuku.truyencc.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TruyenCCAPI {
    @GET("genre")
    Call<List<Genre>> getGenres();
}
