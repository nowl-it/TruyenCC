package com.lukakuku.truyencc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.models.Chapter;
import com.lukakuku.truyencc.screens.ReadActivity;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private final List<Chapter> chapters;
    private final Context context;

    private final String novel_id;

    public ChapterAdapter(Context context, List<Chapter> chapters, String novel_id) {
        this.context = context;
        this.chapters = chapters;
        this.novel_id = novel_id;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_item_list, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        holder.title.setText(chapter.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadActivity.class);
            intent.putExtra("novel_id", novel_id);
            intent.putExtra("chapter_id", chapter.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
