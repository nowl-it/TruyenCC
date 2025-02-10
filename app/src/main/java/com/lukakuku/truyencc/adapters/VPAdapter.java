package com.lukakuku.truyencc.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lukakuku.truyencc.fragments.BookmarkFragment;
import com.lukakuku.truyencc.fragments.HistoryFragment;
import com.lukakuku.truyencc.fragments.HomeFragment;

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
            case 1:
                return new HomeFragment();
            case 2:
                return new HistoryFragment();
            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
