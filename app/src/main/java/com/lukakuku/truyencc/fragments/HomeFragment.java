package com.lukakuku.truyencc.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lukakuku.truyencc.LoadingScreenActivity;
import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.RetrofitClient;
import com.lukakuku.truyencc.adapters.GenreAdapter;
import com.lukakuku.truyencc.adapters.NovelAdapter;
import com.lukakuku.truyencc.adapters.SearchAdapter;
import com.lukakuku.truyencc.models.Genre;
import com.lukakuku.truyencc.models.Novel;
import com.lukakuku.truyencc.models.TruyenCCAPI;
import com.lukakuku.truyencc.screens.SeeAllActivity;
import com.lukakuku.truyencc.screens.SeeAllByGenreActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

enum SeeAllType {
    HOT_NOVELS,
    NEWEST_NOVELS,
    COMPLETED_NOVELS;

    @NonNull
    public String toString() {
        switch (this) {
            case HOT_NOVELS:
                return "Truyện hot";
            case NEWEST_NOVELS:
                return "Truyện mới cập nhật";
            case COMPLETED_NOVELS:
                return "Truyện đã hoàn thành";
            default:
                return "";
        }
    }

    public int toAPI() {
        switch (this) {
            case HOT_NOVELS:
                return 0;
            case NEWEST_NOVELS:
                return 1;
            case COMPLETED_NOVELS:
                return 2;
            default:
                return -1;
        }
    }
}

