package com.allwin.test_mitsogo.FirstFragment.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

   @SerializedName("city")
   private City city;

   @SerializedName("cnt")
   private int cnt;

   @SerializedName("cod")
   private String cod;

   @SerializedName("name")
   private String name;

   @SerializedName("message")
   private int message;

   @SerializedName("list")
   private List<ListItem> list;

   @SerializedName("weather")
   private List<WeatherItem> weather;

   @SerializedName("dt")
   private int dt;

   @SerializedName("pop")
   private int pop;

   @SerializedName("visibility")
   private int visibility;

   @SerializedName("dt_txt")
   private String dtTxt;

   @SerializedName("main")
   private Main main;

   @SerializedName("clouds")
   private Clouds clouds;

   @SerializedName("sys")
   private Sys sys;

   @SerializedName("wind")
   private Wind wind;

   @SerializedName("rain")
   private Rain rain;

   public int getDt() {
	  return dt;
   }

   public int getPop() {
	  return pop;
   }

   public int getVisibility() {
	  return visibility;
   }

   public String getDtTxt() {
	  return dtTxt;
   }

   public Main getMain() {
	  return main;
   }

   public Clouds getClouds() {
	  return clouds;
   }

   public Sys getSys() {
	  return sys;
   }

   public Wind getWind() {
	  return wind;
   }

   public Rain getRain() {
	  return rain;
   }


   public List<WeatherItem> getWeather() {
	  return weather;
   }

   public String getName() {
	  return name;
   }

   public City getCity() {
	  return city;
   }

   public int getCnt() {
	  return cnt;
   }

   public String getCod() {
	  return cod;
   }

   public int getMessage() {
	  return message;
   }

   public List<ListItem> getList() {
	  return list;
   }
}