package me.bakumon.ugank.module.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页 ViewPager 适配器
 *
 * @author bakumon https://bakumon.me
 * @date 2016/12/6
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {
    private String[] title;
    private List<Fragment> mFragments = new ArrayList<>();

    public HomeViewPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        title = titles;
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}
