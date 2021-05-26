package com.allwin.test_mitsogo.FirstFragment.model;

import com.google.gson.annotations.SerializedName;

public class Sys{

	@SerializedName("pod")
	private String pod;

	public String getPod(){
		return pod;
	}
}