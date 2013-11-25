package com.msns.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msns.R;

public class OtherSparksFragment extends Fragment {
	
	public OtherSparksFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_other_profile_sparks, 
				container, false);
		
		return rootView;
	}
}
