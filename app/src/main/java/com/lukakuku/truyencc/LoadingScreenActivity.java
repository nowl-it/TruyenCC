package com.lukakuku.truyencc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LoadingScreenActivity extends Activity {
    public static boolean IS_LOADING = true;

    public static void showLoading(Activity context) {
        IS_LOADING = true;
        Intent intent = new Intent(context, LoadingScreenActivity.class);
        context.startActivity(intent);
    }

    public static void hideLoading(Context context) {
        IS_LOADING = false;
        Intent intent = new Intent(context, LoadingScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!IS_LOADING) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!IS_LOADING) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!IS_LOADING) {
            finish();
        }
    }
}
