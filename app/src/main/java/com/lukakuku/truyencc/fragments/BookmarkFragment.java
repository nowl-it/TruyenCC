package com.lukakuku.truyencc.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.RetrofitClient;
import com.lukakuku.truyencc.adapters.NovelAdapter;
import com.lukakuku.truyencc.models.Bookmark;
import com.lukakuku.truyencc.models.Novel;
import com.lukakuku.truyencc.models.NovelInfo;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkFragment extends Fragment {
    private NovelAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBookmark);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        addSpaceItem(recyclerView);

        List<Novel> novels = new ArrayList<>();
        adapter = new NovelAdapter(this.getContext(), novels);

        recyclerView.setAdapter(adapter);

        TruyenCCAPI api = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Bookmark bookmark = new Bookmark();
        bookmark.loadFromPreferences(view.getContext());

        List<String> bookmarkList = bookmark.getBookmarks();
        for (String novelId : bookmarkList) {
            Call<NovelInfo> call = api.getNovelById(novelId);
            call.enqueue(new Callback<NovelInfo>() {
                @Override
                public void onResponse(@NonNull Call<NovelInfo> call, @NonNull Response<NovelInfo> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        return;
                    }
                    NovelInfo novelInfo = response.body();
                    Novel novel = new Novel(novelInfo.getNovelId(), novelInfo.getTitle(), novelInfo.getCoverImageUrl(), 0);

                    adapter.addNovel(novel);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(@NonNull Call<NovelInfo> call, @NonNull Throwable t) {
                }
            });
        }
    }

    private void addSpaceItem(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 32;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}