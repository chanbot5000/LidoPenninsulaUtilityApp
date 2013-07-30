package com.fuscoe.lidoPenn;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class settingsactivity extends PreferenceActivity {
		
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
		
	}
}
