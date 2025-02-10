package com.lukakuku.truyencc.screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lukakuku.truyencc.LoadingScreenActivity;
import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.RetrofitClient;
import com.lukakuku.truyencc.models.ChapterContent;
import com.lukakuku.truyencc.models.History;
import com.lukakuku.truyencc.models.TruyenCCAPI;

import retrofit2.Call;

public class ReadActivity extends AppCompatActivity {
    private final TruyenCCAPI truyenCCAPI = RetrofitClient.getClient().create(TruyenCCAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoadingScreenActivity.showLoading(this);

        ImageButton backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(v -> finish());

        ImageButton prevBtn = findViewById(R.id.btn_prev_chapter);
        ImageButton nextBtn = findViewById(R.id.btn_next_chapter);

        TextView title = findViewById(R.id.title);
        TextView chapter = findViewById(R.id.select_chapter);

        TextView content = findViewById(R.id.content);

        Intent intent = getIntent();
        String novelId = intent.getStringExtra("novel_id");
        String chapterId = intent.getStringExtra("chapter_id");

        History history = new History();
        history.loadFromPreferences(this);

        history.addToHistory(novelId, chapterId);

        history.saveToPreferences(this);

        Log.d("DATA", "onCreate: " + novelId + " - " + chapterId);

        Call<ChapterContent> call = truyenCCAPI.getChapterContent(novelId, chapterId);

        Context context = this;

        call.enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<ChapterContent> call, @NonNull retrofit2.Response<ChapterContent> response) {
                ChapterContent chapterContent = response.body();
                if (chapterContent != null) {
                    setTitle(chapterContent.getTitle());

                    title.setText(chapterContent.getTitle().split(" - ")[0]);
                    chapter.setText(chapterContent.getTitle().split(" - ")[1]);

                    Log.d("PREV", "onResponse: " + chapterContent.getPrevChapter());
                    Log.d("NEXT", "onResponse: " + chapterContent.getNextChapter());

                    if (chapterContent.getPrevChapter() > 0) {
                        prevBtn.setOnClickListener(v -> {
                            Intent intent = new Intent(context, ReadActivity.class);
                            intent.putExtra("novel_id", novelId);
                            intent.putExtra("chapter_id", chapterContent.toString(chapterContent.getPrevChapter()));
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        prevBtn.setEnabled(false);
                    }

                    if (chapterContent.getNextChapter() > 0) {
                        nextBtn.setOnClickListener(v -> {
                            Intent intent = new Intent(context, ReadActivity.class);
                            intent.putExtra("novel_id", novelId);
                            intent.putExtra("chapter_id", chapterContent.toString(chapterContent.getNextChapter()));
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        nextBtn.setEnabled(false);
                    }

                    content.setText(
                            Html.fromHtml(
                                    chapterContent.getContent(),
                                    Html.FROM_HTML_MODE_COMPACT
                            )
                    );

                    LoadingScreenActivity.hideLoading(context);
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<ChapterContent> call, @NonNull Throwable t) {
                LoadingScreenActivity.hideLoading(context);
            }
        });
    }
}
