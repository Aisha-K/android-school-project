package com.example.angemichaella.homeservices;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> tabTitles = new ArrayList<String>();
    private List<Fragment> tabFragments = new ArrayList<Fragment>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    public void addFragment(Fragment fragment, String fragmentTitle){
        tabTitles.add(fragmentTitle);
        tabFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int pos){
        return tabTitles.get(pos);
    }

    @Override
    public int getCount() {
        return tabFragments.size();
    }

}