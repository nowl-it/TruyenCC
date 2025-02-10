package com.lukakuku.truyencc.adapters;

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
import com.lukakuku.truyencc.models.NovelInfo;
import com.lukakuku.truyencc.screens.ReadActivity;

import java.util.List;

public class NovelHistoryAdapter extends RecyclerView.Adapter<NovelHistoryAdapter.NovelViewHolder> {
    List<NovelInfo> novelList;
    Context context;
    List<String> chapters = new java.util.ArrayList<>();

    public NovelHistoryAdapter(Context context, List<NovelInfo> novelList) {
        this.context = context;
        this.novelList = novelList;
    }


    @NonNull
    @Override
    public NovelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_novel_item_list, parent, false);

        return new NovelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NovelViewHolder holder, int position) {
        NovelInfo novel = novelList.get(position);

        holder.title.setText(novel.getTitle());
        holder.chapter.setText(chapters.get(position));

        Glide.with(
                holder.image
        ).load(novel.getCoverImageUrl()).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadActivity.class);
            intent.putExtra("novel_id", novel.getNovelId());
            intent.putExtra("chapter_id", chapters.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.novelList == null ? 0 : this.novelList.size();
    }

    public void addNovel(NovelInfo novel, String chapter) {
        novelList.add(novel);
        chapters.add(chapter);
        notifyItemInserted(novelList.size() - 1);
    }

    public static class NovelViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView chapter;
        ImageView image;

        public NovelViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            chapter = itemView.findViewById(R.id.chapter);
            image = itemView.findViewById(R.id.image);
        }
    }
}
