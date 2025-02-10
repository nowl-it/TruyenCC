package com.lukakuku.truyencc.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lukakuku.truyencc.fragments.BookmarkFragment;
import com.lukakuku.truyencc.fragments.HistoryFragment;
import com.lukakuku.truyencc.fragments.HomeFragment;
import com.lukakuku.truyencc.fragments.RefreshableFragment;

public class VPAdapter extends FragmentStateAdapter {
    private final Fragment[] fragments = new Fragment[]{
            new BookmarkFragment(),
            new HomeFragment(),
            new HistoryFragment()
    };

    public VPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int id) {
        return fragments[id];
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void refresh(
            int position
    ) {
        Fragment fragment = fragments[position];
        if (fragment instanceof RefreshableFragment) {
            ((RefreshableFragment) fragment).refresh();
        }
    }
}
