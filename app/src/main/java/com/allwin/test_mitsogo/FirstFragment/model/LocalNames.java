package com.allwin.test_mitsogo.FirstFragment.model;

import com.google.gson.annotations.SerializedName;

public class LocalNames{

	@SerializedName("feature_name")
	private String featureName;

	@SerializedName("ascii")
	private String ascii;

	public String getFeatureName(){
		return featureName;
	}

	public String getAscii(){
		return ascii;
	}
}