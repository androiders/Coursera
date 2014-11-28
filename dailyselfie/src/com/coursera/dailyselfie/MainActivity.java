package com.coursera.dailyselfie;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Main activity for the daily selfi application.
 * @author androiders
 *
 */
public class MainActivity extends ListActivity {

	private int CAMERA_REQUEST_CODE = 1;
	private SelfieInfo mCurrSelfie;
	private Intent camIntent;
	
	//handler for the alarm and broadcast manager
	private SelfieAlarmHandler mAlarmHandler;
	
	SelfieViewAdapter mViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		mViewAdapter = new SelfieViewAdapter(this);
		mAlarmHandler = new SelfieAlarmHandler();
		
		setListAdapter(mViewAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//Don't show alarms while we are in the application :)
		mAlarmHandler.cancelAlarm(this);
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_camera) {
			mCurrSelfie = new SelfieInfo();
			camIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrSelfie.getUri()); // set the image file name
		    startActivityForResult(camIntent, CAMERA_REQUEST_CODE );
			//return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		//Schedule the alarm when the user quits the application or it is no longer visible
		//should probably check to see if it is already scheduled to acount for lifecycle methods
		//and also cancel it if the user starts the app before alarm is fired
		mAlarmHandler.scheduleAlarm(this);
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == CAMERA_REQUEST_CODE)
		{
			if(resultCode == RESULT_OK)
			{
				mViewAdapter.add(mCurrSelfie);
			}
			if(resultCode == RESULT_CANCELED)
			{
				//do stuff??
			}
		}
	}
}
