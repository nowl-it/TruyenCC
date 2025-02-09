package com.lukakuku.truyencc;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lukakuku.truyencc.adapters.VPAdapter;
import com.lukakuku.truyencc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoadingScreenActivity.showLoading(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);

        FragmentManager fm = getSupportFragmentManager();
        VPAdapter adapter = new VPAdapter(fm, getLifecycle());

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        customNavigationBtn();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            viewPager.setCurrentItem(item.getOrder(), false);
            return true;
        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });

        viewPager.setCurrentItem(
                bottomNavigationView.getMenu().findItem(
                        bottomNavigationView.findViewById(R.id.navigation_home).getId()).getOrder(),
                false);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);

            recreate();

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void customNavigationBtn() {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            View view = bottomNavigationView.findViewById(menuItem.getItemId());
            if (view != null) {
                view.setOnLongClickListener(v -> true);

                view.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
                            break;

                        case MotionEvent.ACTION_UP:
                            v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                            v.performClick();
                            break;

                        case MotionEvent.ACTION_CANCEL:
                            v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                            break;

                        default:
                            break;
                    }
                    return true;
                });
            }
        }
    }
}
