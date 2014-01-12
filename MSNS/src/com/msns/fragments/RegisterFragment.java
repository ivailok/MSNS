package com.msns.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

public class RegisterFragment extends Fragment {
	
	private final int MinPasswordLength = 10;
	private final int MinUsernameLength = 8;
	private final int MaxUsernameLength = 20;
	
	
	private FragmentActivity mActivity;
	private ProgressBar mProgressBar;
	private Button mRegisterButton;
	private EditText mUsernameField;
	private EditText mFirstPasswordField;
	private EditText mSecondPasswordField;
	private TextView mUsernameConstraintField;
	private TextView mPasswordConstraintField;
	private TextView mPasswordsConstraintField;
	private TextView mRegisterErrorResponse;
	
	
	private OnClickListener registerClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(validate()) {
				RegisterTask regTask = new RegisterTask();
				
				mProgressBar.setVisibility(View.VISIBLE);
				mActivity = getActivity();
				
				regTask.execute();
			}
		}
	};
	

	private void setmProgressBar(ProgressBar mProgressBar) {
		this.mProgressBar = mProgressBar;
	}

	private void setmLoginButton(Button mLoginButton) {
		this.mRegisterButton = mLoginButton;
	}
	
	private void setmUsernameField(EditText mUsernameField) {
		this.mUsernameField = mUsernameField;
	}
	
	private void setmFirstPasswordField(EditText mFirstPasswordField) {
		this.mFirstPasswordField = mFirstPasswordField;
	}
	
	private void setmSecondPasswordField(EditText mSecondPasswordField) {
		this.mSecondPasswordField = mSecondPasswordField;
	}
	
	private void setmUsernameConstraintField(TextView mUsernameConstraintField) {
		this.mUsernameConstraintField = mUsernameConstraintField;
	}
	
	private void setmPasswordConstraintField(TextView mPasswordConstraintField) {
		this.mPasswordConstraintField = mPasswordConstraintField;
	}
	
	private void setmPasswordsConstraintField(TextView mPasswordsConstraintField) {
		this.mPasswordsConstraintField = mPasswordsConstraintField;
	}
	
	private void setmRegisterErrorResponse(TextView mRegisterErrorResponse) {
		this.mRegisterErrorResponse = mRegisterErrorResponse;
	}
	
	
	public RegisterFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_register, container, false);
		
		setmUsernameField((EditText) rootView.findViewById(R.id.register_username));
		setmFirstPasswordField((EditText) rootView.findViewById(R.id.register_password_first));
		setmSecondPasswordField((EditText) rootView.findViewById(R.id.register_password_second));
		setmLoginButton((Button) rootView.findViewById(R.id.register_proceed_button));
		setmUsernameConstraintField((TextView) rootView.findViewById(R.id.username_constraint_textview));
		setmPasswordConstraintField((TextView) rootView.findViewById(R.id.password_constraint_textview));
		setmPasswordsConstraintField((TextView) rootView.findViewById(R.id.same_passwords_constraint));
		setmRegisterErrorResponse((TextView) rootView.findViewById(R.id.register_error_response));
		setmProgressBar((ProgressBar) rootView.findViewById(R.id.register_progress_bar));
		
		mRegisterButton.setOnClickListener(registerClickListener);
		
		return rootView;
	}
	
	private boolean validate() {
		String username = mUsernameField.getText().toString();
		int passwordLength = mFirstPasswordField.getText().length();
		String pass1 = mFirstPasswordField.getText().toString();
		String pass2 = mSecondPasswordField.getText().toString();
		
		if (username.length() < MinUsernameLength || username.length() > MaxUsernameLength) {
			mUsernameConstraintField.setTextColor(Color.RED);
			return false;
		}
		else {
			mUsernameConstraintField.setTextColor(Color.GRAY);
		}
		
		if (passwordLength < MinPasswordLength) {
			mPasswordConstraintField.setTextColor(Color.RED);
			return false;
		}
		else {
			mPasswordConstraintField.setTextColor(Color.GRAY);
		}
		
		if (!pass1.equals(pass2)) {
			mPasswordsConstraintField.setVisibility(View.VISIBLE);
			return false;
		}
		
		return true;
	}
	
	private class RegisterTask extends AsyncTask<Void, Void, Void> {
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
