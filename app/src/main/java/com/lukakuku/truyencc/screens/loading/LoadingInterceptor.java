package com.lukakuku.truyencc.screens.loading;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class LoadingInterceptor implements Interceptor {
    private final Context context;

    public LoadingInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        LoadingScreenManager.showLoadingScreen(context);

        try {
            return chain.proceed(chain.request());
        } finally {
            LoadingScreenManager.hideLoadingScreen();
        }
    }
}
