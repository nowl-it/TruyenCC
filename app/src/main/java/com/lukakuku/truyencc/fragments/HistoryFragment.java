package com.lukakuku.truyencc.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
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
import com.lukakuku.truyencc.adapters.NovelHistoryAdapter;
import com.lukakuku.truyencc.models.History;
import com.lukakuku.truyencc.models.NovelInfo;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class HistoryFragment extends Fragment {
    TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView historyRecyclerView = view.findViewById(R.id.recyclerView);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);

        historyRecyclerView.setHasFixedSize(true);

        historyRecyclerView.setLayoutManager(layoutManager);

        addSpaceItem(historyRecyclerView);

        NovelHistoryAdapter novelAdapter = new NovelHistoryAdapter(getContext(), new ArrayList<>());
        historyRecyclerView.setAdapter(novelAdapter);

        loadHistory(novelAdapter, view);

        return view;
    }

    private void loadHistory(NovelHistoryAdapter novelAdapter, View view) {
        History history = new History();
        history.loadFromPreferences(view.getContext());

        List<String[]> historyList = history.getHistory();

        for (String[] historyItem : historyList) {
            Log.d("HISTORY", "loadHistory: " + historyItem[0] + " " + historyItem[1]);

            loadNovel(historyItem[0], historyItem[1], novelAdapter);
        }
    }

    private void loadNovel(String novelId, String chapter, NovelHistoryAdapter novelAdapter) {
        Call<NovelInfo> call = truyenCCAPI.getNovelById(String.valueOf(novelId));

        call.enqueue(new Callback<NovelInfo>() {
            @Override
            public void onResponse(@NonNull Call<NovelInfo> call, @NonNull retrofit2.Response<NovelInfo> response) {
                NovelInfo novelInfo = response.body();
                if (novelInfo != null) {
                    novelAdapter.addNovel(novelInfo, chapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NovelInfo> call, @NonNull Throwable t) {
                Log.e("HistoryFragment", "Failed to load novel: " + t.getMessage());
            }
        });
    }

    private void addSpaceItem(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(
                new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        outRect.bottom = 16;
                    }
                }
        );
    }
}
