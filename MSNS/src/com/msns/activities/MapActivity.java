package com.msns.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.msns.R;
import com.msns.models.Area;
import com.msns.models.Corner;
import com.msns.models.UserShortModel;
import com.msns.navdrawer.AbstractNavDrawerActivity;
import com.msns.navdrawer.NavDrawerActivityConfiguration;
import com.msns.navdrawer.NavDrawerAdapter;
import com.msns.navdrawer.NavDrawerItem;
import com.msns.navdrawer.NavMenuItem;
import com.msns.navdrawer.NavMenuSection;
import com.msns.services.GetImageTask;
import com.msns.services.GetNearbyPeopleTask;
import com.msns.services.SendLocationTask;

public class MapActivity extends AbstractNavDrawerActivity {

	private GoogleMap mMap;
	private boolean mIsInitialized = false;
	
	private ArrayList<UserShortModel> mActualPeopleList;
	
	public GoogleMap getmMap() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		return mMap;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_map);
		
		mActualPeopleList = null;
		
		this.setTitle("MSNS");
		
		GoogleMap map = getmMap();

        map.setMyLocationEnabled(true);

        Location loc = map.getMyLocation();
        if (loc != null) {
        	mIsInitialized = true;
        	LatLng detailedLoc = new LatLng(loc.getLatitude(), loc.getLongitude());

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(detailedLoc, 13));
            
            map.addMarker(new MarkerOptions()
                    .title("Me")
                    .position(detailedLoc));
        }
        
        initMap();
	}
	
	private void initMap() {
	    mMap = getmMap();

	    if (mMap != null) {
	    	mMap.setBuildingsEnabled(true);
	    	mMap.setIndoorEnabled(true);
	    	
	    	mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

	            // Use default InfoWindow frame
	            @Override
	            public View getInfoWindow(Marker args) {
	                return null;
	            }

	            // Defines the contents of the InfoWindow
	            @Override
	            public View getInfoContents(Marker args) {

	                // Getting view from the layout file info_window_layout
	                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);


	                TextView title = (TextView) v.findViewById(R.id.tvTitle);
	                title.setText(args.getTitle());

	                getmMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
	                    public void onInfoWindowClick(Marker marker) 
	                    {
	                    	if (marker.getTitle() != "Me") {
		                    	Intent intent = new Intent(MapActivity.this, OtherProfileActivity.class);
		                    	intent.putExtra("OTHER_PROFILE_NICKNAME", marker.getTitle());
		                    	startActivity(intent);
	                    	}
	                    }
	                });

	                // Returning the view containing InfoWindow contents
	                return v;
	            }
	        });  
	    	
	    	mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
				
				@Override
				public void onMyLocationChange(Location location) {
					// TODO Auto-generated method stub
					
					if (location != null) {
						SendLocationTask sendLocationTask = new SendLocationTask(MapActivity.this);
						com.msns.models.Location loc = new com.msns.models.Location();
						loc.latitude = location.getLatitude();
						loc.longitude = location.getLongitude();
						sendLocationTask.execute(loc);
						
						if (!mIsInitialized) {
							mIsInitialized = true;
							LatLng detailedLoc = new LatLng(loc.latitude, loc.longitude);
							getmMap().moveCamera(CameraUpdateFactory.newLatLngZoom(detailedLoc, 13));
				            
							getmMap().addMarker(new MarkerOptions()
				                    .title("Me")
				                    .position(detailedLoc));
						}
					}
				}
			});
	    	
	        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				
				@Override
				public void onCameraChange(CameraPosition position) {
					
					if (position != null) {
						VisibleRegion region = getmMap().getProjection().getVisibleRegion();
						Area area = new Area();
						Corner nearLeftCorner = new Corner();
						nearLeftCorner.x = region.nearLeft.longitude;
						nearLeftCorner.y = region.nearLeft.latitude;
						area.bottomLeftCorner = nearLeftCorner;
						
						Corner farRightCorner = new Corner();
						farRightCorner.x = region.farRight.longitude;
						farRightCorner.y = region.farRight.latitude;
						area.upperRightCorner = farRightCorner;
						
						GetNearbyPeopleTask getNearbyPeopleTask = new GetNearbyPeopleTask(MapActivity.this);
						getNearbyPeopleTask.execute(area);
					}
				}
			});
	    }
	}
	
	public void setNearbyPeople(ArrayList<UserShortModel> userData) {
		mActualPeopleList = userData;
		
		for (int i = 0; i < userData.size(); i++) {
			if (userData.get(i).profileImage == null) {
				getmMap().addMarker(new MarkerOptions()
	    		.title(userData.get(i).nickname).icon(BitmapDescriptorFactory.fromResource(R.drawable.navdrawer_profile)).position(new LatLng(userData.get(i).latitude, userData.get(i).longitude)));
			}
			else {
				GetImageTask getImageTask = new GetImageTask(this, userData.get(i).nickname, userData.get(i).latitude, userData.get(i).longitude);
				getImageTask.execute(userData.get(i).profileImage);
			}
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
        navDrawerActivityConfiguration.setMainLayout(R.layout.activity_map);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (resultCode == Activity.RESULT_OK)
		{
			int navigationIndex = intent.getIntExtra("NAVIGATION_PAGE", -1);
			if (navigationIndex != -1)
			{
				Intent data = new Intent();
				data.putExtra("NAVIGATION_PAGE", navigationIndex);
	        	setResult(Activity.RESULT_OK, data);
	        	finish();
			}
		}
	}
	
    @Override
    protected void onNavItemSelected(int id) {
    	Intent data = new Intent();
    	switch ((int)id) {
        case 101:
        	data.putExtra("NAVIGATION_PAGE", 0);
        	setResult(Activity.RESULT_OK, data);
        	finish();
            break;
        case 102:
        	data.putExtra("NAVIGATION_PAGE", 1);
        	setResult(Activity.RESULT_OK, data);
        	finish();
            break;
        case 103:
        	data.putExtra("NAVIGATION_PAGE", 2);
        	setResult(Activity.RESULT_OK, data);
        	finish();
        	break;
        case 201:
        	break;
        }
    }
}
