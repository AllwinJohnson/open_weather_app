package com.allwin.test_mitsogo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {


   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);

	  Timber.e("onCreate");

	  Toolbar toolbar = findViewById(R.id.toolbar);
	  setSupportActionBar(toolbar);

	  if (BuildConfig.DEBUG) {
		 Timber.plant(new Timber.DebugTree());
	  }

	  FloatingActionButton fab = findViewById(R.id.fab);
	  fab.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View view) {
			showSnackBar(view, "Replace with your own action");

		 }
	  });
   }

   private void showSnackBar(View view, String content) {
	  Snackbar.make(view, content, Snackbar.LENGTH_LONG)
			  .setAction("Action", null).show();

   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.menu_main, menu);
	  return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	  // Handle action bar item clicks here. The action bar will
	  // automatically handle clicks on the Home/Up button, so long
	  // as you specify a parent activity in AndroidManifest.xml.
	  int id = item.getItemId();

	  //noinspection SimplifiableIfStatement
	  if (id == R.id.action_settings) {
		 return true;
	  }

	  return super.onOptionsItemSelected(item);
   }
}