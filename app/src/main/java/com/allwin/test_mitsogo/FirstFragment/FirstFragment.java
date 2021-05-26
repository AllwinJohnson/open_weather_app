package com.allwin.test_mitsogo.FirstFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.allwin.test_mitsogo.FirstFragment.model.LocationResponseItem;
import com.allwin.test_mitsogo.FirstFragment.model.WeatherResponse;
import com.allwin.test_mitsogo.R;
import com.allwin.test_mitsogo.constant.Constants;
import com.allwin.test_mitsogo.helper.Connectivity;
import com.allwin.test_mitsogo.service.RetroClientService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.content.Context.BATTERY_SERVICE;

public class FirstFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

   private static final int PERMISSION_REQUEST_LOCATION = 100;
   final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
   Context mContext;
   Unbinder unbinder;
   @BindView(R.id.tvBattery)
   AppCompatTextView tvBattery;
   @BindView(R.id.tvOs)
   AppCompatTextView tvOs;
   @BindView(R.id.tvMobile)
   AppCompatTextView tvMobile;
   @BindView(R.id.tvWiFi)
   AppCompatTextView tvWiFi;
   @BindView(R.id.tvExtM)
   AppCompatTextView tvExternalMemory;

   @BindView(R.id.tvIntM)
   AppCompatTextView tvInternalMemory;

   @BindView(R.id.tv_weather)
   AppCompatTextView tv_weather;

   @BindView(R.id.tv_desc)
   AppCompatTextView tv_desc;

   @BindView(R.id.tv_place)
   AppCompatTextView tv_place;

   @BindView(R.id.tv_pressure)
   AppCompatTextView tv_pressure;

   @BindView(R.id.tv_temp)
   AppCompatTextView tv_temp;

   @BindView(R.id.tv_humidity)
   AppCompatTextView tv_humidity;

   @BindView(R.id.lay3)
   CardView card;


   Location gps_loc;
   Location network_loc;
   Location final_loc;
   double longitude;
   double latitude;
   String userCountry, userAddress;
   FusedLocationProviderClient fusedLocationClient;

   public static int _getBatteryPercentage(Context context) {
	  if (Build.VERSION.SDK_INT >= 21) {
		 BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
		 return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

	  } else {
		 IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		 Intent batteryStatus = context.registerReceiver(null, iFilter);

		 int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
		 int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

		 double batteryPct = level / (double) scale;

		 return (int) (batteryPct * 100);
	  }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  // Inflate the layout for this fragment
	  Timber.v("onCreateView");

	  return inflater.inflate(R.layout.fragment_first, container, false);

   }

   public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
	  super.onViewCreated(view, savedInstanceState);
	  unbinder = ButterKnife.bind(this, view);

	  Timber.v("onViewCreated");

	  mContext = view.getContext();
	  fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

	  _init();

	  view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View view) {
			/*NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);*/

		 }
	  });
   }

   private void _init() {
	  tvBattery.setText(String.valueOf(_getBatteryPercentage(mContext) + "%"));
	  tvOs.setText(String.valueOf(_getOsDetails()));
	  _getNetworkConfig();
	  _getStorageDetail();
	  _getLocation();

   }

   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
	  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	  // BEGIN_INCLUDE(onRequestPermissionsResult)
	  if (requestCode == PERMISSION_REQUEST_LOCATION) {
		 // Request for camera permission.
		 if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			// Permission has been granted. Start camera preview Activity.
			showSnackBar("Location permission granted");
			_getLocation();

		 } else {
			// Permission request was denied.
			_requestPermission();

		 }
	  }
	  // END_INCLUDE(onRequestPermissionsResult)
   }

   private void _requestPermission() {
	  if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
			  && ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
		 // Provide an additional rationale to the user if the permission was not granted
		 // and the user would benefit from additional context for the use of the permission.
		 // Display a SnackBar with cda button to request the missing permission.
		 Snackbar.make(requireActivity().findViewById(android.R.id.content)
				 , "Location permission require to get weather report"
				 , Snackbar.LENGTH_INDEFINITE)
				 .setAction("Grant", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
					   // Request the permission
					   requestPermissions(permissions, PERMISSION_REQUEST_LOCATION);

					}
				 }).show();

	  }
   }

   private void _getLocation() {
	  if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
			  && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

		 requestPermissions(permissions, PERMISSION_REQUEST_LOCATION);

	  } else {
		 fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
			@Override
			public void onSuccess(Location location) {
			   // Got last known location. In some rare situations this can be null.
			   Timber.e(" location " + location);

			   if (location != null) {
				  // Logic to handle location object
				  double lat = location.getLatitude();
				  double lon = location.getLongitude();
				  _getWeatherLocation(lat, lon);

			   } else {
				  showSnackBar("oops, weather data not available at this time:)");

			   }
			}
		 });
	  }
   }

   private void _getWeatherLocation(double lat, double lon) {
	  Call<List<LocationResponseItem>> weatherData = RetroClientService.getService().getLocation(lat, lon, 1, Constants.weather_token);
	  weatherData.enqueue(new Callback<List<LocationResponseItem>>() {
		 @Override
		 public void onResponse(@NonNull Call<List<LocationResponseItem>> call, @NonNull Response<List<LocationResponseItem>> response) {
			Timber.e(" success ");
			if (response.isSuccessful()) {
			   String data = null;

			   if (!TextUtils.isEmpty(response.body().get(0).getName())) {
				  data = response.body().get(0).getName();
			   }

			   if (data != null) {
				  _getWeatherData(data);

			   } else {
				  showSnackBar("oops, weather data not available at this time:)");

			   }
			} else {
			   showSnackBar("oops, weather data not available at this time:)");

			}
		 }

		 @Override
		 public void onFailure(@NonNull Call<List<LocationResponseItem>> call, @NonNull Throwable t) {
			showSnackBar("oops, weather data not available at this time:)");

		 }
	  });
   }

   private void _getWeatherData(String data) {
	  Call<WeatherResponse> weatherData = RetroClientService.getService().getWeather(data, Constants.weather_token);
	  weatherData.enqueue(new Callback<WeatherResponse>() {
		 @Override
		 public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
			if (response.isSuccessful()) {
			   Timber.e(" Place      " + response.body().getName());
			   Timber.e(" atmosphere " + response.body().getWeather().get(0).getMain());
			   Timber.e("     descr  " + response.body().getWeather().get(0).getDescription());
			   Timber.e("  	url   " + Constants.image_base + response.body().getWeather().get(0).getIcon().trim() + ".png");
			   Timber.e(" Visibility " + response.body().getVisibility());
			   Timber.e(" Temp 	  " + convertFahrenheitToCelcius((int) response.body().getMain().getTemp()));
			   Timber.e(" Feels like " + convertFahrenheitToCelcius((int) response.body().getMain().getFeelsLike()));
			   Timber.e(" Humidity   " + response.body().getMain().getHumidity());
			   Timber.e(" Pressure   " + response.body().getMain().getPressure());

			   tv_place.setText(String.valueOf(response.body().getName()));
			   tv_weather.setText(String.valueOf(response.body().getWeather().get(0).getMain()));
			   tv_desc.setText(String.valueOf(response.body().getWeather().get(0).getDescription()));
			   tv_humidity.setText(String.valueOf(response.body().getMain().getHumidity()));
			   tv_temp.setText(String.valueOf(convertFahrenheitToCelcius((int) response.body().getMain().getTemp())));
			   tv_pressure.setText(String.valueOf(response.body().getMain().getPressure()));

			   card.setVisibility(View.VISIBLE);

			} else {
			   showSnackBar("oops, weather data not available at this time:)");

			}
		 }

		 @Override
		 public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
			showSnackBar("oops, weather data not available at this time:)");

		 }
	  });
   }

   @SuppressLint("SetTextI18n")
   private void _getStorageDetail() {
	  //internal
	  double totalInternalMemory = new File(requireActivity().getFilesDir().getAbsoluteFile().toString()).getTotalSpace();
	  int totMb = (int) (totalInternalMemory / (1024 * 1024));
	  double availableSize = new File(requireActivity().getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
	  int freeMb = (int) (availableSize / (1024 * 1024));

	  tvInternalMemory.setText(" Internal Storage \n " +
			  "Free  	: " + freeMb / (1024) + " GB\n" +
			  "Total  	: " + totMb / (1024) + " GB");

	  if (isExternalStorageReadable() && isExternalStorageWritable()) {
		 //external
		 long freeBytesExternal = new File(requireActivity().getExternalFilesDir(null).toString()).getFreeSpace();
		 int free = (int) (freeBytesExternal / (1024 * 1024));
		 long totalExternalMemory = new File(requireActivity().getExternalFilesDir(null).toString()).getTotalSpace();
		 int total = (int) (totalExternalMemory / (1024 * 1024));

		 tvExternalMemory.setText(" External Storage \n " +
				 "Free  	: " + free / (1024) + " GB\n" +
				 "Total  	: " + total / (1024) + " GB");
	  } else {
		 tvExternalMemory.setText("External storage not available");

	  }
   }

   private void _getNetworkConfig() {
	  if (Connectivity.isConnectedWifi(mContext)) {
		 tvMobile.setAlpha(0.5f);
	  } else if (Connectivity.isConnectedMobile(mContext)) {
		 tvWiFi.setAlpha(0.5f);
	  }
   }

   private String _getOsDetails() {
	  StringBuilder builder = new StringBuilder();
	  builder.append("android : ").append(Build.VERSION.RELEASE);

	  Field[] fields = Build.VERSION_CODES.class.getFields();
	  for (Field field : fields) {
		 String fieldName = field.getName();
		 int fieldValue = -1;

		 try {
			fieldValue = field.getInt(new Object());
		 } catch (IllegalArgumentException e) {
			e.printStackTrace();
		 } catch (IllegalAccessException e) {
			e.printStackTrace();
		 } catch (NullPointerException e) {
			e.printStackTrace();
		 }

		 if (fieldValue == Build.VERSION.SDK_INT) {
			builder.append(" : ").append(fieldName).append(" : ");
			builder.append("sdk=").append(fieldValue);
		 }
	  }

	  return builder.toString();
   }

   @Override
   public void onResume() {
	  super.onResume();
	  Timber.v("onResume");
	  tvBattery.setText(String.valueOf(_getBatteryPercentage(mContext)));
	  _getNetworkConfig();
	  _getStorageDetail();
//	  _getLocation();

   }

   public boolean isExternalStorageWritable() {
	  String state = Environment.getExternalStorageState();
	  if (Environment.MEDIA_MOUNTED.equals(state)) {
		 return true;
	  }
	  return false;
   }

   /* Checks if external storage is available to at least read */
   public boolean isExternalStorageReadable() {
	  String state = Environment.getExternalStorageState();
	  if (Environment.MEDIA_MOUNTED.equals(state) ||
			  Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		 return true;
	  }
	  return false;
   }

   // Converts to celcius
   private int convertFahrenheitToCelcius(int fahrenheit) {
	  return (int) ((fahrenheit - 32) * 5 / 9);
   }

   private void showSnackBar(String content) {
	  Snackbar.make(requireActivity().findViewById(android.R.id.content), content, Snackbar.LENGTH_LONG).setAction("Action", null).show();

   }
}