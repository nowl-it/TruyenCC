package com.lukakuku.truyencc.fragments;

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
import com.lukakuku.truyencc.models.Genre;
import com.lukakuku.truyencc.models.Novel;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private int totalAPIRequests = 4;

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

        RecyclerView genresNovelReadingLayout = view.findViewById(R.id.genreReadingLayout);
        genresNovelReadingLayout.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        RecyclerView genreNovelsRecyclerView = view.findViewById(R.id.genreNovelReadingLayout);
        genreNovelsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        RecyclerView hotNovelsLayout = view.findViewById(R.id.hotNovelReadingLayout);
        hotNovelsLayout.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        RecyclerView newestNovelsLayout = view.findViewById(R.id.newNovelReadingLayout);
        newestNovelsLayout.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        RecyclerView completedNovelsLayout = view.findViewById(R.id.completedNovelReadingLayout);
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

    private void getNewestNovels(RecyclerView recyclerView) {
        TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Call<List<Novel>> call = truyenCCAPI.getNewestNovels();

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

    private void getHotNovels(RecyclerView recyclerView) {
        TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Call<List<Novel>> call = truyenCCAPI.getHotNovels();

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
            }

            @Override
            public void onFailure(@NonNull Call<List<Genre>> call, @NonNull Throwable throwable) {
                decrementAPIRequests();
            }
        });
    }

    private synchronized void decrementAPIRequests() {
        this.totalAPIRequests--;

        if (this.totalAPIRequests == 0) {
            LoadingScreenActivity.hideLoading(getContext());
        }
    }
}
