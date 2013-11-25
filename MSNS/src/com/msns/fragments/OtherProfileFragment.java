package com.msns.fragments;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msns.R;
import com.msns.models.ProfileModel;
import com.msns.services.DownloadImageTask;

public class OtherProfileFragment extends Fragment {
	protected ProfileModel mData; // here your asynchronously loaded data

	private LinearLayout mProfileStatus;
	private LinearLayout mProfileForm;
	private TextView mNicknameField;
	private TextView mGenderField;
	private TextView mAgeField;
	private TextView mMeetsField;
	private ImageView mProfileImage;
	
	
	public ImageView getmProfileImage() {
		if (mProfileImage == null) {
			mProfileImage = (ImageView) getActivity().findViewById(R.id.otherProfileImage);
		}
		return mProfileImage;
	}
	
	public TextView getmNicknameField() {
		if (mNicknameField == null) {
			mNicknameField = (TextView) getActivity().findViewById(R.id.otherProfileCurrentNickname);
		}
		return mNicknameField;
	}
	
	public LinearLayout getmProfileStatus() {
		if (mProfileStatus == null) {
			mProfileStatus = (LinearLayout) getActivity().findViewById(R.id.otherProfileStatus);
		}
		return mProfileStatus;
	}
	
	public LinearLayout getmProfileForm() {
		if (mProfileForm == null) {
			mProfileForm = (LinearLayout) getActivity().findViewById(R.id.otherProfileForm);
		}
		return mProfileForm;
	}
	
	public TextView getmGenderField() {
		if (mGenderField == null) {
			mGenderField = (TextView) getActivity().findViewById(R.id.otherProfileGenderValue);
		}
		return mGenderField;
	}
	
	public TextView getmAgeField() {
		if (mAgeField == null) {
			mAgeField = (TextView) getActivity().findViewById(R.id.otherProfileAgeValue);
		}
		return mAgeField;
	}
	
	public TextView getmMeetsField() {
		if (mMeetsField == null) {
			mMeetsField = (TextView) getActivity().findViewById(R.id.otherProfileMeetsValue);
		}
		return mMeetsField;
	}
	
    public void setData(ProfileModel data, boolean activityCreating) {
        this.mData = data;
        
        if (getActivity() != null && activityCreating == false) {
        	TextView nicknameField = getmNicknameField();
	        nicknameField.setText(data.nickname);
	        
	        TextView ageField = getmAgeField();
	        ageField.setText(String.valueOf(data.age));
	        
	        TextView genderField = getmGenderField();
	        genderField.setText(data.gender);
	        
	        TextView meetsField = getmMeetsField();
	        meetsField.setText(String.valueOf(data.meets));
	        
	        ImageView profileImage = getmProfileImage();
	        if (data.profileImage != null) {
	        	DownloadImageTask downloadImageTask = new DownloadImageTask(getActivity(), profileImage);
	        	downloadImageTask.execute(data.profileImage);
	        }
	        
	        showProgress(false);
        }
    }
	
	public OtherProfileFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_other_profile_details, 
				container, false);
    	
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		FragmentActivity act = getActivity();
		if (mData != null && act != null) {
			setData(mData, false);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		final View profileStatus = getmProfileStatus();
		final View profileForm = getmProfileForm();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			profileStatus.setVisibility(View.VISIBLE);
			profileStatus.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							profileStatus.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			profileForm.setVisibility(View.VISIBLE);
			profileForm.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							profileForm.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			profileStatus.setVisibility(show ? View.VISIBLE : View.GONE);
			profileForm.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