public class HomeFragment extends Fragment implements RefreshableFragment {
    private int TOTAL_API_CALLS = 4;
    private RecyclerView genresNovelReadingLayout, genreNovelsRecyclerView, hotNovelsLayout, newestNovelsLayout, completedNovelsLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        SearchView searchView = view.findViewById(R.id.search_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        searchView.clearFocus();

        genresNovelReadingLayout = view.findViewById(R.id.genreReadingLayout);
        genresNovelReadingLayout.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        genreNovelsRecyclerView = view.findViewById(R.id.genreNovelReadingLayout);
        genreNovelsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        hotNovelsLayout = view.findViewById(R.id.hotNovelReadingLayout);
        hotNovelsLayout.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        newestNovelsLayout = view.findViewById(R.id.newNovelReadingLayout);
        newestNovelsLayout.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        completedNovelsLayout = view.findViewById(R.id.completedNovelReadingLayout);
        completedNovelsLayout.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        this.getGenres(
                genresNovelReadingLayout,
                genreNovelsRecyclerView
        );

        this.getHotNovels(
                hotNovelsLayout
        );

        this.getNewestNovels(
                newestNovelsLayout
        );

        this.getCompletedNovels(
                completedNovelsLayout
        );

        seeAllBtnClick(view.findViewById(R.id.see_all_hot_novel), SeeAllType.HOT_NOVELS);
        seeAllBtnClick(view.findViewById(R.id.see_all_new_novel), SeeAllType.NEWEST_NOVELS);
        seeAllBtnClick(view.findViewById(R.id.see_all_completed_novel), SeeAllType.COMPLETED_NOVELS);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    recyclerView.setAdapter(null);
                } else {
                    searchNovelRender(newText, recyclerView);
                }
                return false;
            }
        });
    }

    private void addSpaceItem(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(
                new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        outRect.right = 16;
                    }
                }
        );
    }

    private void seeAllBtnClick(View view, SeeAllType type) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SeeAllActivity.class);
            intent.putExtra("title", type.toString());
            intent.putExtra("type", type.toAPI());
            startActivity(intent);
        });
    }

    private void seeAllBtnClick(View view, Genre type) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SeeAllByGenreActivity.class);
            intent.putExtra("title", type.getName());
            intent.putExtra("type", type.getId());
            startActivity(intent);
        });
    }

    private void getHotNovels(RecyclerView recyclerView) {
        TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Call<List<Novel>> call = truyenCCAPI.getHotNovels();

        recyclerView.removeAllViews();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Novel>> call, @NonNull Response<List<Novel>> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR FETCHING", "onResponse: ");
                    return;
                }
                List<Novel> novels = response.body();
                NovelAdapter novelAdapter = new NovelAdapter(getContext(), novels);

                recyclerView.setAdapter(novelAdapter);
                addSpaceItem(recyclerView);

                decrementAPIRequests();
            }

            @Override
            public void onFailure(@NonNull Call<List<Novel>> call, @NonNull Throwable throwable) {
                decrementAPIRequests();
            }
        });
    }

    private void getNewestNovels(RecyclerView recyclerView) {
        TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Call<List<Novel>> call = truyenCCAPI.getNewestNovels();

        recyclerView.removeAllViews();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Novel>> call, @NonNull Response<List<Novel>> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR FETCHING", "onResponse: ");
                    return;
                }
                List<Novel> novels = response.body();
                NovelAdapter novelAdapter = new NovelAdapter(getContext(), novels);

                recyclerView.setAdapter(novelAdapter);
                addSpaceItem(recyclerView);
                decrementAPIRequests();
            }

            @Override
            public void onFailure(@NonNull Call<List<Novel>> call, @NonNull Throwable throwable) {
                decrementAPIRequests();
            }
        });
    }

    private void getCompletedNovels(RecyclerView recyclerView) {
        TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Call<List<Novel>> call = truyenCCAPI.getCompletedNovels();

        recyclerView.removeAllViews();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Novel>> call, @NonNull Response<List<Novel>> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR FETCHING", "onResponse: ");
                    return;
                }
                List<Novel> novels = response.body();
                NovelAdapter novelAdapter = new NovelAdapter(getContext(), novels);
                recyclerView.setAdapter(novelAdapter);
                addSpaceItem(recyclerView);

                decrementAPIRequests();
            }

            @Override
            public void onFailure(@NonNull Call<List<Novel>> call, @NonNull Throwable throwable) {
                decrementAPIRequests();
            }
        });
    }

    private void getGenres(RecyclerView recyclerView, RecyclerView genreNovelRecyclerView) {
        TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Call<List<Genre>> call = truyenCCAPI.getGenres();

        recyclerView.removeAllViews();
        genreNovelRecyclerView.removeAllViews();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Genre>> call, @NonNull Response<List<Genre>> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR FETCHING", "onResponse: " + response.message());
                    return;
                }
                List<Genre> genres = response.body();
                GenreAdapter genreAdapter = new GenreAdapter(getContext(), genres, genreNovelRecyclerView);

                recyclerView.setAdapter(genreAdapter);

                addSpaceItem(recyclerView);
                addSpaceItem(genreNovelRecyclerView);

                if (genres != null) {
                    genreAdapter.selectItemByIndex(0);
                }

                decrementAPIRequests();

                if (getView() == null) {
                    return;
                }

                genreAdapter.setOnItemClickListener((view, position) -> {
                    genreAdapter.selectItemByIndex(position);
                    seeAllBtnClick(getView().findViewById(R.id.see_all_genre_novel), genreAdapter.getSelectedGenre());
                });

                seeAllBtnClick(getView().findViewById(R.id.see_all_genre_novel), genreAdapter.getSelectedGenre());
            }

            @Override
            public void onFailure(@NonNull Call<List<Genre>> call, @NonNull Throwable throwable) {
                decrementAPIRequests();
            }
        });
    }

    private synchronized void decrementAPIRequests() {
        this.TOTAL_API_CALLS--;

        if (this.TOTAL_API_CALLS == 0) {
            LoadingScreenActivity.hideLoading(getContext());
        }
    }

    private void searchNovelRender(String query, RecyclerView recyclerView) {
        TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Call<List<Novel>> call = truyenCCAPI.searchNovels(query);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Novel>> call, @NonNull Response<List<Novel>> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR FETCHING", "onResponse: ");
                    return;
                }
                List<Novel> novels = response.body();
                SearchAdapter novelAdapter = new SearchAdapter(getContext(), novels);

                recyclerView.setAdapter(novelAdapter);

                if (novels == null || novels.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Novel>> call, @NonNull Throwable throwable) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (TOTAL_API_CALLS == 0) {
            TOTAL_API_CALLS = 4;
        }
    }

    @Override
    public void refresh() {
        if (getView() == null) {
            return;
        }

        onResume();
    }
}
