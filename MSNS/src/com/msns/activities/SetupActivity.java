package com.msns.activities;

import java.util.ArrayList;
import java.util.List;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.msns.R;
import com.msns.models.HttpRequestObject;
import com.msns.models.UserRegisterModel;
import com.msns.services.SetupTask;

public class SetupActivity extends Activity {
	
	private LinearLayout mSetupStatus;
	private LinearLayout mDefaultSetupView;
	private EditText mNicknameField;
	private Spinner mAgeSpinner;
	private Spinner mGenderSpinner;
	private TextView mSelectedGoogleAccField;
	private Button mProvideGoogleAccButton;
	private Button mAddGoogleAccButton;
	private Button mProceedButton;
	
	
	private OnClickListener proceedButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mGender = String.valueOf(getmGenderSpinner().getSelectedItem());
			mAge = Integer.valueOf(String.valueOf(getmAgeSpinner().getSelectedItem()));
			mNickname = getmNicknameField().getText().toString();
			mUserID = getmSelectedGoogleAccField().getText().toString();
			
			if (checkValidity()) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SetupActivity.this.getApplicationContext());
				Editor editablePrefs = prefs.edit();
				editablePrefs.putString("USER_ID", mUserID);
				editablePrefs.commit();
				
				showProgress(true);
				proceed();
			}
		}
	};
	
	private OnClickListener provideGoogleAccButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.google"},
			         false, null, null, null, null);
			 startActivityForResult(intent, 0);
		}
	};
	
	private OnClickListener addGoogleAccButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent addAccountIntent = new Intent(android.provider.Settings.ACTION_ADD_ACCOUNT)
		    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[] {"com.google"});
		    startActivity(addAccountIntent);
		}
	};
	
	
	private String mNickname;
	private int mAge;
	private String mGender;
	private String mUserID;
	
	
	public LinearLayout getmSetupStatus() {
		if (this.mSetupStatus == null) {
			this.mSetupStatus = (LinearLayout) findViewById(R.id.setupStatus);
		}
		return this.mSetupStatus;
	}
	
	public LinearLayout getmDefaultSetupView() {
		if (this.mDefaultSetupView == null) {
			this.mDefaultSetupView = (LinearLayout) findViewById(R.id.defaultSetupView);
		}
		return this.mDefaultSetupView;
	}
	
	public EditText getmNicknameField() {
		if (this.mNicknameField == null) {
			this.mNicknameField = (EditText) findViewById(R.id.nicknameField);
		}
		return this.mNicknameField;
	}
	
	public Spinner getmAgeSpinner() {
		if (this.mAgeSpinner == null) {
			this.mAgeSpinner = (Spinner) findViewById(R.id.ageSelector);
		}
		return this.mAgeSpinner;
	}

	public Spinner getmGenderSpinner() {
		if (this.mGenderSpinner == null) {
			this.mGenderSpinner = (Spinner) findViewById(R.id.genderSelector);
		}
		return this.mGenderSpinner;
	}
	
	public TextView getmSelectedGoogleAccField() {
		if (this.mSelectedGoogleAccField == null) {
			this.mSelectedGoogleAccField = (TextView) findViewById(R.id.selectedGoogleAcc);
		}
		return this.mSelectedGoogleAccField;
	}
	
	public Button getmProvidedGoogleAccButton() {
		if (this.mProvideGoogleAccButton == null) {
			this.mProvideGoogleAccButton = (Button) findViewById(R.id.provideGoogleAccButton);
		}
		return this.mProvideGoogleAccButton;
	}
	
	public Button getmAddGoogleAccButton() {
		if (this.mAddGoogleAccButton == null)
		{
			this.mAddGoogleAccButton = (Button) findViewById(R.id.addGoogleAccButton);
		}
		return mAddGoogleAccButton;
	}
	
	public Button getmProceedButton() {
		if (this.mProceedButton == null)
		{
			this.mProceedButton = (Button) findViewById(R.id.proceedButton);
		}
		return mProceedButton;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		
		this.buildAgeSpinnerList();
		
		this.buildGenderSpinnerList();
		
		this.getmProvidedGoogleAccButton().setOnClickListener(provideGoogleAccButtonListener);
		
		this.getmAddGoogleAccButton().setOnClickListener(addGoogleAccButtonListener);
		
		this.getmProceedButton().setOnClickListener(proceedButtonListener);
	}
	
	protected void onActivityResult(final int requestCode, final int resultCode,
	         final Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	        this.getmSelectedGoogleAccField().setText(accountName);
	        this.mUserID = accountName;
	    }
	}

	
	private void buildAgeSpinnerList() {
		List<String> ageList = new ArrayList<String>();
		for (int i = 18; i < 120; i++)
		{
			ageList.add(String.valueOf(i));
		}
		ArrayAdapter<String> ageDataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, ageList);
		ageDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.getmAgeSpinner().setAdapter(ageDataAdapter);
	}

	private void buildGenderSpinnerList() {
		List<String> genderList = new ArrayList<String>();
		genderList.add("Male");
		genderList.add("Female");
		ArrayAdapter<String> genderDataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, genderList);
		genderDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.getmGenderSpinner().setAdapter(genderDataAdapter);
	}
	
	private boolean checkValidity()
	{
		// Reset errors.
		mNicknameField.setError(null);

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mNickname)) {
			mNicknameField.setError(getString(R.string.error_field_required));
			focusView = mNicknameField;
			cancel = true;
		} else if (mNickname.length() < 4 || mNickname.length() > 30) {
			mNicknameField.setError(getString(R.string.error_invalid_nickname));
			focusView = mNicknameField;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mUserID)) {
			mSelectedGoogleAccField.setError(getString(R.string.error_userID_required));
			focusView = mSelectedGoogleAccField;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
			return false;
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			return true;
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		final View setupStatus = getmSetupStatus();
		final View setupForm = getmDefaultSetupView();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			setupStatus.setVisibility(View.VISIBLE);
			setupStatus.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							setupStatus.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			setupForm.setVisibility(View.VISIBLE);
			setupForm.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							setupForm.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			setupStatus.setVisibility(show ? View.VISIBLE : View.GONE);
			setupForm.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	private void proceed()
	{
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//		String url = prefs.getString("EXTERNAL_SERVICES_BASE_URL", "") + "users/senduserdata";
//		
//		UserRegisterModel model = new UserRegisterModel();
//		model.age = this.mAge;
//		model.gender = this.mGender;
//		model.nickname = this.mNickname;
//		model.userID = this.mUserID;
//		
//		HttpRequestObject requestData = new HttpRequestObject();
//		requestData.setData(model);
//		requestData.setHeaders(null);
//		requestData.setUrl(url);
//		
//		SetupTask setupTask = new SetupTask(this);
//		setupTask.execute(requestData);
		
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
	}
}
