package com.msns.fragments;

import java.io.IOException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.msns.R;
import com.msns.models.ProfileModel;
import com.msns.services.DownloadImageTask;
import com.msns.services.ImageHandler;
import com.msns.services.UploadImageTask;

public class ProfileFragment extends Fragment {
	
	protected ProfileModel mData; // here your asynchronously loaded data

	private LinearLayout mProfileStatus;
	private LinearLayout mProfileForm;
	private TextView mNicknameField;
	private TextView mGenderField;
	private TextView mAgeField;
	private TextView mMeetsField;
	private Button mTakePicButton;
	private Button mSelectImgButton;
	private ImageView mProfileImage;
	
	
	private OnClickListener takePicOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    startActivityForResult(takePictureIntent, 16);
		}
	};
	
	private OnClickListener selectImgOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(galleryIntent , 8);
		}
	};
	
	
	public ImageView getmProfileImage() {
		if (mProfileImage == null) {
			mProfileImage = (ImageView) getActivity().findViewById(R.id.myProfileImage);
		}
		return mProfileImage;
	}
	
	public Button getmTakePicButton() {
		if (mTakePicButton == null) {
			mTakePicButton = (Button) getActivity().findViewById(R.id.myProfileTakeNewPictureButton);
		}
		return mTakePicButton;
	}
	
	public Button getmSelectImgButton() {
		if (mSelectImgButton == null) {
			mSelectImgButton = (Button) getActivity().findViewById(R.id.myProfileSelectNewPictureButton);
		}
		return mSelectImgButton;
	}
	
	public TextView getmNicknameField() {
		if (mNicknameField == null) {
			mNicknameField = (TextView) getActivity().findViewById(R.id.myProfileCurrentNickname);
		}
		return mNicknameField;
	}
	
	public LinearLayout getmProfileStatus() {
		if (mProfileStatus == null) {
			mProfileStatus = (LinearLayout) getActivity().findViewById(R.id.myProfileStatus);
		}
		return mProfileStatus;
	}
	
	public LinearLayout getmProfileForm() {
		if (mProfileForm == null) {
			mProfileForm = (LinearLayout) getActivity().findViewById(R.id.myProfileForm);
		}
		return mProfileForm;
	}
	
	public TextView getmGenderField() {
		if (mGenderField == null) {
			mGenderField = (TextView) getActivity().findViewById(R.id.myProfileGenderValue);
		}
		return mGenderField;
	}
	
	public TextView getmAgeField() {
		if (mAgeField == null) {
			mAgeField = (TextView) getActivity().findViewById(R.id.myProfileAgeValue);
		}
		return mAgeField;
	}
	
	public TextView getmMeetsField() {
		if (mMeetsField == null) {
			mMeetsField = (TextView) getActivity().findViewById(R.id.myProfileMeetsValue);
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
	        if (data.profileImage != null)
	        {
	        	DownloadImageTask downloadImageTask = new DownloadImageTask(getActivity(), profileImage);
	        	downloadImageTask.execute(data.profileImage);
	        }
	        
			Button takePicButton = getmTakePicButton();
			takePicButton.setOnClickListener(takePicOnClickListener);
			
			Button selectImgButton = getmSelectImgButton();
			selectImgButton.setOnClickListener(selectImgOnClickListener);
	        
	        showProgress(false);
        }
    }
	
	public ProfileFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile_details, 
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == 8 && data != null) {
	            uploadImage(data.getData());
	        }
			else if (requestCode == 16 && data != null)
			{
				uploadImage(data.getData());
			}
		}
	}
	
	private void uploadImage(Uri imageUri) {
		ImageHandler handler = new ImageHandler();
        handler.setMaxImageHeight(140);
        handler.setMaxImageWidth(200);
        
        try {
        	Bitmap picture = handler.getCorrectlyOrientedImage(getActivity().getApplicationContext(), imageUri);
			UploadImageTask uploadImageTask = new UploadImageTask(getActivity(), picture);
			uploadImageTask.execute(picture);
		} catch (IOException e) {
			Toast.makeText(getActivity(), "Image cannot be loaded!", Toast.LENGTH_SHORT).show();
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
