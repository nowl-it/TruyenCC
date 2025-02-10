package com.lukakuku.truyencc.screens;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.RetrofitClient;
import com.lukakuku.truyencc.adapters.NovelAdapter;
import com.lukakuku.truyencc.models.Novel;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllActivity extends AppCompatActivity {
    private NovelAdapter adapter;
    private List<Novel> novels;
    private TruyenCCAPI api;
    private int page = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_all);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        setTitle(title);

        TextView textView = findViewById(R.id.title);
        textView.setText(title);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            novels.clear();
            adapter.notifyDataSetChanged();
            loadNovels(); // Tải lại từ đầu
            swipeRefreshLayout.setRefreshing(false);
        });

        ImageButton backButton = findViewById(R.id.back_btn);
        ImageButton scrollToTopButton = findViewById(R.id.scroll_to_top);

        backButton.setOnClickListener(v -> finish());

        scrollToTopButton.setOnClickListener(v -> {
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.smoothScrollToPosition(0);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);

        recyclerView.setLayoutManager(layoutManager);
        addSpaceItem(recyclerView);

        novels = new ArrayList<>();
        adapter = new NovelAdapter(this, novels);
        recyclerView.setAdapter(adapter);

        api = RetrofitClient.getClient().create(TruyenCCAPI.class);
        loadNovels();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                FlexboxLayoutManager layoutManager = (FlexboxLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == novels.size() - 1) {
                    isLoading = true;
                    page++;
                    loadNovels();
                }

                if (layoutManager != null && layoutManager.findFirstCompletelyVisibleItemPosition() > 0) {
                    scrollToTopButton.setVisibility(View.VISIBLE);
                } else {
                    scrollToTopButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadNovels() {
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 0);

        Call<List<Novel>> call;
        switch (type) {
            case 0:
                call = api.getHotNovelsByPage(page);
                break;
            case 1:
                call = api.getNewestNovelsByPage(page);
                break;
            case 2:
                call = api.getCompletedNovelsByPage(page);
                break;
            default:
                return;
        }

        call.enqueue(new Callback<List<Novel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Novel>> call, @NonNull Response<List<Novel>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    isLoading = false;
                    return;
                }
                List<Novel> newNovels = response.body();
                novels.addAll(newNovels);
                adapter.addNovels(newNovels);
                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<List<Novel>> call, @NonNull Throwable t) {
                isLoading = false;
            }
        });
    }

    private void addSpaceItem(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 32;
            }
        });
    }
}
