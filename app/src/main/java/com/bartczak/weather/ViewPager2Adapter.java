package com.bartczak.weather;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPager2Adapter extends FragmentStateAdapter {

    private static final int ITEM_COUNT = 2;

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BasicWeatherFragment();
            case 1:
                return new AdvancedWeatherFragment();
            default:
                throw new IllegalArgumentException("Invalid fragment position " + position);
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
