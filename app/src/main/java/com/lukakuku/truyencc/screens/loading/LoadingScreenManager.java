package com.lukakuku.truyencc.screens.loading;

import android.content.Context;
import android.content.Intent;

import com.lukakuku.truyencc.LoadingScreenActivity;

public class LoadingScreenManager {
    private static boolean isLoadingShown = false;

    public static void showLoadingScreen(Context context) {
        if (!isLoadingShown) {
            isLoadingShown = true;
            Intent intent = new Intent(context, LoadingScreenActivity.class);
            context.startActivity(intent);
        }
    }

    public static void hideLoadingScreen() {
        isLoadingShown = false;
    }
}
