package com.allwin.test_mitsogo.FirstFragment.model;

import com.google.gson.annotations.SerializedName;

public class Rain{

	@SerializedName("3h")
	private double jsonMember3h;

	public double getJsonMember3h(){
		return jsonMember3h;
	}
}