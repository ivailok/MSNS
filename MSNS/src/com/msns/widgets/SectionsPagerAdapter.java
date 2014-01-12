package com.msns.widgets;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msns.models.FragmentDetails;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<FragmentDetails> mFragments;
	
	public SectionsPagerAdapter(FragmentManager fm, ArrayList<FragmentDetails> fragments) {
		super(fm);
		
		mFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = mFragments.get(position).getmActualFragment();
		return fragment;
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		String pageTitle = mFragments.get(position).getmPageTitle();
		return pageTitle;
	}
}
