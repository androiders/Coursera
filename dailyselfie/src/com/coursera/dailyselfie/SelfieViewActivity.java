package com.coursera.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class SelfieViewActivity extends Activity {

	public static String EXTRA_URI_STRING = "SELFIE_URI";
	
	private Uri mImagePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mImagePath = Uri.parse(getIntent().getStringExtra(EXTRA_URI_STRING));
		
		setContentView(R.layout.selfie_view_activity);
		ImageView selfie = (ImageView) findViewById(R.id.selfie_large);
		
		  // Get the dimensions of the bitmap
//	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//	    bmOptions.inJustDecodeBounds = true;
	    Bitmap bitmap = BitmapFactory.decodeFile(mImagePath.getPath());
//	    int photoW = bmOptions.outWidth;
//	    int photoH = bmOptions.outHeight;

	    selfie.setImageBitmap(bitmap);
		
	}
	
}
