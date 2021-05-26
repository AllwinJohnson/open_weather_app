package com.allwin.test_mitsogo.FirstFragment.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LocationResponse{

	@SerializedName("LocationResponse")
	private List<LocationResponseItem> locationResponse;

	public List<LocationResponseItem> getLocationResponse(){
		return locationResponse;
	}
}