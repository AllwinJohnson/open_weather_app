package com.allwin.test_mitsogo.FirstFragment.model;

import com.google.gson.annotations.SerializedName;

public class LocationResponseItem{

	@SerializedName("local_names")
	private LocalNames localNames;

	@SerializedName("country")
	private String country;

	@SerializedName("name")
	private String name;

	@SerializedName("lon")
	private double lon;

	@SerializedName("lat")
	private double lat;

	public LocalNames getLocalNames(){
		return localNames;
	}

	public String getCountry(){
		return country;
	}

	public String getName(){
		return name;
	}

	public double getLon(){
		return lon;
	}

	public double getLat(){
		return lat;
	}
}