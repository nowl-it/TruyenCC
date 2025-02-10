package com.lukakuku.truyencc.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.models.Novel;
import com.lukakuku.truyencc.screens.NovelActivity;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.NovelViewHolder> {
    List<Novel> novelList;
    Context context;

    public SearchAdapter(Context context, List<Novel> novelList) {
        this.context = context;
        this.novelList = novelList;
    }


    @NonNull
    @Override
    public NovelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item_list, parent, false);

        return new NovelViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NovelViewHolder holder, int position) {
        Novel novel = novelList.get(position);

        holder.title.setText(novel.getTitle());

        holder.chapter.setText(
                "Chapter " + novel.getNewestChapter()
        );

        Glide.with(
                holder.image
        ).load(novel.getCoverImgUrl()).into(holder.image);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NovelActivity.class);
            intent.putExtra("novel_id", novel.getNovelId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.novelList == null ? 0 : this.novelList.size();
    }

    public static class NovelViewHolder extends RecyclerView.ViewHolder {
        TextView title, chapter;
        ImageView image;

        public NovelViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            chapter = itemView.findViewById(R.id.chapter);
            image = itemView.findViewById(R.id.image);
        }
    }
}
