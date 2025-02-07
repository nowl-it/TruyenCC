package com.lukakuku.truyencc.models;

import androidx.annotation.NonNull;

public class Genre {
    private String id, name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Genre{id='" + id + "', name='" + name + "'}";
    }
}
