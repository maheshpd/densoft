package com.densoftinfotech.densoftpayroll.adapter;

import com.densoftinfotech.densoftpayroll.fragments.MyLeaveFragment;
import com.densoftinfotech.densoftpayroll.fragments.MyPlannerFragment;
import com.densoftinfotech.densoftpayroll.fragments.MyTeamFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class PlannerAdapter extends FragmentStatePagerAdapter {
    public PlannerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MyLeaveFragment.newInstance("1", "");

            case 1:
                return MyPlannerFragment.newInstance("2", "");

            case 2:
                return MyTeamFragment.newInstance("3", "");

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
