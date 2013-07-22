package com.fuscoe.lidoPenn;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.fuscoe.lidoPenn.R.id;

public class LidoPennActivity extends Activity {
	MapView mMapView = null;
	ArcGISTiledMapServiceLayer tileLayer;
	int counter;

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

		/* add base lines
		ArcGISDynamicMapServiceLayer base = new ArcGISDynamicMapServiceLayer(
				"http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_base/MapServer");
		mMapView.addLayer(base);*/

		// create handle on button for selecting visible layers
		ImageButton layerDialogButton = (ImageButton) findViewById(R.id.layerdialogbutton);

		layerDialogButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				layerDialogMenu();

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
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
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