package com.lukakuku.truyencc.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class NovelAdapter extends RecyclerView.Adapter<NovelAdapter.NovelViewHolder> {
    List<Novel> novelList;
    Context context;

    private OnItemClickListener listener;

    public NovelAdapter(Context context, List<Novel> novelList) {
        this.context = context;
        this.novelList = novelList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NovelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_novel_item_list, parent, false);

        return new NovelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NovelViewHolder holder, int position) {
        Novel novel = novelList.get(position);

        holder.title.setText(novel.getTitle());
        Glide.with(
                holder.image
        ).load(novel.getCoverImgUrl()).into(holder.image);

        Log.d("WTF", "onBindViewHolder: ");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(novel);
            } else {
                Intent intent = new Intent(context, NovelActivity.class);
                intent.putExtra("novel_id", novel.getNovelId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.novelList == null ? 0 : this.novelList.size();
    }

    public void addNovels(List<Novel> novels) {
        if (this.novelList == null) {
            this.novelList = novels;
            notifyDataSetChanged();
        } else {
            int startPosition = this.novelList.size();
            this.novelList.addAll(novels);
            notifyItemRangeInserted(startPosition, novels.size());
        }
    }

    public void addNovel(Novel novel) {
        if (this.novelList == null) {
            this.novelList = List.of(novel);
            notifyDataSetChanged();
        } else {
            this.novelList.add(novel);
            notifyItemInserted(this.novelList.size() - 1);
        }
    }

    public Novel getNovel(int position) {
        return novelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(Novel novel);
    }

    public static class NovelViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public NovelViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
        }
    }
}
