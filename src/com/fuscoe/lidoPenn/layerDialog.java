package com.fuscoe.lidoPenn;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class layerDialog extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getDialog().setTitle("Select a Layer");

		View v = inflater.inflate(R.layout.layerdialogmenu, container, false);

		v.findViewById(R.id.waterButton).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int layer = 1;
				layerChooser(layer);
			}
		});
		
		v.findViewById(R.id.gasButton).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int layer = 2;
				layerChooser(layer);
			}
		});

		v.findViewById(R.id.sewerButton).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int layer = 3;
				layerChooser(layer);
			}
		});
		
		v.findViewById(R.id.stormDrainButton).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int layer = 4;
				layerChooser(layer);
			}
		});
		
		v.findViewById(R.id.electricButton).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int layer = 5;
				layerChooser(layer);
			}
		});
		
		return v;

	}
	
	public void layerChooser(int layer){
		
		getDialog().dismiss();
		LidoPennActivity activity = (LidoPennActivity) getActivity();
		activity.addLayer(layer);
	}

}
