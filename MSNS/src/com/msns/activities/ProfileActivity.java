package com.msns.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.msns.R;
import com.msns.fragments.InterestsFragment;
import com.msns.fragments.ProfileFragment;
import com.msns.fragments.SparksFragment;
import com.msns.models.Interest;
import com.msns.models.ProfileModel;
import com.msns.navdrawer.AbstractNavDrawerActivity;
import com.msns.navdrawer.NavDrawerActivityConfiguration;
import com.msns.navdrawer.NavDrawerAdapter;
import com.msns.navdrawer.NavDrawerItem;
import com.msns.navdrawer.NavMenuItem;
import com.msns.navdrawer.NavMenuSection;

public class ProfileActivity extends AbstractNavDrawerActivity {
	
	private SectionsPagerAdapter mSectionsPagerAdapter;

	private ViewPager mViewPager;
	
	private AsyncHttpClient mClient;
	private List<Fragment> mFragments;
	
	private ProfileModel mProfileData;
	private ArrayList<Interest> mInterests;
	
	
	public AsyncHttpClient getmAsyncHttpClient() {
		if (mClient == null) {
			mClient = new AsyncHttpClient();
		}
		return mClient;
	}
	
	public List<Fragment> getmFragments() {
		mFragments = getSupportFragmentManager().getFragments();
		return mFragments;
		
	}
	
	public ProfileModel getmProfileData() {
		return mProfileData;
	}
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
        
        if (savedInstanceState == null) {
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        	String baseUrl = prefs.getString("EXTERNAL_SERVICES_BASE_URL", "");
            String getMyProfileUrl = baseUrl + "users/getmyprofile";
            String userID = prefs.getString("USER_ID", "");
            Header[] headers = {
            	new BasicHeader("Content-Type", "application/json"),
    	        new BasicHeader("Accept", "application/json"),
    	        new BasicHeader("X-userID", userID)
            };
            
            mClient = getmAsyncHttpClient();
            mClient.get(this, getMyProfileUrl, headers, null, new AsyncHttpResponseHandler() {
            	@Override
            	public void onFailure(int statusCode, Throwable error, String content) {
            		Toast.makeText(ProfileActivity.this, "No connection", Toast.LENGTH_SHORT).show();
            	};
            	
                public void onSuccess(String response) {
                    mProfileData = new Gson().fromJson(response, ProfileModel.class);
                    List<Fragment> fragments = getmFragments();
                    ((ProfileFragment)fragments.get(0)).setData(mProfileData, false);
                }
            });
            
            String getMyInterestsUrl = baseUrl + "users/getmyinterests";
            mClient.get(this, getMyInterestsUrl, headers, null, new AsyncHttpResponseHandler() {
            	@Override
            	public void onFailure(int statusCode, Throwable error, String content) {
            		Toast.makeText(ProfileActivity.this, "No connection", Toast.LENGTH_SHORT).show();
            	};
            	
                public void onSuccess(String response) {
                	Type listType = new TypeToken<ArrayList<Interest>>() {}.getType();
                	mInterests = new Gson().fromJson(response, listType);
                	int data = mInterests.size();
                	List<Fragment> fragments = getmFragments();
                    ((InterestsFragment)fragments.get(1)).setData(mInterests, false);
                }
            });
        }
        else
        {
        	mProfileData = new ProfileModel();
        	mProfileData.profileImage = savedInstanceState.getString("ProfileImage");
        	mProfileData.nickname = savedInstanceState.getString("Nickname");
        	mProfileData.gender = savedInstanceState.getString("Gender");
        	mProfileData.age = savedInstanceState.getInt("Age");
        	mProfileData.meets = savedInstanceState.getInt("Meets");
        	List<Fragment> fragments = getmFragments();
            ((ProfileFragment)fragments.get(0)).setData(mProfileData, true);
            
            mInterests = new ArrayList<Interest>();
            ArrayList<String> interests = savedInstanceState.getStringArrayList("Interests");
            for (int i = 0; i < interests.size(); i++) {
            	Interest interest = new Interest();
            	interest.Key = interests.get(i);
            	mInterests.add(interest);
            }
            ((InterestsFragment)fragments.get(1)).setData(mInterests, true);
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (mProfileData != null)
		{
			outState.putString("ProfileImage", mProfileData.profileImage);
			outState.putString("Nickname", mProfileData.nickname);
			outState.putString("Gender", mProfileData.gender);
			outState.putInt("Age", mProfileData.age);
			outState.putInt("Meets", mProfileData.meets);
		}
		if (mInterests != null) 
		{
			ArrayList<String> interests = new ArrayList<String>();
			for (int i = 0; i < mInterests.size(); i++) {
				interests.add(mInterests.get(i).Key);
			}
			outState.putStringArrayList("Interests", interests);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (resultCode == Activity.RESULT_OK)
		{
			int navigationIndex = intent.getIntExtra("NAVIGATION_PAGE", 0);
			mViewPager.setCurrentItem(navigationIndex, true);
		}
	}
   
    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
       
        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create( 100, "Profile"),
                NavMenuItem.create(101,"Details", "navdrawer_profile", false, this),
                NavMenuItem.create(102, "Interests", "navdrawer_airport", true, this),
                NavMenuItem.create(103, "Sparks", "navdrawer_airport", true, this),
                NavMenuSection.create(200, "Interactions"),
                NavMenuItem.create(201, "Map", "navdrawer_rating", false, this),
                NavMenuItem.create(202, "Notifications", "navdrawer_eula", false, this)};
       
        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.activity_profile);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);      
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(
            new NavDrawerAdapter(this, R.layout.nav_drawer_item, menu ));
        return navDrawerActivityConfiguration;
    }
   
    @Override
    protected void onNavItemSelected(int id) {
    	switch ((int)id) {
        case 101:
        	mViewPager.setCurrentItem(0, true);
            break;
        case 102:
        	mViewPager.setCurrentItem(1, true);
            break;
        case 103:
        	mViewPager.setCurrentItem(2, true);
        	break;
        case 201:
        	Intent intent = new Intent(this, MapActivity.class);
        	startActivityForResult(intent, 201);
        }
    }
    
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			
			switch (position)
			{
				case 0:
					fragment = new ProfileFragment();
					break;
				case 1:
					fragment = new InterestsFragment();
					break;
				case 2:
					fragment = new SparksFragment();
					break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.profile_section1_title).toUpperCase(l);
			case 1:
				return getString(R.string.profile_section2_title).toUpperCase(l);
			case 2:
				return getString(R.string.profile_section3_title).toUpperCase(l);
			}
			return null;
		}
	}
}
