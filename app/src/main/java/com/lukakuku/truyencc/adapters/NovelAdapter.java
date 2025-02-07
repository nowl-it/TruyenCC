package com.lukakuku.truyencc.adapters;

import android.content.Context;
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

import java.util.List;

public class NovelAdapter extends RecyclerView.Adapter<NovelAdapter.NovelViewHolder> {
    List<Novel> novelList;
    Context context;

    public NovelAdapter(Context context, List<Novel> novelList) {
        this.context = context;
        this.novelList = novelList;
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
    }

    @Override
    public int getItemCount() {
        return this.novelList == null ? 0 : this.novelList.size();
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
