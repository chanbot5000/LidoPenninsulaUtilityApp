package com.fuscoe.lidoPenn;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.fuscoe.lidoPenn.R.id;




public class LidoPennActivity extends Activity {
	MapView mMapView = null;
	ArcGISTiledMapServiceLayer tileLayer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.main);
		
		ArcGISDynamicMapServiceLayer water = new ArcGISDynamicMapServiceLayer("http://fullcirclethinking.com/arcgisweb/rest/services/Lido/lido_water/MapServer");
		
		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView)findViewById(R.id.map);
		/* create a @ArcGISTiledMapServiceLayer */
	
		tileLayer = new ArcGISTiledMapServiceLayer(
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		// Add tiled layer to MapView
		mMapView.addLayer(tileLayer);		
		mMapView.addLayer(water);	
		
		//create handle on button for selecting visible layers
		ImageButton layerDialogButton = (ImageButton) findViewById(R.id.layerdialogbutton);
		
		layerDialogButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				//created new dialog container for this activity
				Dialog d = new Dialog(LidoPennActivity.this);
				
				//set content of the dialog viewer via setContentView
				d.setContentView(R.layout.dialog);	
				d.setTitle("Select Layer to View");
				d.setCanceledOnTouchOutside(true);
				d.show(); // use dialog.dismiss(); to close the dialog after layer selection is made
				
				d.findViewById(id.waterToggle);
				
			}
		}); 			
		
	// you need to call findViewById on the Dialog's view
	// get the dialog's view and call findViewById on it
/*
	ToggleButton waterToggleButton = (ToggleButton) findViewById(R.id.waterToggle);
	waterToggleButton.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			
			Toast toast = Toast.makeText(LidoPennActivity.this, "Test toast", 5000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	});*/
	
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