package com.lukakuku.truyencc.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.RetrofitClient;
import com.lukakuku.truyencc.models.Genre;
import com.lukakuku.truyencc.models.Novel;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private final List<Genre> genreList;
    private final Context context;
    private RecyclerView renderContext = null;
    private int selectedPosition = -1;
    private OnItemClickListener onItemClickListener;

    public GenreAdapter(Context context, List<Genre> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    public GenreAdapter(Context context, List<Genre> genreList, RecyclerView renderContext) {
        this.context = context;
        this.genreList = genreList;
        this.renderContext = renderContext;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_genre_item_list, parent, false);

        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();
        Genre genre = genreList.get(currentPosition);

        holder.title.setText(genre.getName());

        if (selectedPosition == position) {
            holder.itemView.setBackground(ResourcesCompat.getDrawable(
                    context.getResources(),
                    R.drawable.rounded_corner_genre_list_pressed,
                    null
            ));
            render();
        } else {
            holder.itemView.setBackground(ResourcesCompat.getDrawable(
                    context.getResources(),
                    R.drawable.rounded_corner_genre_list_normal,
                    null
            ));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = currentPosition;

            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, currentPosition);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return genreList == null ? 0 : genreList.size();
    }

    public void selectItemByIndex(int index) {
        if (index >= 0 && index < genreList.size()) {
            this.selectedPosition = index;
            notifyItemChanged(selectedPosition);
        }
    }

    public void selectItemById(String id) {
        for (int i = 0; i < genreList.size(); i++) {
            if (Objects.equals(genreList.get(i).getId(), id)) {
                this.selectedPosition = i;
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }

    public Genre getSelectedGenre() {
        Log.d("SELECTED", "getSelectedGenre: " + genreList.get(selectedPosition));
        if (selectedPosition >= 0 && selectedPosition < genreList.size()) {
            return genreList.get(selectedPosition);
        }

        return null;
    }

    private void render() {
        if (renderContext == null) {
            return;
        }

        ProgressBar progressBar = ((Activity) context).findViewById(R.id.loading_bar);

        renderContext.setVisibility(View.GONE);
        renderContext.removeAllViews();
        renderContext.setAdapter(null);

        progressBar.setVisibility(View.VISIBLE);

        TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

        Call<List<Novel>> call = truyenCCAPI.getNovelsByGenre(this.getSelectedGenre().getId());

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Novel>> call, @NonNull Response<List<Novel>> response) {
                if (!response.isSuccessful()) {
                    Log.d("ERROR FETCHING", "onResponse: ");
                }
                progressBar.setVisibility(View.GONE);
                renderContext.setVisibility(View.VISIBLE);

                List<Novel> novels = response.body();
                NovelAdapter novelAdapter = new NovelAdapter(context, novels);

                renderContext.setAdapter(novelAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Novel>> call, @NonNull Throwable throwable) {
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
        }
    }
}
