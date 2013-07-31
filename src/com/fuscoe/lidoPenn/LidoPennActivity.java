package com.fuscoe.lidoPenn;

import com.fuscoe.lidoPenn.utils.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationService;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnMapEventListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.fuscoe.lidoPenn.R.id;

public class LidoPennActivity extends Activity {
	MapView mMapView = null;
	ArcGISTiledMapServiceLayer tileLayer;
	LocationService ls;
	GraphicsLayer gLayer = new GraphicsLayer();

	// preference var setup
	public static final String SHOWDEVICELOCATION = "pref_devicelocation";
	private SharedPreferences settings;
	private OnSharedPreferenceChangeListener listener;

	ArcGISDynamicMapServiceLayer activeUtilityLayer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.main);

		// //////
		// set up preferences
		// //////

		// set default values on 1st time opening app
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);

		// variable containing key/value pairs of each preference
		settings = PreferenceManager.getDefaultSharedPreferences(this);

		listener = new OnSharedPreferenceChangeListener() {

			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {

				boolean deviceLocation = settings.getBoolean(key, false);

				// logic to show/hide device location on preference change
				if (deviceLocation == true) {
					ls.start();
				} else if (deviceLocation == false) {
					ls.stop();
				}
			}
		};

		settings.registerOnSharedPreferenceChangeListener(listener);

		ImageButton prefButton = (ImageButton) findViewById(R.id.preferences);
		prefButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(LidoPennActivity.this,
						settingsactivity.class);
				startActivity(intent);
			}
		});

		// //////
		// set up map stuff
		// //////

		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView) findViewById(R.id.map);
		/* create a @ArcGISTiledMapServiceLayer */

		tileLayer = new ArcGISTiledMapServiceLayer(
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/");

		// Add tiled layer to MapView
		mMapView.addLayer(tileLayer);
		mMapView.addLayer(gLayer);		

		// check if GPS is turned on, if not, prompt user to turn it on
		checkIfGPSOn();

		// create handle on button for adding a layer to map
		ImageButton layerDialogButton = (ImageButton) findViewById(R.id.layerDialogButton);

		layerDialogButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				layerDialogMenu();

			}
		});

		// create handler for clearing layer
		ImageButton clearLayers = (ImageButton) findViewById(R.id.clearLayers);
		clearLayers.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {
					mMapView.removeLayer(activeUtilityLayer);
				} finally {
					Toast t = Toast.makeText(LidoPennActivity.this,
							"Could not remove utility layer", 5000);
					t.show();
				}
			}

		});

		// //// /////////
		// add device location to map////
		// //// /////////
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

			public void onStatusChanged(Object source, STATUS status) {
				if (source == mMapView && status == STATUS.INITIALIZED) {
					ls = mMapView.getLocationService();
					ls.setAutoPan(false);					
				}

				// check if device location preference is true, if true, set
				// device location on map
				if (settings.getBoolean(SHOWDEVICELOCATION, false) == true) {
					ls.start();
				}
			}
		});

		// //////
		// open camera activity for adding point to map
		// //////
		ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
		cameraButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(LidoPennActivity.this,
						cameraActivity.class);
				startActivity(intent);

			}
		});

	}

	public void layerDialogMenu() {

		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		layerDialog layerDialogMenu = new layerDialog();
		layerDialogMenu.show(fragmentTransaction, "layerDialogMenu");
	}

	public void addLayer(int layer) {
		
		try {
			mMapView.removeLayer(activeUtilityLayer);
		} finally {
			
		}
		
		if (layer == 1) {
			ArcGISDynamicMapServiceLayer water = new ArcGISDynamicMapServiceLayer(
					"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_water/MapServer");
			activeUtilityLayer = water;
			mMapView.addLayer(water);
		} else if (layer == 2) {
			ArcGISDynamicMapServiceLayer gas = new ArcGISDynamicMapServiceLayer(
					"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_gas/MapServer");
			activeUtilityLayer = gas;
			mMapView.addLayer(gas);
		} else if (layer == 3) {
			ArcGISDynamicMapServiceLayer sd = new ArcGISDynamicMapServiceLayer(
					"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_sewer/MapServer");
			activeUtilityLayer = sd;
			mMapView.addLayer(sd);
		} else if (layer == 4) {
			ArcGISDynamicMapServiceLayer sewer = new ArcGISDynamicMapServiceLayer(
					"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_sd/MapServer");
			activeUtilityLayer = sewer;
			mMapView.addLayer(sewer);
		} else if (layer == 5) {
			Toast t = Toast.makeText(LidoPennActivity.this,
					"Electric Dataset not yet ready for viewing", 5000);
			t.show();

			Toast t2 = Toast.makeText(LidoPennActivity.this,
					"The app has become unstable and will likely crash soon",
					5000);
			t2.show();

		}

	}

	public void addPhotoPoint(double x, double y) {

		Location loc = ls.getLocation();
		double locy = loc.getLatitude();
		double locx = loc.getLongitude();

		Point wgspoint = new Point(locx, locy);
		Point mapPoint = (Point) GeometryEngine.project(wgspoint,
				SpatialReference.create(4326), mMapView.getSpatialReference());

		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 5,
				STYLE.CIRCLE);
		Graphic graphic = new Graphic(mapPoint, sms);
		gLayer.addGraphic(graphic);
	}

	public void checkIfGPSOn() {
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// check if GPS is enabled, if not, send user to GPS settings
		if (!enabled) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LidoPennActivity.this);
			builder.setMessage("GPS is not enabled. Would you like to enable it?");
			builder.setCancelable(true);
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							Intent intent = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);

						}
					});

			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();

						}
					});

			AlertDialog alert = builder.create();
			alert.show();

		}
	}

	public void refreshDisplay(View v) {
		String deviceLocationPrefValue = settings.getString(SHOWDEVICELOCATION,
				"not able to get value");
		Toast toast = Toast.makeText(LidoPennActivity.this,
				"Checkbox is checked", 5000);
		toast.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.unpause();
	}

}