package com.msns.fragments;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.msns.R;
import com.msns.models.Interest;
import com.msns.widgets.InterestsAdapter;

public class OtherInterestsFragment extends Fragment {
private ArrayList<Interest> mInterests;
	
	private LinearLayout mProfileInterestsStatus;
	private LinearLayout mProfileNoInterests;
	private LinearLayout mProfileInterestsForm;
	private ListView mInterestsContainer;
	
	
	public LinearLayout getmProfileInterestsStatus() {
		if (mProfileInterestsStatus == null) {
			mProfileInterestsStatus = (LinearLayout) getActivity().findViewById(R.id.otherProfileInterestsStatus);
		}
		return mProfileInterestsStatus;
	}
	
	public LinearLayout getmProfileNoInterests() {
		if (mProfileNoInterests == null) {
			mProfileNoInterests = (LinearLayout) getActivity().findViewById(R.id.otherProfileNoInterests);
		}
		return mProfileNoInterests;
	}
	
	public LinearLayout getmProfileInterestsForm() {
		if (mProfileInterestsForm == null) {
			mProfileInterestsForm = (LinearLayout) getActivity().findViewById(R.id.otherProfileInterestsForm);
		}
		return mProfileInterestsForm;
	}
	
	public ListView getmInterestsContainer() {
		if (mInterestsContainer == null) {
			mInterestsContainer = (ListView) getActivity().findViewById(R.id.otherInterestsContainer);
		}
		return mInterestsContainer;
	}
	
	
	
	
	public void setData(ArrayList<Interest> data, boolean activityCreating) {
		mInterests = data;
		
		if (getActivity() != null && activityCreating == false) {
	        if (data.size() == 0) {
	        	showProgress(false, false);
	        }
	        else {
	        	ListView interestsContainer = getmInterestsContainer();
	        	InterestsAdapter adapter = new InterestsAdapter(getActivity(), R.layout.interests_list_item, data);
	        	interestsContainer.setAdapter(adapter);
	        	showProgress(false, true);
	        }
		}
    }
	
	public OtherInterestsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_other_profile_interests, 
				container, false);
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		FragmentActivity act = getActivity();
		if (mInterests != null && act != null) {
			setData(mInterests, false);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean isLoading, final boolean hasInterests) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		final View profileInterestsStatus = getmProfileInterestsStatus();
		final View profileNoInterests = getmProfileNoInterests();
		final View profileInterestsForm = getmProfileInterestsForm();
		
		final View loadedInterestsView = hasInterests ? profileInterestsForm : profileNoInterests;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			profileInterestsStatus.setVisibility(View.VISIBLE);
			profileInterestsStatus.animate().setDuration(shortAnimTime)
					.alpha(isLoading ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							profileInterestsStatus.setVisibility(isLoading ? View.VISIBLE
									: View.GONE);
						}
					});

			loadedInterestsView.setVisibility(View.VISIBLE);
			loadedInterestsView.animate().setDuration(shortAnimTime)
					.alpha(isLoading ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							loadedInterestsView.setVisibility(isLoading ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			profileInterestsStatus.setVisibility(isLoading ? View.VISIBLE : View.GONE);
			loadedInterestsView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
		}
	}
}
