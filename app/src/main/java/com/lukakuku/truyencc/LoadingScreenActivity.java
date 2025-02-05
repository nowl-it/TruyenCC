package com.lukakuku.truyencc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoadingScreenActivity extends AppCompatActivity {
    protected SharedPreferences preferences;
    private static final String IS_FIRST_LAUNCH = "is_first_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferences = getSharedPreferences("app_session", MODE_PRIVATE);

        boolean isFirstLaunch = preferences.getBoolean(IS_FIRST_LAUNCH, true);
        Looper looper = Looper.myLooper();

        Log.d("[SCREEN]<LOADING>", "onCreate: isFirstLaunch = " + isFirstLaunch);

        if (!isFirstLaunch) {
            startMainActivity();
            return;
        }

        preferences.edit().putBoolean(IS_FIRST_LAUNCH, false).apply();

        setContentView(R.layout.activity_loading_screen);

        if (looper == null) {
            looper = Looper.getMainLooper();
        }

        Handler handler = new Handler(looper);

        handler.postDelayed(this::startMainActivity, 3000);
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("[SCREEN]<LOADING>", "onDestroy: Activity destroyed");

        preferences.edit().putBoolean(IS_FIRST_LAUNCH, true).apply();
    }
}
