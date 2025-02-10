package com.lukakuku.truyencc.screens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.RetrofitClient;
import com.lukakuku.truyencc.adapters.GenreAdapter;
import com.lukakuku.truyencc.adapters.NovelAdapter;
import com.lukakuku.truyencc.models.Genre;
import com.lukakuku.truyencc.models.Novel;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllByGenreActivity extends AppCompatActivity {
    private NovelAdapter novelAdapter;
    private RecyclerView novelRecyclerView;

    private TextView textView;

    private List<Novel> novels;

    private TruyenCCAPI api;
    private int page = 1;
    private boolean isLoading = false;

    private String GENRE_SELECTED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_all_by_genre);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        setTitle(title);

        textView = findViewById(R.id.title);
        textView.setText(title);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            if (novels != null) novels.clear();
            if (novelAdapter != null) novelAdapter.notifyDataSetChanged();
            loadNovels();
            swipeRefreshLayout.setRefreshing(false);
        });

        ImageButton backButton = findViewById(R.id.back_btn);
        ImageButton scrollToTopButton = findViewById(R.id.scroll_to_top);

        backButton.setOnClickListener(v -> finish());

        scrollToTopButton.setOnClickListener(v -> {
            novelRecyclerView.smoothScrollToPosition(0);
        });

        novelRecyclerView = findViewById(R.id.novel);

        ProgressBar loadingProgressBar = findViewById(R.id.loading_bar);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        layoutManager.setAlignItems(AlignItems.FLEX_START);

        novelRecyclerView.setLayoutManager(layoutManager);

        addSpaceBottomItem(novelRecyclerView);

        novels = new ArrayList<>();

        novelAdapter = new NovelAdapter(this, novels);

        novelRecyclerView.setAdapter(novelAdapter);

        api = RetrofitClient.getClient().create(TruyenCCAPI.class);
        loadGenres();
        loadNovels();

        novelRecyclerView.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.GONE);

        novelRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void loadGenres() {
        Intent intent = getIntent();
        String type = GENRE_SELECTED != null ? GENRE_SELECTED : intent.getStringExtra("type");

        Context context = this;
        Call<List<Genre>> call = api.getGenres();

        RecyclerView genreRecyclerView = findViewById(R.id.genre);
        genreRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(@NonNull Call<List<Genre>> call, @NonNull Response<List<Genre>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    isLoading = false;
                    return;
                }
                List<Genre> genres = response.body();

                GenreAdapter genreAdapter = new GenreAdapter(context, genres);

                genreRecyclerView.setAdapter(genreAdapter);
                addSpaceRightItem(genreRecyclerView);

                genreAdapter.selectItemById(
                        type
                );

                genreAdapter.setOnItemClickListener((view, position) -> {
                    Genre genre = genreAdapter.getSelectedGenre();
                    if (genre == null) {
                        return;
                    }

                    page = 1;


                    if (novels != null) novels.clear();

                    setTitle(genre.getName());
                    textView.setText(genre.getName());

                    GENRE_SELECTED = genre.getId();

                    novelRecyclerView.removeAllViews();
                    novelRecyclerView.setAdapter(null);

                    if (novelAdapter != null) novelAdapter.notifyDataSetChanged();

                    loadNovels();

                    novelRecyclerView.setAdapter(novelAdapter);
                });

                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<List<Genre>> call, @NonNull Throwable t) {
                isLoading = false;
            }
        });
    }

    private void loadNovels() {
        Intent intent = getIntent();
        String type = GENRE_SELECTED != null ? GENRE_SELECTED : intent.getStringExtra("type");

        Call<List<Novel>> call = api.getNovelsByGenreAndPage(type, page);

        call.enqueue(new Callback<List<Novel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Novel>> call, @NonNull Response<List<Novel>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    isLoading = false;
                    return;
                }

                List<Novel> newNovels = response.body();
                novels.addAll(newNovels);
                novelAdapter.addNovels(newNovels);

                isLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<List<Novel>> call, @NonNull Throwable t) {
                isLoading = false;
            }
        });
    }

    private void addSpaceBottomItem(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 32;
            }
        });
    }

    private void addSpaceRightItem(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.right = 32;
            }
        });
    }
}
