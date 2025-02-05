package com.lukakuku.truyencc.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lukakuku.truyencc.R;
import com.lukakuku.truyencc.fragments.BookmarkFragment;
import com.lukakuku.truyencc.fragments.HistoryFragment;
import com.lukakuku.truyencc.fragments.HomeFragment;

enum Fragments {
    BOOKMARK(R.id.navigation_bookmark, 0),
    HOME(R.id.navigation_home, 1),
    HISTORY(R.id.navigation_history, 2);

    private final int id;
    private final int position;

    Fragments(int id, int position) {
        this.id = id;
        this.position = position;
    }

    public int findPosition(int id) {
        for (Fragments fragment : Fragments.values()) {
            if (fragment.id == id) {
                return fragment.position;
            }
        }
        return 0;
    }
}

public class VPAdapter extends FragmentStateAdapter {
    public VPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int id) {
        switch (id) {
            case 0:
                return new BookmarkFragment();
            case 2:
                return new HistoryFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
