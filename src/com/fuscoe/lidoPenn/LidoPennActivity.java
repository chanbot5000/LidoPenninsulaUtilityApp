package com.fuscoe.lidoPenn;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
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
	int counter;
	LocationService ls;
	boolean deviceLocationShown = false;
	GraphicsLayer gLayer = new GraphicsLayer();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.main);

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

		// create handle on button for selecting visible layers
		ImageButton layerDialogButton = (ImageButton) findViewById(R.id.layerDialogButton);

		layerDialogButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				layerDialogMenu();

			}
		});

		//create handler for clearing layer
		ImageButton clearLayers = (ImageButton) findViewById(R.id.clearLayers);
		clearLayers.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (counter == 1) {
					try {
						mMapView.removeLayer(1);
						counter = 0;
					} finally {
					
					}

				} else {
					// counter = 1;
				}
				String counterString = Integer.toString(counter);
				Toast t = Toast.makeText(LidoPennActivity.this, counterString,
						5000);
				t.show();
			}
		});

		//////					/////////
		// add device location to map////
		//////					/////////
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

			public void onStatusChanged(Object source, STATUS status) {
				if (source == mMapView && status == STATUS.INITIALIZED) {
					ls = mMapView.getLocationService();
					ls.setAutoPan(true);
					
					
					//addPhotoPoint(lat, lon);					
				}
			}
		});
		
		//when location button is clicked, add device location to map
		ImageButton locationbutton = (ImageButton) findViewById(R.id.addDeviceLocation);
		locationbutton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {				
				if (!deviceLocationShown){
					ls.start();
					deviceLocationShown = true;
					
					Location loc = ls.getLocation();
					double lat = loc.getLatitude();
					double lon = loc.getLongitude();
					
					Point p = new Point(lat, lon);		
					SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 5, STYLE.CIRCLE);		
					Graphic graphic = new Graphic(p, sms);		
					gLayer.addGraphic(graphic);
					
					
					
				}else if(deviceLocationShown){
					ls.stop();
					deviceLocationShown = false;
				}
			}
		});
		
		//open camera activity for adding point to map
		ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
		cameraButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(LidoPennActivity.this,
						cameraActivity.class);
				startActivity(intent);

			}
		});

		
		//addPhotoPoint();
	}

	public void layerDialogMenu() {

		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		layerDialog layerDialogMenu = new layerDialog();
		layerDialogMenu.show(fragmentTransaction, "layerDialogMenu");
	}

	public void addLayer(int layer) {

		if (counter == 1) {
			mMapView.removeLayer(1);
		} else {
			counter = 1;
		}

		if (layer == 1) {
			ArcGISDynamicMapServiceLayer water = new ArcGISDynamicMapServiceLayer(
					"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_water/MapServer");
			mMapView.addLayer(water);
		} else if (layer == 2) {
			ArcGISDynamicMapServiceLayer gas = new ArcGISDynamicMapServiceLayer(
					"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_gas/MapServer");
			mMapView.addLayer(gas);
		} else if (layer == 3) {
			ArcGISDynamicMapServiceLayer sd = new ArcGISDynamicMapServiceLayer(
					"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_sewer/MapServer");
			mMapView.addLayer(sd);
		} else if (layer == 4) {
			ArcGISDynamicMapServiceLayer sewer = new ArcGISDynamicMapServiceLayer(
					"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_sd/MapServer");
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
		
		String counterString = Integer.toString(counter);

		Toast t = Toast.makeText(LidoPennActivity.this, counterString, 5000);
		t.show();
	}
	
	public void addPhotoPoint(double x, double y){
		
		//GraphicsLayer gLayer = new GraphicsLayer();
		/*double x = 33.6962;
		double y = -117.8372;
		*/
		Point p = new Point(x,y);		
		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 5, STYLE.CIRCLE);		
		Graphic graphic = new Graphic(p, sms);		
		gLayer.addGraphic(graphic);
		//mMapView.addLayer(gLayer);
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