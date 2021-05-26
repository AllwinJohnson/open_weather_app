package com.allwin.test_mitsogo.service;


import com.allwin.test_mitsogo.constant.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Foctr5 on 30-10-2017.
 */

public class RetroClientService {

   public static OkHttpClient client;
   public static OkHttpClient HeaderLessClient;
   private static Endpoint endpoint;

   public static void configService(String endpoint) {
	  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
	  interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
	  HeaderLessClient = new OkHttpClient.Builder()
			  .readTimeout(60, TimeUnit.SECONDS)
			  .connectTimeout(60, TimeUnit.SECONDS)
			  .writeTimeout(60, TimeUnit.SECONDS)
			  .addInterceptor(interceptor)
			  .build();
	  Retrofit retrofit = new Retrofit.Builder()
			  .baseUrl(endpoint).client(HeaderLessClient)
			  .addConverterFactory(GsonConverterFactory.create())
			  .addConverterFactory(ScalarsConverterFactory.create())
			  .build();

	  setPackageService(retrofit.create(Endpoint.class));
   }


   public static Endpoint getService() {
	  if (endpoint == null) {
		 configService(Constants.BASE_URL);
	  }
	  return endpoint;
   }

   public static void setPackageService(Endpoint service) {
	  endpoint = service;
   }


}

