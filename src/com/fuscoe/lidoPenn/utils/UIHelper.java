package com.fuscoe.lidoPenn.utils;

import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import android.app.Activity;
import android.widget.CheckBox;

public class UIHelper {
	
	public static void setCheckbox(Activity activity, int id, boolean value){
		CheckBox cb = (CheckBox) activity.findViewById(id);
		cb.setChecked(value);
	}
}
