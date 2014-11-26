package com.coursera.dailyselfie;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private static String SELFIE_STR = "_selfie_";
	private int CAMERA_REQUEST_CODE = 1;
	private Uri selfieUri;
	private Intent camIntent;
//	ListView selfieList;
	
	SelfieViewAdapter mViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		
//		selfieList = (ListView) findViewById(R.id.selfieList);
		//selfieList = getListView();
		
		camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		mViewAdapter = new SelfieViewAdapter(this);
		
		setListAdapter(mViewAdapter);
	}

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
			selfieUri = getOutputMediaFileUri(); // create a file to save the image
			camIntent.putExtra(MediaStore.EXTRA_OUTPUT, selfieUri); // set the image file name
		    startActivityForResult(camIntent, CAMERA_REQUEST_CODE );
			//return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(){
		
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }
	    
	 // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	    		SELFIE_STR  + timeStamp + ".jpg");
	    
	    return mediaFile;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == CAMERA_REQUEST_CODE)
		{
			if(resultCode == RESULT_OK)
			{
				Toast.makeText(this, "MOHAHAHAH", Toast.LENGTH_LONG).show();
				mViewAdapter.add(selfieUri);
			}
			if(resultCode == RESULT_CANCELED)
			{
				//do styff
			}
		}
	}
}
