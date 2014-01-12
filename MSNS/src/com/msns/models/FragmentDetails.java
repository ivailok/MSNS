package com.msns.models;

import android.support.v4.app.Fragment;

public class FragmentDetails {
	private Fragment mActualFragment;
	private String mPageTitle;
	
	public Fragment getmActualFragment() {
		return mActualFragment;
	}

	public void setmActualFragment(Fragment mActualFragment) {
		this.mActualFragment = mActualFragment;
	}

	public String getmPageTitle() {
		return mPageTitle;
	}

	public void setmPageTitle(String mPageTitle) {
		this.mPageTitle = mPageTitle;
	}
	
	public FragmentDetails(Fragment fragment, String title)
	{
		setmActualFragment(fragment);
		setmPageTitle(title);
	}
}
