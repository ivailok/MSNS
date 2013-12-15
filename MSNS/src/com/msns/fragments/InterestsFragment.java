package com.msns.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.msns.R;
import com.msns.models.Interest;
import com.msns.widgets.InterestsAdapter;

public class InterestsFragment extends Fragment {
	
	private ArrayList<Interest> mInterests;
	
	private LinearLayout mProfileInterestsStatus;
	private LinearLayout mProfileInterestsBasicView;
	private LinearLayout mProfileNoInterests;
	private LinearLayout mProfileInterestsForm;
	private ListView mInterestsContainer;
	private Spinner mInterestSpinner;
	private Button mAddButton;
	
	
	public OnClickListener addButtonOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String interest = String.valueOf(getmInterestSpinner().getSelectedItem());
			addInterest(interest);
		}
	};
	
	
	public LinearLayout getmProfileInterestsStatus() {
		if (mProfileInterestsStatus == null) {
			mProfileInterestsStatus = (LinearLayout) getActivity().findViewById(R.id.myProfileInterestsStatus);
		}
		return mProfileInterestsStatus;
	}
	
	public LinearLayout getmProfileInterestsBasicView() {
		if (mProfileInterestsBasicView == null) {
			mProfileInterestsBasicView = (LinearLayout) getActivity().findViewById(R.id.myProfileInterestsBasicView);
		}
		return mProfileInterestsBasicView;
	}
	
	public LinearLayout getmProfileNoInterests() {
		if (mProfileNoInterests == null) {
			mProfileNoInterests = (LinearLayout) getActivity().findViewById(R.id.myProfileNoInterests);
		}
		return mProfileNoInterests;
	}
	
	public LinearLayout getmProfileInterestsForm() {
		if (mProfileInterestsForm == null) {
			mProfileInterestsForm = (LinearLayout) getActivity().findViewById(R.id.myProfileInterestsForm);
		}
		return mProfileInterestsForm;
	}
	
	public ListView getmInterestsContainer() {
		if (mInterestsContainer == null) {
			mInterestsContainer = (ListView) getActivity().findViewById(R.id.interestsContainer);
		}
		return mInterestsContainer;
	}
	
	public Spinner getmInterestSpinner() {
		if (mInterestSpinner == null) {
			mInterestSpinner = (Spinner) getActivity().findViewById(R.id.interestSelector);
		}
		return mInterestSpinner;
	}
	
	public Button getmAddButton() {
		if (mAddButton == null) {
			mAddButton = (Button) getActivity().findViewById(R.id.addInterestButton);
		}
		return mAddButton;
	}
	
	
	@SuppressWarnings("unchecked")
	public void addInterest(String item) {
		final Interest interest = new Interest();
		interest.Key = item;
		if (!mInterests.contains(interest)) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        	String baseUrl = prefs.getString("EXTERNAL_SERVICES_BASE_URL", "");
            String addInterestUrl = baseUrl + "users/addinterest?interestKey=" + interest.Key.replace(" ", "%20");
            String userID = prefs.getString("USER_ID", "");
            Header[] headers = {
            	new BasicHeader("Content-Type", "application/json"),
    	        new BasicHeader("Accept", "application/json"),
    	        new BasicHeader("X-userID", userID)
            };
			
            final FragmentActivity currentActitivity = this.getActivity();
			AsyncHttpClient client = new AsyncHttpClient();
            client.put(getActivity(), addInterestUrl, headers, null, "application/json", new AsyncHttpResponseHandler() {
				
            	@Override
            	public void onFailure(int statusCode, Throwable error,
            			String content) {
            		Toast.makeText(getActivity(), "Can't update list of interests!", Toast.LENGTH_SHORT).show();
            	}
            	
            	@Override
            	public void onSuccess(int statusCode, String content) {
            		if (statusCode == HttpStatus.SC_OK) {
            			mInterests.add(interest);
            			
            			if (mInterests.size() == 1)
            			{
            				InterestsAdapter adapter = new InterestsAdapter(getActivity(), R.layout.interests_list_item, mInterests);
            	        	getmInterestsContainer().setAdapter(adapter);
            				showProgress(false, true);
            			}
            			
            			((ArrayAdapter<Interest>) getmInterestsContainer().getAdapter()).notifyDataSetChanged();
            		}
            		else {
            			Toast.makeText(getActivity(), "Can't update list of interests!", Toast.LENGTH_SHORT).show();
            		}
            	}
			});
		}
		else {
			Toast.makeText(getActivity(), "Already on the list!", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void setData(ArrayList<Interest> data, boolean activityCreating) {
		mInterests = data;
		
		if (getActivity() != null && activityCreating == false) {
			LinearLayout baseView = getmProfileInterestsBasicView();
			baseView.setVisibility(View.VISIBLE);
			
			buildInterestSpinnerList();
			
			Button addButton = getmAddButton();
			addButton.setOnClickListener(addButtonOnClickListener);
			
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
	
	public InterestsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile_interests, 
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
	
	private void buildInterestSpinnerList() {
		List<String> interestList = new ArrayList<String>();
		interestList.add("Android development");
		interestList.add("Football");
		interestList.add("Scuba-diving");
		interestList.add("Reading");
		interestList.add("Watching movies");
		interestList.add("Traveling");
		
		ArrayAdapter<String> interestDataAdapter = new ArrayAdapter<String>(this.getActivity(),
			android.R.layout.simple_spinner_item, interestList);
		interestDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.getmInterestSpinner().setAdapter(interestDataAdapter);
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
		final View unloadedInterestsView = hasInterests ? profileNoInterests : profileInterestsForm;
		
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

			unloadedInterestsView.setVisibility(View.GONE);
			unloadedInterestsView.animate().setDuration(shortAnimTime)
					.alpha(isLoading ? 0 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							unloadedInterestsView.setVisibility(isLoading ? View.GONE
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
