package com.lukakuku.truyencc.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Bookmark {
    private static final String PREF_NAME = "bookmark_prefs";
    private static final String KEY_BOOKMARK = "bookmarks";

    private List<String> bookmarkList;

    // Constructor
    public Bookmark() {
        this.bookmarkList = new ArrayList<>();
    }

    // Thêm bookmark
    public void addBookmark(String id) {
        if (!bookmarkList.contains(id)) {
            bookmarkList.add(id);
        }
    }

    // Xóa bookmark
    public void removeBookmark(String id) {
        bookmarkList.remove(id);
    }

    // Lấy danh sách bookmark
    public List<String> getBookmarks() {
        return new ArrayList<>(bookmarkList);
    }

    // Kiểm tra bookmark tồn tại
    public boolean isBookmarked(String id) {
        return bookmarkList.contains(id);
    }

    // Lưu bookmark vào SharedPreferences
    public void saveToPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(bookmarkList);
        editor.putString(KEY_BOOKMARK, json);
        editor.apply();
    }

    // Tải bookmark từ SharedPreferences
    public void loadFromPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String json = prefs.getString(KEY_BOOKMARK, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            bookmarkList = gson.fromJson(json, type);
        } else {
            bookmarkList = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "bookmarkList=" + bookmarkList +
                '}';
    }
}
