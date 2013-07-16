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
								
			}
		}); 			
	}	

	
	public void onWaterToggled(View view){
		
		//is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();
		
		if (on){
			//enable layer to draw
			//but first lets toast to see if its working
			Toast toast = Toast.makeText(LidoPennActivity.this, "water has been toggled on", 5000);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
		} else{
			//remove layer from map
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