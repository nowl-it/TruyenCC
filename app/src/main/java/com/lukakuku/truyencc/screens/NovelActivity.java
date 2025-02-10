package com.lukakuku.truyencc.screens;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.lukakuku.truyencc.LoadingScreenActivity;
import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.RetrofitClient;
import com.lukakuku.truyencc.adapters.ChapterAdapter;
import com.lukakuku.truyencc.adapters.GenreAdapter;
import com.lukakuku.truyencc.models.Bookmark;
import com.lukakuku.truyencc.models.Chapter;
import com.lukakuku.truyencc.models.Genre;
import com.lukakuku.truyencc.models.NovelChapter;
import com.lukakuku.truyencc.models.NovelInfo;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class NovelActivity extends AppCompatActivity {
    TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);
    private List<Chapter> chapterList;
    private int CHAPTER_PAGE = 1;
    private String NOVEL_ID;

    private ChapterAdapter chapterAdapter;

    private int TOTAL_API_CALLS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_novel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoadingScreenActivity.showLoading(this);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            CHAPTER_PAGE = 1;
            if (chapterList != null) chapterList.clear();
            loadChapters();
            swipeRefreshLayout.setRefreshing(false);
        });

        ImageButton backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        NOVEL_ID = intent.getStringExtra("novel_id");

        Bookmark bookmark = new Bookmark();
        bookmark.loadFromPreferences(this);

        ImageButton bookmarkButton = findViewById(R.id.btn_bookmark);

        if (bookmark.isBookmarked(NOVEL_ID)) {
            bookmarkButton.setImageResource(R.drawable.ic_fas_bookmark);
        } else {
            bookmarkButton.setImageResource(R.drawable.ic_far_bookmark);
        }

        bookmarkButton.setOnClickListener(v -> {
            if (bookmark.isBookmarked(NOVEL_ID)) {
                bookmark.removeBookmark(NOVEL_ID);
                bookmarkButton.setImageResource(R.drawable.ic_far_bookmark);
            } else {
                bookmark.addBookmark(NOVEL_ID);
                bookmarkButton.setImageResource(R.drawable.ic_fas_bookmark);
            }
            bookmark.saveToPreferences(this);
        });

        RecyclerView chaptersRecyclerView = findViewById(R.id.chapter_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        chaptersRecyclerView.setLayoutManager(layoutManager);

        chapterList = new ArrayList<>();

        chapterAdapter = new ChapterAdapter(this, chapterList, NOVEL_ID);
        chaptersRecyclerView.setAdapter(chapterAdapter);

        Button seeMoreButton = findViewById(R.id.read_more_btn);

        seeMoreButton.setOnClickListener(v -> {
            CHAPTER_PAGE++;
            loadChapters();
        });


        Call<NovelInfo> call = truyenCCAPI.getNovelById(NOVEL_ID);
        call.enqueue(NovelCallback(this));
    }

    private Callback<NovelInfo> NovelCallback(NovelActivity novelActivity) {
        return new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<NovelInfo> call, @NonNull retrofit2.Response<NovelInfo> response) {
                NovelInfo novelInfo = response.body();
                if (novelInfo != null) {
                    setTitle(novelInfo.getTitle());

                    TextView novelTitle = findViewById(R.id.title);
                    novelTitle.setText(novelInfo.getTitle());

                    ImageView novelCover = findViewById(R.id.img_novel);
                    Glide.with(novelActivity).load(novelInfo.getCoverImageUrl()).into(novelCover);

                    TextView novelName = findViewById(R.id.name_novel);
                    novelName.setText(novelInfo.getTitle());

                    TextView novelAuthor = findViewById(R.id.author);
                    novelAuthor.setText(novelInfo.getAuthor().getName());

                    TextView novelCre = findViewById(R.id.cre);
                    novelCre.setText(novelInfo.getCre());

                    TextView novelStatus = findViewById(R.id.status);
                    novelStatus.setText(novelInfo.getStatus());

                    loadGenres(novelInfo.getGenres());

                    TextView novelDescription = findViewById(R.id.novel_description);
                    novelDescription.setText(
                            Html.fromHtml(novelInfo.getDescription(), Html.FROM_HTML_MODE_COMPACT)
                    );

                    loadChapters();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NovelInfo> call, @NonNull Throwable t) {
                Log.d("ERROR", "onFailure: " + t.getMessage());
            }
        };
    }

    private void loadGenres(List<Genre> genres) {
        RecyclerView genresRecyclerView = findViewById(R.id.genre_list);
        GenreAdapter genreAdapter = new GenreAdapter(this, genres);

        genresRecyclerView.setAdapter(genreAdapter);
        addSpaceRightItem(genresRecyclerView);

        genreAdapter.setOnItemClickListener((view, position) -> {
            Genre genre = genres.get(position);
            Intent intent = new Intent(this, SeeAllByGenreActivity.class);
            intent.putExtra("title", genre.getName());
            intent.putExtra("type", genre.getId());
            startActivity(intent);
        });

        decrementApiCalls();
    }

    private void loadChapters() {
        Log.d("NOVEL + CHAPTER", "loadChapters: " + NOVEL_ID + " " + CHAPTER_PAGE);
        Call<NovelChapter> call = truyenCCAPI.getChaptersByNovelIdAndPage(NOVEL_ID, CHAPTER_PAGE);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<NovelChapter> call, @NonNull retrofit2.Response<NovelChapter> response) {
                NovelChapter novelChapters = response.body();

                if (novelChapters != null) {
                    List<Chapter> chapters = novelChapters.getChapters();
                    chapterList.addAll(chapters);
                    chapterAdapter.notifyDataSetChanged();

                    Button readButton = findViewById(R.id.btn_read);

                    if (!chapterList.isEmpty()) {
                        readButton.setVisibility(View.VISIBLE);
                        readButton.setText(chapterList.get(0).getTitle());

                        readButton.setOnClickListener(v -> {
                            Intent intent = new Intent(NovelActivity.this, ReadActivity.class);
                            intent.putExtra("novel_id", NOVEL_ID);
                            intent.putExtra("chapter_id", chapterList.get(0).getId());
                            startActivity(intent);
                        });
                    } else {
                        readButton.setVisibility(View.GONE);
                    }

                    decrementApiCalls();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NovelChapter> call, @NonNull Throwable t) {
                Log.d("ERROR", "onFailure: " + t.getMessage());
            }
        });
    }

    private void addSpaceRightItem(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(
                new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        outRect.right = 16;
                    }
                }
        );
    }

    private void decrementApiCalls() {
        TOTAL_API_CALLS--;
        if (TOTAL_API_CALLS == 0) {
            LoadingScreenActivity.hideLoading(this);
        }
    }
}
