/* Copyright 2012 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the �Sample code usage restrictions� document for further information.
 *
 */

package com.fuscoe.lidoPenn;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
		
		
		//mMapView.setExtent(lidoExtent); 
		
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