package com.lukakuku.truyencc.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class History {
    private static final String PREFERENCES_NAME = "app_preferences";
    private static final String HISTORY_KEY = "history";

    // Lưu lịch sử dưới dạng danh sách chứa các mảng 2 phần tử [novelId, chapterId]
    private List<String[]> history;

    public History() {
        this.history = new ArrayList<>();
    }

    /**
     * Tải danh sách lịch sử từ SharedPreferences.
     *
     * @param context Context của ứng dụng.
     */
    public void loadFromPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String historyJson = preferences.getString(HISTORY_KEY, "[]");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<String[]>>() {
        }.getType();
        this.history = gson.fromJson(historyJson, listType);
    }

    /**
     * Lưu danh sách lịch sử vào SharedPreferences.
     *
     * @param context Context của ứng dụng.
     */
    public void saveToPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String historyJson = gson.toJson(this.history);

        editor.putString(HISTORY_KEY, historyJson);
        editor.apply();
    }

    /**
     * Lấy danh sách lịch sử.
     *
     * @return Danh sách lịch sử.
     */
    public List<String[]> getHistory() {
        return history;
    }

    /**
     * Thêm một mục vào danh sách lịch sử.
     *
     * @param novelId   ID của truyện.
     * @param chapterId ID của chương.
     */
    public void addToHistory(String novelId, String chapterId) {
        history.add(0, new String[]{novelId, chapterId});
    }

    /**
     * Xóa một mục khỏi danh sách lịch sử.
     *
     * @param novelId ID của truyện cần xóa.
     */
    public void removeFromHistory(String novelId) {
        history.removeIf(item -> item[0].equals(novelId));
    }

    /**
     * Lấy chapterId của một novelId.
     *
     * @param novelId ID của truyện.
     * @return ID của chương, hoặc null nếu không tìm thấy.
     */
    public String getChapterId(String novelId) {
        for (String[] item : history) {
            if (item[0].equals(novelId)) {
                return item[1];
            }
        }
        return null;
    }

    /**
     * Xóa toàn bộ lịch sử.
     */
    public void clearHistory() {
        history.clear();
    }
}
