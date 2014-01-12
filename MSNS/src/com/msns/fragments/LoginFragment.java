package com.msns.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msns.R;
import com.msns.activities.SetupActivity;

public class LoginFragment extends Fragment {
	
	private final int MinPasswordLength = 10;
	private final int MinUsernameLength = 8;
	private final int MaxUsernameLength = 20;
	
	private FragmentActivity mActivity;
	private ProgressBar mProgressBar;
	private Button mLoginButton;
	private EditText mUsernameField;
	private EditText mPasswordField;
	private TextView mUsernameConstraintField;
	private TextView mPasswordConstraintField;
	
	
	private OnClickListener loginClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(validate()) {
				LoginTask loginTask = new LoginTask();
				
				mProgressBar.setVisibility(View.VISIBLE);
				mActivity = getActivity();
				
				loginTask.execute();
			}
		}
	};
	

	private void setmProgressBar(ProgressBar mProgressBar) {
		this.mProgressBar = mProgressBar;
	}

	private void setmLoginButton(Button mLoginButton) {
		this.mLoginButton = mLoginButton;
	}
	
	private void setmUsernameField(EditText mUsernameField) {
		this.mUsernameField = mUsernameField;
	}
	
	private void setmPasswordField(EditText mPasswordField) {
		this.mPasswordField = mPasswordField;
	}
	
	private void setmUsernameConstraintField(TextView mUsernameConstraintField) {
		this.mUsernameConstraintField = mUsernameConstraintField;
	}
	
	private void setmPasswordConstraintField(TextView mPasswordConstraintField) {
		this.mPasswordConstraintField = mPasswordConstraintField;
	}

	public LoginFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login, container, false);
		
		setmUsernameField((EditText) rootView.findViewById(R.id.login_username));
		setmPasswordField((EditText) rootView.findViewById(R.id.login_password));
		setmLoginButton((Button) rootView.findViewById(R.id.login_proceed_button));
		setmUsernameConstraintField((TextView) rootView.findViewById(R.id.username_constraint_textview));
		setmPasswordConstraintField((TextView) rootView.findViewById(R.id.password_constraint_textview));
		setmProgressBar((ProgressBar) rootView.findViewById(R.id.login_progress_bar));
		
		mLoginButton.setOnClickListener(loginClickListener);
		
		return rootView;
	}
	
	private Boolean validate() {
		String username = mUsernameField.getText().toString();
		int passwordLength = mPasswordField.getText().length();
		
		if (username.length() < MinUsernameLength || username.length() > MaxUsernameLength) {
			mUsernameConstraintField.setTextColor(Color.RED);
			return false;
		}
		
		if (passwordLength < MinPasswordLength) {
			mPasswordConstraintField.setTextColor(Color.RED);
			return false;
		}
		
		return true;
	}
	
	private class LoginTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mProgressBar.setVisibility(View.GONE);
			
			Intent intent = new Intent(mActivity, SetupActivity.class);
			mActivity.startActivity(intent);
			mActivity.finish();
		}
	}
}
