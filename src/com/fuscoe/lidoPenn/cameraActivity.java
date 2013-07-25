package com.fuscoe.lidoPenn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class cameraActivity extends Activity implements LocationListener {

	ImageView iv;
	TextView latitudeField;
	TextView longitudeField;
	LocationManager locationManager;
	String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cameradialog);

		// /////
		// get GPS coordinates from device
		// /////

		// get handle on text views for placing lat/lon text
		latitudeField = (TextView) findViewById(R.id.latitude);
		longitudeField = (TextView) findViewById(R.id.longitude);

		// get location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// define the criteria for how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		// initialize the location fields
		if (location != null) {
			onLocationChanged(location);
		} else {
			latitudeField.setText("Location not avail");
			longitudeField.setText("Location not avail");
		}

		// ////
		// activate camera stuff
		// ////
		
		/*
		iv = (ImageView) findViewById(R.id.imageView1);

		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 0); */
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		Bitmap bm = (Bitmap) data.getExtras().get("data");
		iv.setImageBitmap(bm);

	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	public void onLocationChanged(Location location) {
		double lat = (double) location.getLatitude();
		double lon = (double) location.getLongitude();
		latitudeField.setText(String.valueOf(lat));
		longitudeField.setText(String.valueOf(lon));
		
		/*
		LidoPennActivity activity = getActivity();
		activity.addPhotoPoint(lat, lat);*/

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
