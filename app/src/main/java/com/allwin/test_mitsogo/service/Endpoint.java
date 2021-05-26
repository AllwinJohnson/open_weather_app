package com.allwin.test_mitsogo.service;

import com.allwin.test_mitsogo.FirstFragment.model.LocationResponse;
import com.allwin.test_mitsogo.FirstFragment.model.LocationResponseItem;
import com.allwin.test_mitsogo.FirstFragment.model.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Endpoint {

   @GET("geo/1.0/reverse")
   Call<List<LocationResponseItem>> getLocation(@Query("lat") double lat, @Query("lon") double lon, @Query("limit") int limit, @Query("appid") String appId);

   @GET("data/2.5/weather")
   Call<WeatherResponse> getWeather(@Query("q") String name, @Query("appid") String appId);

}
