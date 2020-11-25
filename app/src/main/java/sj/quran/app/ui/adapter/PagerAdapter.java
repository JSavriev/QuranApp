package sj.quran.app.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @NotNull
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String str) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(str);
    }

    public CharSequence getPageTitle(int i) {
        return mFragmentTitleList.get(i);
    }
}
